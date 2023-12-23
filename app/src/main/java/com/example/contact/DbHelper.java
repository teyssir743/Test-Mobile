package com.example.contact;//class for data base helper

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(@Nullable Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table on data base
        db.execSQL(Constants.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
      // upgrade table if any structure change in db
        // drop table if existes
        db.execSQL("DROP TABLE IF EXISTS "+ Constants.TABLE_NAME);

        // crate table
        onCreate(db);

    }


    // insert data in database
    public  long insertContact(String image , String name ,String phone ,String email , String note , String addedTime , String updateTime )
    {
        // write data on data base
        SQLiteDatabase db = this .getWritableDatabase();
        // create ContentValue class object  to save data
        ContentValues contentValues = new ContentValues();


        // Add data to ContentValues
        contentValues.put(Constants.C_IMAGE, image);
        contentValues.put(Constants.C_NAME, name);
        contentValues.put(Constants.C_PHONE, phone);
        contentValues.put(Constants.C_EMAIL, email);
        contentValues.put(Constants.C_NOTE, note);
        contentValues.put(Constants.C_ADDED_TIME, addedTime);
        contentValues.put(Constants.C_UPDATED_TIME, updateTime);

        // Insert data into the table
        long id = db.insert(Constants.TABLE_NAME, null, contentValues);

        // Close the database connection
        db.close();

        // Return the row ID of the newly inserted row, or -1 if an error occurred
        return id;
    }


// get data
    public ArrayList<ModelContact> getAlldata()
    {
        //create Array list
        ArrayList<ModelContact> arrayList = new ArrayList<>();
        // sql commanded querry
        String selectQuerry="SELECT * FROM "+ Constants.TABLE_NAME;
        //get readable db
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor=db.rawQuery(selectQuerry,null);
        //looping through all record and add to list
        if (cursor.moveToFirst())
        {do {
                ModelContact modelContact= new ModelContact
                        (
                ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_ID)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_IMAGE)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_NAME)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_PHONE)),
                       ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_EMAIL)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_NOTE)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_ADDED_TIME)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_UPDATED_TIME))
                        );

                arrayList.add(modelContact);

            }while (cursor.moveToNext());
        }
        db.close();
        return arrayList;
    }


    }






