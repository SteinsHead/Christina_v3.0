package com.example.christina_v30;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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


public class SearchCenter extends AppCompatActivity {
    private SearchView real_search;
    private TextView back_text;
    private ListView result_list;
    public List<Map<String, String>> search_list = new ArrayList<Map<String, String>>();
    public List<Map<String, String>> change_list = new ArrayList<Map<String, String>>();
    private String search_url =
            "https://bangumi.bilibili.com/api/timeline_v2_global";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);

        find_views();

        back_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        GetVideo getVideo = new GetVideo();
        getVideo.execute(search_url);

        MyAdapter myAdapter = new MyAdapter(SearchCenter.this, search_list);
        result_list.setAdapter(myAdapter);

        real_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                change_list = result(newText);
                MyAdapter resultAdapter = new MyAdapter(SearchCenter.this, change_list);
                result_list.setAdapter(resultAdapter);

                return false;
            }
        });
        result_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SearchCenter.this, "自己追的番就要好好看完哦",Toast.LENGTH_SHORT).show();
                Intent intent = getIntent();
                System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh" + change_list.get(position).get("name_text").toString());
                Bundle bundle = intent.getExtras();
                bundle.putString("name", change_list.get(position).get("name_text"));
                bundle.putString("cover", change_list.get(position).get("cover"));
                bundle.putString("favorite", change_list.get(position).get("favorite_text"));
                bundle.putString("play", change_list.get(position).get("play_text"));
                bundle.putString("update", change_list.get(position).get("update_text"));

                String[] str = new String[5];
//                str[0] = change_list.get(position).get("name_text").toString();
//                str[1] = change_list.get(position).get("favorite_text").toString();
//                str[2] = change_list.get(position).get("cover").toString();
//                str[3] = change_list.get(position).get("play_text").toString();
//                str[4] = change_list.get(position).get("update_text").toString();
//                bundle.putStringArray("video_info", str);
                intent.putExtras(bundle);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

    }


    public List<Map<String, String>> result(String newText){
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        for(Map<String, String> ans: search_list){
            if(ans.get("name_text").toString().contains(newText)){
                System.out.println(ans.get("name_text"));
                result.add(ans);
            }
        }
        return result;
    }

    public void find_views(){
        real_search = (SearchView) findViewById(R.id.real_search);
        back_text = (TextView) findViewById(R.id.back_text);
        result_list = (ListView) findViewById(R.id.result_list);
    }

    //访问网络的内部类
    private class GetVideo extends AsyncTask<String, String, String> {

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
                for(int i = 0; i < array.length(); i++){
                    System.out.println(array.get(i));
                    JSONObject value = array.getJSONObject(i);

                    Map<String, String> map = new HashMap<String, String>();
                    map.put("cover", value.getString("square_cover"));
                    map.put("name_text", value.getString("title"));
                    map.put("favourite_text", value.getString("favorites"));
                    map.put("play_text", value.getString("play_count"));
                    map.put("update_text", value.getString("weekday"));

                    search_list.add(map);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
