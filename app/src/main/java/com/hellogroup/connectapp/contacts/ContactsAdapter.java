package com.hellogroup.connectapp.contacts;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.hellogroup.connectapp.R;
import com.hellogroup.connectapp.data.Contact;
import com.hellogroup.connectapp.util.async_task_thread_pool.AsyncTaskEx;
import com.hellogroup.connectapp.util.async_task_thread_pool.AsyncTaskThreadPool;
import com.hellogroup.connectapp.util.library.SearchablePinnedHeaderListViewAdapter;
import com.hellogroup.connectapp.util.library.StringArrayAlphabetIndexer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ContactsAdapter extends SearchablePinnedHeaderListViewAdapter<Contact> {
    private ArrayList<Contact> mContacts;
    private LayoutInflater mInflater;
    private final AsyncTaskThreadPool mAsyncTaskThreadPool = new AsyncTaskThreadPool(1, 2, 10);

    ColorGenerator generator = ColorGenerator.MATERIAL;
    TextDrawable.IBuilder builder = TextDrawable.builder()
            .beginConfig()
            .withBorder(4)
            .endConfig()
            .round();

    @Override
    public CharSequence getSectionTitle(int sectionIndex) {
        return ((StringArrayAlphabetIndexer.AlphaBetSection) getSections()[sectionIndex]).getName();
    }

    public ContactsAdapter(Context context, final ArrayList<Contact> contacts) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setData(contacts);
    }

    public void setData(final ArrayList<Contact> contacts) {
        this.mContacts = contacts;
        final String[] generatedContactNames = generateContactNames(contacts);
        setSectionIndexer(new StringArrayAlphabetIndexer(generatedContactNames, true));
    }

    public void replaceData(ArrayList<Contact> contacts) {
        setData(contacts);
        notifyDataSetChanged();
    }

    private String[] generateContactNames(final List<Contact> contacts) {
        final ArrayList<String> contactNames = new ArrayList<>();
        if (contacts != null)
            for (final Contact contactEntity : contacts)
                contactNames.add(contactEntity.displayName);
        return contactNames.toArray(new String[contactNames.size()]);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final View rootView;
        if (convertView == null) {
            holder = new ViewHolder();
            rootView = mInflater.inflate(R.layout.listview_item, parent, false);
            holder.contactImage = rootView.findViewById(R.id.contact_image);
            holder.contactName = rootView.findViewById(R.id.contact_name);
            holder.headerView = rootView.findViewById(R.id.header_text);
            rootView.setTag(holder);
        } else {
            rootView = convertView;
            holder = (ViewHolder) rootView.getTag();
        }
        final Contact contact = getItem(position);
        final String displayName = contact.displayName;
        int color = generator.getColor(displayName);
        TextDrawable drawable = builder.build(displayName.substring(0,1), color);
        holder.contactImage.setImageDrawable(drawable);
        holder.contactName.setText(displayName);

        if (holder.updateTask != null && !holder.updateTask.isCancelled())
            holder.updateTask.cancel(true);

        bindSectionHeader(holder.headerView, null, position);
        return rootView;
    }

    @Override
    public boolean doFilter(Contact item, CharSequence constraint) {
        if (TextUtils.isEmpty(constraint))
            return true;
        final String displayName = item.displayName;
        return !TextUtils.isEmpty(displayName) && displayName.toLowerCase(Locale.getDefault())
                .contains(constraint.toString().toLowerCase(Locale.getDefault()));
    }

    @Override
    public ArrayList<Contact> getOriginalList() {
        return mContacts;
    }

    private static class ViewHolder {
        //public CircularContactView friendProfileCircularContactView;
        TextView contactName, headerView;
        public ImageView contactImage;
        public AsyncTaskEx<Void, Void, Bitmap> updateTask;
    }
}
