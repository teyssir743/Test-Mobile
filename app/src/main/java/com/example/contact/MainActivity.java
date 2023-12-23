package com.example.contact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

 private DbHelper dbHelper;

 private AdapterContact adapterContact;
    private RecyclerView contactRV;
    private FloatingActionButton fab ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      //init db
       dbHelper = new DbHelper(this);

        //initialisation
        fab =findViewById(R.id.fab);
        contactRV= findViewById(R.id.contactRv);
        contactRV.setHasFixedSize(true);

        //add listener
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //move to new activity to add contact
                //intent explicite to AddEditContact Activity
                Intent intent = new Intent(MainActivity.this ,addEditContact.class);
                // + create new activity called AddEdit Contact

                startActivity(intent);
                //design AddEditContact
            }

        });

         loadData();
    }
    private void loadData(){
        adapterContact = new AdapterContact(this , dbHelper.getAlldata());
        contactRV.setAdapter(adapterContact);

    }

    protected void onResume(){
        super.onResume();
        loadData(); //refrech data
    }
}

// create a adapter class to show data in liste view