package com.example.christina_v30;

import android.app.Activity;
import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectCenter extends AppCompatActivity {
    private ImageView collect_back_image, collect_image;
    private ListView collect_list;
    private MyDatabaseHelper databaseHelper;
    public List<Map<String, String>> list = new ArrayList<Map<String, String>>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collect_layout);

        databaseHelper = new MyDatabaseHelper(CollectCenter.this, "UserInfo", null, 2);

        finds();

        list = ShowList();
        MyAdapter myAdapter = new MyAdapter(CollectCenter.this, list);
        collect_list.setAdapter(myAdapter);

        collect_back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    public List<Map<String, String>> ShowList(){
        Intent intent = getIntent();
        String id = (String) intent.getStringExtra("user_name");
        List<Map<String, String>> tmp = new ArrayList<Map<String, String>>();
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.query("Video", null, null, null, null, null, null);
        if(cursor.moveToFirst()){
            do{
                if(cursor.getString(cursor.getColumnIndex("username")).equals(id)){
                    Map<String, String> ans = new HashMap<String, String>();
                    ans.put("cover", cursor.getString(cursor.getColumnIndex("cover")));
                    ans.put("name_text", cursor.getString(cursor.getColumnIndex("name")));
                    ans.put("favorite_text", cursor.getString(cursor.getColumnIndex("favorite")));
                    ans.put("play_text", cursor.getString(cursor.getColumnIndex("play")));
                    ans.put("update_text", cursor.getString(cursor.getColumnIndex("date")));

                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        return tmp;
    }

    public void finds(){
        collect_back_image = (ImageView) findViewById(R.id.collect_back_image);
        collect_image = (ImageView) findViewById(R.id.collect_image);
        collect_list = (ListView) findViewById(R.id.collect_list);
    }
}
