/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hellogroup.connectapp.util.async_task_thread_pool;

import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.FileFilter;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * same as {@link android.os.AsyncTask} but doesn't have a limit on the number of tasks,plus it uses threads as many as the<br/>
 * number of cores , minus 1 (or a single thread if it's a single core device)
 */
public abstract class AsyncTaskEx<Params,Progress,Result>
  {
  public interface IOnFinishedListener
    {
    void onFinished();
    }

  private static final String                  LOG_TAG               ="CustomAsyncTask";
  private static final int                     CORE_POOL_SIZE        =2;
  private static final int                     MAXIMUM_POOL_SIZE     =Math.max(CORE_POOL_SIZE,getCoresCount()-1);
  private static final int                     KEEP_ALIVE            =1;
  private static final ThreadFactory           sThreadFactory        =new ThreadFactory()
                                                                       {
                                                                         private final AtomicInteger mCount =new AtomicInteger(1);

                                                                         @Override
                                                                         public Thread newThread(final Runnable r)
                                                                           {
                                                                           return new Thread(r,"CustomAsyncTask #"+mCount.getAndIncrement());
                                                                           }
                                                                       };
  private static final BlockingQueue<Runnable> sPoolWorkQueue        =new LinkedBlockingQueue<>();
  /**
   * An {@link Executor} that can be used to execute tasks in parallel.
   */
  public static final Executor                 THREAD_POOL_EXECUTOR  =new ThreadPoolExecutor(CORE_POOL_SIZE,MAXIMUM_POOL_SIZE,KEEP_ALIVE,TimeUnit.SECONDS,sPoolWorkQueue,sThreadFactory);
  /**
   * An {@link Executor} that executes tasks one at a time in serial order. This serialization is global to a
   * particular process.
   */
  // public static final Executor SERIAL_EXECUTOR =new SerialExecutor();
  private static final int                     MESSAGE_POST_RESULT   =0x1;
  private static final int                     MESSAGE_POST_PROGRESS =0x2;
  private static final InternalHandler         sHandler              =new InternalHandler();
  private static volatile Executor             sDefaultExecutor      =THREAD_POOL_EXECUTOR;
  private final WorkerRunnable<Params,Result>  mWorker;
  private final FutureTask<Result>             mFuture;
  private volatile Status                      mStatus               = Status.PENDING;
  private final AtomicBoolean                  mCancelled            =new AtomicBoolean();
  private final AtomicBoolean                  mTaskInvoked          =new AtomicBoolean();
  private final Set<IOnFinishedListener>       mOnFinishedListeners  =new HashSet<>();

  /**
   * return the number of cores of the device.<br/>
   * based on : http://stackoverflow.com/a/10377934/878126
   */
  private static int getCoresCount()
    {
    class CpuFilter implements FileFilter
      {
      @Override
      public boolean accept(final File pathname)
        {
        return Pattern.matches("cpu[0-9]+",pathname.getName());
        }
      }
    try
      {
      final File dir=new File("/sys/devices/system/cpu/");
      final File[] files=dir.listFiles(new CpuFilter());
      return files.length;
      }
    catch(final Exception e)
      {
      return Math.max(1,Runtime.getRuntime().availableProcessors());
      }
    }

  /**
   * Indicates the current status of the task. Each status will be set only once during the lifetime of a task.
   */
  public enum Status
    {
    /**
     * Indicates that the task has not been executed yet.
     */
    PENDING,
    /**
     * Indicates that the task is running.
     */
    RUNNING,
    /**
     *
     */
    FINISHED,
    }

  public AsyncTaskEx()
    {
    mWorker=new WorkerRunnable<Params,Result>()
      {
        @Override
        public Result call() throws Exception
          {
          mTaskInvoked.set(true);
          android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
          // noinspection unchecked
          return postResult(doInBackground(mParams));
          }
      };
    mFuture=new FutureTask<Result>(mWorker)
      {
        @Override
        protected void done()
          {
          try
            {
            postResultIfNotInvoked(get());
            }
          catch(final InterruptedException e)
            {
            android.util.Log.w(LOG_TAG,e);
            }
          catch(final ExecutionException e)
            {
            throw new RuntimeException("An error occured while executing doInBackground()",e.getCause());
            }
          catch(final CancellationException e)
            {
            postResultIfNotInvoked(null);
            }
          }
      };
    }

  private void postResultIfNotInvoked(final Result result)
    {
    final boolean wasTaskInvoked=mTaskInvoked.get();
    if(!wasTaskInvoked)
      postResult(result);
    }

  private Result postResult(final Result result)
    {
    @SuppressWarnings("unchecked")
    final Message message=sHandler.obtainMessage(MESSAGE_POST_RESULT,new AsyncTaskExResult<>(this,result));
    message.sendToTarget();
    return result;
    }

  public final Status getStatus()
    {
    return mStatus;
    }

  public abstract Result doInBackground(@SuppressWarnings("unchecked") Params... params);

  /**
   * Runs on the UI thread before {@link #doInBackground}.
   *
   * @see #onPostExecute
   * @see #doInBackground
   */
  protected void onPreExecute()
    {}

  public void onPostExecute(final Result result)
    {}

  protected void onProgressUpdate(@SuppressWarnings("unchecked") final Progress... values)
    {}

  protected void onCancelled(final Result result)
    {
    onCancelled();
    }

  protected void onCancelled()
    {}

  public final boolean isCancelled()
    {
    return mCancelled.get();
    }

  public final boolean cancel(final boolean mayInterruptIfRunning)
    {
    mCancelled.set(true);
    return mFuture.cancel(mayInterruptIfRunning);
    }

  public final Result get() throws InterruptedException,ExecutionException
    {
    return mFuture.get();
    }

  public final Result get(final long timeout,final TimeUnit unit) throws InterruptedException,ExecutionException,TimeoutException
    {
    return mFuture.get(timeout,unit);
    }

  public final AsyncTaskEx<Params,Progress,Result> execute(@SuppressWarnings("unchecked") final Params... params)
    {
    return executeOnExecutor(sDefaultExecutor,params);
    }

  public final AsyncTaskEx<Params,Progress,Result> executeOnExecutor(final Executor exec,@SuppressWarnings("unchecked") final Params... params)
    {
    if(mStatus!= Status.PENDING)
      switch(mStatus)
        {
        case RUNNING:
          throw new IllegalStateException("Cannot execute task:"+" the task is already running.");
        case FINISHED:
          throw new IllegalStateException("Cannot execute task:"+" the task has already been executed "+"(a task can be executed only once)");
        case PENDING:
          break;
        default:
          break;
        }
    mStatus= Status.RUNNING;
    onPreExecute();
    mWorker.mParams=params;
    exec.execute(mFuture);
    return this;
    }

  /**
   * Convenience version of {@link #execute(Object...)} for use with a simple Runnable object. See {@link #execute(Object[])} for more information on the order of execution.
   *
   * @see #execute(Object[])
   * @see #executeOnExecutor(Executor, Object[])
   */
  public static void execute(final Runnable runnable)
    {
    sDefaultExecutor.execute(runnable);
    }

  /**
   * This method can be invoked from {@link #doInBackground} to publish updates on the UI thread while the background
   * computation is still running. Each call to this method will trigger the execution of {@link #onProgressUpdate} on
   * the UI thread. {@link #onProgressUpdate} will note be called if the task has been canceled.
   *
   * @param values
   * The progress values to update the UI with.
   * @see #onProgressUpdate
   * @see #doInBackground
   */
  public final void publishProgress(@SuppressWarnings("unchecked") final Progress... values)
    {
    if(!isCancelled())
      sHandler.obtainMessage(MESSAGE_POST_PROGRESS,new AsyncTaskExResult<>(this,values)).sendToTarget();
    }

  private void finish(final Result result)
    {
    if(isCancelled())
      onCancelled(result);
    else onPostExecute(result);
    for(final IOnFinishedListener listener : mOnFinishedListeners)
      listener.onFinished();
    mStatus= Status.FINISHED;
    }

  public void addOnFinishedListener(final IOnFinishedListener onFinishedListener)
    {
    this.mOnFinishedListeners.add(onFinishedListener);
    }

  public void removeOnFinishedListener(final IOnFinishedListener onFinishedListener)
    {
    this.mOnFinishedListeners.remove(onFinishedListener);
    }

  private static class InternalHandler extends Handler
    {
    @SuppressWarnings({"unchecked","rawtypes"})
    @Override
    public void handleMessage(final Message msg)
      {
      final AsyncTaskExResult result=(AsyncTaskExResult)msg.obj;
      switch(msg.what)
        {
        case MESSAGE_POST_RESULT:
          // There is only one result
          result.mTask.finish(result.mData[0]);
          break;
        case MESSAGE_POST_PROGRESS:
          result.mTask.onProgressUpdate(result.mData);
          break;
        }
      }
    }

  private static abstract class WorkerRunnable<Params,Result> implements Callable<Result>
    {
    Params[] mParams;
    }

  @SuppressWarnings({})
  private static class AsyncTaskExResult<Data>
    {
    final AsyncTaskEx<?,?,?> mTask;
    final Data[]             mData;

    AsyncTaskExResult(final AsyncTaskEx<?,?,?> task,@SuppressWarnings("unchecked") final Data... data)
      {
      mTask=task;
      mData=data;
      }
    }
  }
