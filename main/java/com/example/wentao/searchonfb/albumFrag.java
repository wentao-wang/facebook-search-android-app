package com.example.wentao.searchonfb;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by wentao on 4/24/17.
 */

public class albumFrag extends AppCompatActivity {

    ExpandableListView albumEx;
    ArrayList<String> albumName;
    Map<String,ArrayList<String>> albumContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        albumEx =(ExpandableListView) findViewById(R.id.ex1);


    }



}
