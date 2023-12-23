package com.example.contact;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AdapterContact  extends  RecyclerView.Adapter<AdapterContact.ContactViewHolder>{
    private Context context;
    private ArrayList<ModelContact> contactList;


    // add constructor
    // alt + ins
    public  AdapterContact(Context context , ArrayList<ModelContact> contactList)
    {
        this.context=context;
        this.contactList=contactList;

    }



    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.row_contact_item , parent,false);
        ContactViewHolder vh = new ContactViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
     ModelContact modelContact = contactList.get(position);
     // get data
        // we need only 3 data
        String id = modelContact.getId();
        String image = modelContact.getImage();
        String name = modelContact.getName();

        // set data in view
        holder.contactName.setText(name);
        if(image.contains(null))
        {
            holder.contactImage.setImageResource(R.drawable.baseline_person_24);
        }
        else {
            holder.contactImage.setImageURI(Uri.parse(image));

        }
        //handle click listener
        holder.contactDial.setOnClickListener(new  View.OnClickListener(){
            @Override
            public  void onClick(View v){

            }

        });}



    @Override
    public int getItemCount() {
        return contactList.size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder
    {
        // vue fo row contact
        ImageView contactImage , contactDial ;
        TextView contactName ;
        public ContactViewHolder(@NotNull View itemView)
        {
            super(itemView);
            // init view
            contactImage = itemView.findViewById(R.id.contact_image);
            contactDial = itemView.findViewById(R.id.contact_number_dial);
            contactName = itemView.findViewById(R.id.contact_name);

        }

    }





}
