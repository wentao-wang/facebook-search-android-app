package com.example.philwang.philhw9;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.Map;

import static com.example.philwang.philhw9.sharedValue.*;
/**
 * Created by philwang on 2017/4/24.
 */

public class albumFrag extends Fragment {
    View view;
    int type;
    ExpandableListView expandableListView;
    public static albumFrag newInstance(Bundle args) {
        albumFrag rf = new albumFrag();
        rf.setArguments(args);
        return rf;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.album_frag, container, false);
        type = getArguments().getInt("type");
        expandableListView = (ExpandableListView) view.findViewById(R.id.grouplist);
        albumAdapter abAdp=new albumAdapter(this.getContext());
        expandableListView.setAdapter(abAdp);

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            int previousGroup = -1;
            @Override
            public void onGroupExpand(int groupPosition) {

                if(groupPosition != previousGroup)
                    expandableListView.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });


        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                return false;
            }
        });
        return view;
    }


}
