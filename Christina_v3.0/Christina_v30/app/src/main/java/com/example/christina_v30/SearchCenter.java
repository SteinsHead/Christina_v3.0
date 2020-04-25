package com.example.christina_v30;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class SearchCenter extends AppCompatActivity {
    private SearchView real_search;
    private TextView back_text;
    private ListView result_list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);

        find_views();



    }

    public void find_views(){
        real_search = (SearchView) findViewById(R.id.real_search);
        back_text = (TextView) findViewById(R.id.back_text);
        result_list = (ListView) findViewById(R.id.result_list);
    }
}
