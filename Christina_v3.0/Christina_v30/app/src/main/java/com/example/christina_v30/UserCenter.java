package com.example.christina_v30;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class UserCenter extends AppCompatActivity {
    private static final int user_login = 3;//设置UserCenter--LoginCenter的RequestCode

    private MyDatabaseHelper databaseHelper;


    private ImageView back_image;
    private ImageView center_image;
    private Button main_button;
    private Button active_button;
    private Button save_button;
    private Button cartoon_button;
    private String user_temp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_center);

        find_view();

        databaseHelper = new MyDatabaseHelper(UserCenter.this, "UserInfo", null, 4);

        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                Bundle bundle = intent.getExtras();
                bundle.putString("user", user_temp);
                intent.putExtras(bundle);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });



        Intent intent = getIntent();
        final String name = (String) intent.getStringExtra("user_name");
        if(!name.equals("暂时不知道是谁呢")){
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            Cursor cursor = db.query("User", null, null, null, null, null, null);
            if(cursor.moveToFirst()){
                do{
                    if(cursor.getString(cursor.getColumnIndex("username")).equals(name)){
                        Glide.with(UserCenter.this).load(cursor.getString(cursor.getColumnIndex("headimage"))).into(center_image);
                        System.out.println("iamgeaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" + cursor.getString(cursor.getColumnIndex("headimage")));
                    }
                }while (cursor.moveToNext());
            }
            cursor.close();
        }

        center_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserCenter.this, LoginCenter.class);
                Bundle bundle = new Bundle();
                bundle.putString("user_name", name);
                intent.putExtras(bundle);
                startActivityForResult(intent, user_login);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == user_login && resultCode == Activity.RESULT_OK){
            Bundle bundle = data.getExtras();
            user_temp = bundle.getString("user");
            //登录成功的情况下可以显示头像
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            Cursor cursor = db.query("User", null, null, null, null, null, null);
            if(cursor.moveToFirst()){
                do{
                    if(cursor.getString(cursor.getColumnIndex("username")).equals(user_temp)){
                        Glide.with(UserCenter.this).load(cursor.getString(cursor.getColumnIndex("headimage"))).into(center_image);
                        System.out.println("iamgeaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" + cursor.getString(cursor.getColumnIndex("headimage")));
                    }
                }while (cursor.moveToNext());
            }
            cursor.close();
        }

    }

    public void find_view(){
        back_image = (ImageView)findViewById(R.id.back_image);
        center_image = (ImageView)findViewById(R.id.center_image);
        main_button = (Button)findViewById(R.id.main_button);
        active_button = (Button)findViewById(R.id.active_button);
        save_button = (Button)findViewById(R.id.save_button);
        cartoon_button = (Button)findViewById(R.id.cartoon_button);
    }
}
