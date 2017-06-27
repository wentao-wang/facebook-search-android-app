package com.example.philwang.philhw9;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

import static com.example.philwang.philhw9.sharedValue.*;

/**
 * Created by philwang on 2017/4/24.
 */

public class postAdapter extends ArrayAdapter<Map<String,String>> {
    Context context;
    public postAdapter(Context context,ArrayList<Map<String,String>>arrayList) {
        super(context,0,arrayList);
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.group_item_post, parent, false);
        }

        ImageView photo = (ImageView) convertView.findViewById(R.id.photo);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView mess = (TextView) convertView.findViewById(R.id.message);
        Map<String,String> a= getItem(position);
        Map<String,String> user=getDetailguy();
        name.setText(user.get("name"));
        date.setText(a.get("date"));
        mess.setText(a.get("message"));
        Picasso.with(super.getContext())
                .load(user.get("url"))
                .into(photo);
        return convertView;
    }

}
