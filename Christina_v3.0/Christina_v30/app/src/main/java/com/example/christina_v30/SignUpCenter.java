package com.example.christina_v30;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpCenter extends AppCompatActivity {
    private TextView sign_title;
    private EditText user_sign, password_sign;
    private ImageView sign_back_image;
    private MyDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_layout);

        find_views();

        databaseHelper = new MyDatabaseHelper(SignUpCenter.this, "UserInfo", null, 2);


        sign_back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.getWritableDatabase();

                String id = user_sign.getText().toString();
                String password = password_sign.getText().toString();
                System.out.println("hahahahahahahahahahahaha!!!!!!!!!!!" + id);
                System.out.println("hahahahahahahahahahahaha!!!!!!!!!!!" + password);

                if(isExist(id, password)){ //若返回true，说明用户名已注册过
                    Toast.makeText(SignUpCenter.this, "该用户名已被占用\n请重新想个名字！", Toast.LENGTH_SHORT).show();
                }
                else {
                    InsetData(id, password);
                    Toast.makeText(SignUpCenter.this, "注册成功！", Toast.LENGTH_SHORT).show();
                    Intent intent = getIntent();
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    public void InsetData(String id, String password){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        if(id.equals("christina") || id.equals("Christina")){
            values.put("username", id);
            values.put("password", password);
            values.put("rank", "Administrator");
            db.insert("User", null, values);
            values.clear();
        }
        else{
            values.put("username", id);
            values.put("password", password);
            values.put("rank", "NormalPeople");
            db.insert("User", null, values);
            values.clear();
        }
    }

    public boolean isExist(String id, String password){
        //如果用户已经存在，返回true，如果用户不存在，返回false
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.query("User", null, null, null, null, null, null);
        if(cursor.moveToFirst()){
            do{
                if(cursor.getString(cursor.getColumnIndex("username")).equals(id)){
                    return true;
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        return false;
    }

    public void find_views(){
        sign_title = (TextView) findViewById(R.id.sign_title);
        user_sign = (EditText) findViewById(R.id.user_sign);
        password_sign = (EditText) findViewById(R.id.password_sign);
        sign_back_image = (ImageView) findViewById(R.id.sign_back_image);
    }
}
