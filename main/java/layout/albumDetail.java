package layout;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.wentao.searchonfb.ExpandableListAdapter;
import com.example.wentao.searchonfb.MainActivity;
import com.example.wentao.searchonfb.R;
import com.example.wentao.searchonfb.details;
import com.example.wentao.searchonfb.fragAdapter;
import com.example.wentao.searchonfb.results;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;


import static com.example.wentao.searchonfb.R.id.container;
import static com.example.wentao.searchonfb.detailData.*;

/**
 * Created by wentao on 4/25/17.
 */

public class albumDetail extends Fragment {
    public albumDetail() {
        // Required empty public constructor
    }

    public static albumDetail newInstance(Bundle args) {
        albumDetail fragment = new albumDetail();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_details, container, false);
        ExpandableListView ex = (ExpandableListView) view.findViewById(R.id.ex1);
        ExpandableListAdapter ada = new ExpandableListAdapter(getContext(), albumName2, picture2);
        ex.setAdapter(ada);



        return view;
    }


}
