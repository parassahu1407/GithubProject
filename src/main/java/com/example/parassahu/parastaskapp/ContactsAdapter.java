package com.example.parassahu.parastaskapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {

    private List<Person> data;
    public ContactsAdapter(List<Person> data)
    {
        this.data = data;
    }
    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.list_item_layout, parent,false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        Person person = data.get(position);
        String Name = person.getName();
        String MoNumber = person.getPhoneNo();
        holder.txtName.setText(Name);
        holder.txtMoNumber.setText(MoNumber);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ContactViewHolder  extends RecyclerView.ViewHolder
    {
        TextView txtName;
        TextView txtMoNumber;

        public ContactViewHolder(View itemView) {
            super(itemView);

            txtName = (TextView) itemView.findViewById(R.id.textView);
            txtMoNumber = (TextView) itemView.findViewById(R.id.textView2);
        }
    }
}
