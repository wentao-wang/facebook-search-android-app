package com.example.philwang.philhw9;
import com.squareup.picasso.Picasso;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by philwang on 2017/4/22.
 */

public class ResultAdapter extends ArrayAdapter<Map<String,String>> {

    public ResultAdapter(Context context, ArrayList<Map<String,String>> arrayList) {
        super(context, -1, arrayList);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_everyone, parent, false);
        }

        ImageView photo = (ImageView) convertView.findViewById(R.id.everyPhoto);
        TextView name = (TextView) convertView.findViewById(R.id.everyName);
        Map<String,String> a= getItem(position);
            name.setText(a.get("name"));
            Picasso.with(super.getContext())
                    .load(a.get("url"))
                    .into(photo);
        System.out.println();
        return convertView;
    }
}
