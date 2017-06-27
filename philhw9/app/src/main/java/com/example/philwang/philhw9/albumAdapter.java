package com.example.philwang.philhw9;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class albumAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<Map<String,String>> album;
    private ArrayList<String> title;
    private ArrayList<ArrayList<String>> pictureUrl;
    public albumAdapter(Context context)
    {
        this.context = context;
        album=getAlbum();
        title=new ArrayList<>();
        pictureUrl=new ArrayList<>();
        for(int i=0;i<album.size();i++)
        {
            ArrayList<String> tem=new ArrayList<>();
            title.add(album.get(i).get("title"));
            if(album.get(i).containsKey("pic0"))
            {
                tem.add(album.get(i).get("pic0"));
            }
            if(album.get(i).containsKey("pic1"))
            {
                tem.add(album.get(i).get("pic1"));
            }
            pictureUrl.add(tem);
        }
    }
    @Override
    public int getGroupCount() {
       return album.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return pictureUrl.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return title.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return pictureUrl.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.group_header, null);
        }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.name);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.group_item_album, null);
        }
        ImageView expandedListView = (ImageView) convertView
                .findViewById(R.id.album_pic);

        System.out.println("the url is:"+expandedListText);
        Picasso.with(context)
                .load(expandedListText)
                .into(expandedListView);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
