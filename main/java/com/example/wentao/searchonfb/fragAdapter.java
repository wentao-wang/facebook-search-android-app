package com.example.wentao.searchonfb;

import android.content.Context;
import android.media.Image;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by wentao on 4/23/17.
 */

public class fragAdapter extends ArrayAdapter<Map<String,String>>{
    public fragAdapter(Context context, ArrayList<Map<String,String>> list){
        super(context,-1,list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.inner,parent,false);
        }

        ImageView ic = (ImageView) convertView.findViewById(R.id.icon);
        TextView name = (TextView) convertView.findViewById(R.id.itemName);
        Map<String, String> res = getItem(position);
        name.setText(res.get("name"));
        Picasso.with(super.getContext()).load(res.get("picture")).into(ic);
        return convertView;



    }





}
