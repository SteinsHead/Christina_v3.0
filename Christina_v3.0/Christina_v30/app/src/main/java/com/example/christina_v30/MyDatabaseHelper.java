package com.example.christina_v30;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String CREATE_USER = "create table User ("
            + "username Text,"
            + "password Text,"
            + "headimage Text,"
            + "rank Text,"
            + "primary key (username, password, headimage))";
    private static final String CREATE_VIDEO = "create table Video ("
            + "username Text,"
            + "name Text,"
            + "cover Text,"
            + "favorite Text,"
            + "play Text,"
            + "date Text,"
            + "primary key (username, name))";
    private Context mycontext;

    public MyDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mycontext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_VIDEO);
        Toast.makeText(mycontext, "Create Database Successful !", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists User");
        db.execSQL("drop table if exists Video");
        onCreate(db);
    }
}
