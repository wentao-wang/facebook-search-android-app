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
import java.util.HashMap;
import java.util.Map;

import static com.example.wentao.searchonfb.R.id.container;
import static com.example.wentao.searchonfb.detailData.*;

/**
 * Created by wentao on 4/25/17.
 */

public class postAdapter extends ArrayAdapter<String>{

    public postAdapter(Context context, ArrayList<String> list){
        super(context,-1,list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.inpost,parent,false);
        }
        String p = getItem(position);

        TextView time = (TextView) convertView.findViewById(R.id.textView5);
        TextView content = (TextView) convertView.findViewById(R.id.textView6);

        ImageView ic = (ImageView) convertView.findViewById(R.id.imageView5);
        Picasso.with(super.getContext()).load(iconUrl2).into(ic);


        TextView name = (TextView) convertView.findViewById(R.id.textView4);
        name.setText(userName2);


        time.setText(p);
        content.setText(postContent2.get(p));

        return convertView;



    }
}
