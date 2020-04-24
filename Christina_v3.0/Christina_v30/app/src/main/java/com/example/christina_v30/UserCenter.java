package com.example.christina_v30;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UserCenter extends AppCompatActivity {
    private static final int user_login = 3;//设置UserCenter--LoginCenter的RequestCode


    private ImageView back_image;
    private ImageView center_image;
    private Button main_button;
    private Button active_button;
    private Button save_button;
    private Button cartoon_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_center);

        find_view();

        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        center_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserCenter.this, LoginCenter.class);
                startActivityForResult(intent, 101);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
