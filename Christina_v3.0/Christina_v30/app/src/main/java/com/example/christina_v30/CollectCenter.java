package com.example.christina_v30;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectCenter extends AppCompatActivity {
    private ImageView collect_back_image, collect_image;
    private ListView collect_list;
    private MyDatabaseHelper databaseHelper;
    public List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    private NotificationManager manager;
    private Notification notification;
    Bitmap bitmap = null;
    private String default_name = "Christina";
    private String notify_name;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collect_layout);

        databaseHelper = new MyDatabaseHelper(CollectCenter.this, "UserInfo", null, 4);

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

        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wife2);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String id = "my_channel_01";
            String name = "渠道名字";
            NotificationChannel channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(CollectCenter.this, id);
            builder.setContentTitle("Christina")
                    .setContentText("你订阅的番剧 " +  notify_name +  " 更新了")
                    .setSubText("请尽快收看")
                    .setTicker("收到Christina发来的消息")
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.wife2)
                    .setLargeIcon(bitmap)
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setAutoCancel(true);
            notification = builder.build();
            manager.notify(1, notification);
        }
    }

    public List<Map<String, String>> ShowList(){
        Intent intent = getIntent();
        String id = (String) intent.getStringExtra("user_name");
        System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh" + id);
        List<Map<String, String>> tmp = new ArrayList<Map<String, String>>();
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.query("Video", null, null, null, null, null, null);
        if(cursor.moveToFirst()){
            do{
                if(cursor.getString(cursor.getColumnIndex("username")).equals(id)){
                    System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
                    Map<String, String> ans = new HashMap<String, String>();
                    ans.put("cover", cursor.getString(cursor.getColumnIndex("cover")));
                    System.out.println(cursor.getString(cursor.getColumnIndex("cover")));
                    ans.put("name_text", cursor.getString(cursor.getColumnIndex("name")));
                    System.out.println(cursor.getString(cursor.getColumnIndex("name")));

                    notify_name = cursor.getString(cursor.getColumnIndex("name"));

                    ans.put("favorite_text", cursor.getString(cursor.getColumnIndex("favorite")));
                    ans.put("play_text", cursor.getString(cursor.getColumnIndex("play")));
                    ans.put("update_text", cursor.getString(cursor.getColumnIndex("date")));
                    tmp.add(ans);
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
