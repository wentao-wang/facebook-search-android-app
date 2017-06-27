package com.example.philwang.philhw9;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;

import static com.example.philwang.philhw9.sharedValue.*;

/**
 * Created by philwang on 2017/4/24.
 */

public class postFrag extends Fragment {

    View view;
    int type;
    ExpandableListView expandableListView;
    public static postFrag newInstance(Bundle args) {
        postFrag rf = new postFrag();
        rf.setArguments(args);
        return rf;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.post_frag, container, false);
        type = getArguments().getInt("type");
        ListView list = (ListView) view.findViewById(R.id.resultList);
//        System.out.println("type1111"+type+getPaging(type).toString());
        postAdapter adapter = new postAdapter(getContext(), getPost());
        list.setAdapter(adapter);
        return view;
    }
}
