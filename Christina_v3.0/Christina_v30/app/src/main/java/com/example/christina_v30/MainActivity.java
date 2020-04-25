package com.example.christina_v30;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final int main_user = 1;//设置MainActivity--UserCenter的RequestCode
    private static final int main_search = 2;//设置MainActivity--SearchCenter的RequestCode

//    private SearchView searchView;
    private Toolbar toolbar;
    private ListView listView;
    private ImageView main_image, main_page_image, imageView;
    private DrawerLayout drawerLayout;
    private ImageView search_image;

    private String[] main_menu = {"首页","历史记录","下载管理",
            "我的收藏","稍后再看","会员中心","联系客服","游戏中心"};
    private ArrayAdapter arrayAdapter;
    private String url =
            "https://bangumi.bilibili.com/api/timeline_v2_global";
    private ListView info_list;

    public List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        find_views();

        main_page_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

//        searchView.setQueryHint("please search here");
//        searchView.setIconifiedByDefault(true);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,
                main_menu);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

//        searchView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //点击后跳转到新页面
//                Intent intent = new Intent(MainActivity.this, SearchCenter.class);
//                startActivityForResult(intent, main_search);
//            }
//        });

        search_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchCenter.class);
                startActivityForResult(intent, main_search);
            }
        });

        main_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserCenter.class);
                startActivityForResult(intent, main_user);
            }
        });

        //接下来在这里实现信息的获取
        GetVideo getVideo = new GetVideo();
        getVideo.execute(url);

        MyAdapter myAdapter = new MyAdapter(this, list);
        info_list.setAdapter(myAdapter);

    }

    public void find_views(){
//        searchView = (SearchView) findViewById(R.id.search_part);
        drawerLayout = (DrawerLayout)findViewById(R.id.left_menu);
        listView = (ListView)findViewById(R.id.menu_list);
        toolbar = (Toolbar)findViewById(R.id.tl_custom);
        main_image = (ImageView)findViewById(R.id.main_image);
        main_page_image = (ImageView)findViewById(R.id.main_page_image);
        imageView = (ImageView)findViewById(R.id.imageView);
        info_list = (ListView)findViewById(R.id.info_list);
        search_image = (ImageView) findViewById(R.id.search_image);


    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    //访问网络的内部类
    private class GetVideo extends AsyncTask<String, String, String>{

        private String openConnection(String address){
            String result = "";
            try {
                URL url = new URL(address);
                HttpURLConnection connection =
                        (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,
                        "UTF-8"));
                String line = "";
                while ((line = reader.readLine()) != null){
                    result = result + line;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            Log.i("info", result);
            return result;
        }

        @Override
        protected String doInBackground(String... strings) {
            return openConnection(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject object = new JSONObject(s);
                JSONArray array = (JSONArray) object.get("result");

                JSONObject test = array.getJSONObject(0);
                Glide.with(MainActivity.this).load(test.getString("square_cover")).into(imageView);
                for(int i = 0; i < array.length(); i++){
                    System.out.println(array.get(i));
                    JSONObject value = array.getJSONObject(i);

                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("cover", value.getString("square_cover"));
                    map.put("name_text", value.getString("title"));
                    map.put("favourite_text", value.getString("favorites"));
                    map.put("play_text", value.getString("play_count"));
                    map.put("update_text", value.getString("weekday"));

                    list.add(map);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
