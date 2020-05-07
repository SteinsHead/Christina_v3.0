package com.example.christina_v30;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

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
//    private EditText real_search;
    private TextView back_text;
    private ListView result_list;
    private ImageView voice_image;
    public List<Map<String, String>> search_list = new ArrayList<Map<String, String>>();
    public List<Map<String, String>> change_list = new ArrayList<Map<String, String>>();
    private String search_url =
            "https://bangumi.bilibili.com/api/timeline_v2_global";
    private static String[] PERMISSION_STORAGE = {
            Manifest.permission.INTERNET,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_NETWORK_STATE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA
    };
    private static int REQUEST_PERMISSION_CODE = 101;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);

        //
        // 将“12345678”替换成您申请的APPID，申请地址：http://www.xfyun.cn
// 请勿在“=”与appid之间添加任何空字符或者转义符
        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=5e75b878");
        //

        find_views();

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, PERMISSION_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }

        voice_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSpeech(SearchCenter.this);
            }
        });

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
                System.out.println(change_list.get(position).get("name_text"));
                bundle.putString("cover", change_list.get(position).get("cover"));
                System.out.println(change_list.get(position).get("cover"));
                bundle.putString("favorite", change_list.get(position).get("favourite_text"));
                System.out.println(change_list.get(position).get("favourite_text"));
                bundle.putString("play", change_list.get(position).get("play_text"));
                System.out.println(change_list.get(position).get("play_text"));
                bundle.putString("update", change_list.get(position).get("update_text"));
                System.out.println(change_list.get(position).get("update_text"));

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
//        real_search = (EditText) findViewById(R.id.real_search);
        back_text = (TextView) findViewById(R.id.back_text);
        result_list = (ListView) findViewById(R.id.result_list);
        voice_image = (ImageView) findViewById(R.id.voice_image);
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
                for(int i = 0; i < array.length(); i++){
                    System.out.println(array.get(i));
                    JSONObject value = array.getJSONObject(i);

                    Map<String, String> map = new HashMap<String, String>();
                    map.put("cover", value.getString("square_cover"));
                    map.put("name_text", value.getString("title"));
                    map.put("favourite_text", value.getString("favorites"));
                    System.out.println(value.getString("favorites"));
                    map.put("play_text", value.getString("season_id"));
                    map.put("update_text", value.getString("lastupdate_at"));

                    search_list.add(map);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1001){
            for(int i = 0; i < permissions.length; i++){
            }
        }
    }

    /**
     * 初始化语音识别
     */
    public void initSpeech(final Context context) {
        //1.创建RecognizerDialog对象
        RecognizerDialog mDialog = new RecognizerDialog(context, null);
        //2.设置accent、language等参数
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        //3.设置回调接口
        mDialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean isLast) {
                if (!isLast) {
                    //解析语音
                    //返回的result为识别后的汉字,直接赋值到TextView上即可
                    final String result = parseVoice(recognizerResult.getResultString());

                    //添加
                    Toast.makeText(SearchCenter.this, result, Toast.LENGTH_SHORT).show();
                    change_list = result(result);
                    MyAdapter resultAdapter = new MyAdapter(SearchCenter.this, change_list);
                    result_list.setAdapter(resultAdapter);
                }
            }

            @Override
            public void onError(SpeechError speechError) {

            }
        });
        //4.显示dialog，接收语音输入
        mDialog.show();
    }

    /**
     * 解析语音json
     */
    public String parseVoice(String resultString) {
        Gson gson = new Gson();
        SearchCenter.Voice voiceBean = gson.fromJson(resultString, SearchCenter.Voice.class);

        StringBuffer sb = new StringBuffer();
        ArrayList<SearchCenter.Voice.WSBean> ws = voiceBean.ws;
        for (SearchCenter.Voice.WSBean wsBean : ws) {
            String word = wsBean.cw.get(0).w;
            sb.append(word);
        }
        return sb.toString();
    }
    /**
     * 语音对象封装
     */
    public class Voice {

        public ArrayList<SearchCenter.Voice.WSBean> ws;

        public class WSBean {
            public ArrayList<SearchCenter.Voice.CWBean> cw;
        }

        public class CWBean {
            public String w;
        }
    }


}
