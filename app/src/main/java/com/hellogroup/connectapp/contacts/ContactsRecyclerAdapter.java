package com.hellogroup.connectapp.contacts;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.hellogroup.connectapp.R;
import com.hellogroup.connectapp.data.Contact;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactsRecyclerAdapter extends RecyclerView.Adapter<ContactsRecyclerAdapter.ViewHolder> {
    private ArrayList<Contact> contacts;
    private ContactsActivity.ContactItemListener mItemListener;

    ColorGenerator generator = ColorGenerator.MATERIAL;
    TextDrawable.IBuilder builder = TextDrawable.builder()
            .beginConfig()
            .withBorder(4)
            .endConfig()
            .round();

    ContactsRecyclerAdapter(ArrayList<Contact> contacts, ContactsActivity.ContactItemListener itemListener) {
        this.contacts = contacts;
        mItemListener = itemListener;
    }

    void setDataSet(ArrayList<Contact> contacts) {
        this.contacts = contacts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_item, parent, false);
        return new ContactsRecyclerAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(contacts.get(position));
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.contact_name) TextView contactName;
        @BindView(R.id.contact_image) ImageView contactImage;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bind(final Contact contact) {
            int color = generator.getColor(contact.getDisplayName());
            TextDrawable drawable = builder.build(contact.getDisplayName().substring(0,1), color);
            contactImage.setImageDrawable(drawable);
            contactName.setText(contact.getDisplayName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemListener.onContactClick(contact);
                }
            });
        }

    }
}
