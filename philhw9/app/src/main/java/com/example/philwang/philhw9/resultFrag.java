package com.example.philwang.philhw9;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.philwang.philhw9.sharedValue.*;


public class resultFrag extends Fragment {

    private int type;
    
    private Button pre;
    private Button next;
    View view;
    private static final String BACKEND="http://hw8-571.appspot.com/hw8php.php?id=";

    public static resultFrag newInstance(Bundle args) {
        resultFrag rf = new resultFrag();
        rf.setArguments(args);
        return rf;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.result_frag, container, false);
        type = getArguments().getInt("type");

        ListView list = (ListView) view.findViewById(R.id.resultList);
//        System.out.println("type1111"+type+getPaging(type).toString());
        ResultAdapter adapter = new ResultAdapter(getContext(), getList(type));
        list.setAdarepter(adapter);

        pre = (Button) view.findViewById(R.id.preButton);
        next = (Button) view.findViewById(R.id.nextButton);

//        System.out.println("type"+type+getPaging(type).toString());
        refreshButton();
        refreshDetailListner();
        return view;



    }

    private void   refreshDetailListner()
    {
        ListView list = (ListView) view.findViewById(R.id.resultList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                new detailRequest().execute(BACKEND+getList(type).get((int)id).get("id"));

            }
        });
    }

    private class detailRequest extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String input;
            StringBuilder sb = new StringBuilder();
            try {
                URL httpUrl = new URL(urls[0]);
                System.out.println(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) httpUrl.openConnection();
                InputStreamReader streamReader = new InputStreamReader(urlConnection.getInputStream());
                BufferedReader bufReader = new BufferedReader(streamReader);

                try {
                    while ((input = bufReader.readLine())!= null)
                    {
                        sb.append(input).append("\n");
                    }
                } finally {
                    urlConnection.disconnect();
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            return sb.toString();
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            System.out.println("***********"+result);
            Intent intent = new Intent(getContext(), detailActivity.class);
            intent.putExtra("result", result);
            intent.putExtra("type",type);
            startActivity(intent);
        }
    }

    private void refreshButton() {

        final  Map<String, String> pageResult = getPaging(type);
        if (pageResult.get("prev").equals("")) {
            pre.setEnabled(false);
        } else {
            pre.setEnabled(true);
            pre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new pageRequest().execute(pageResult.get("prev"));
                }
            });
        }
        if (pageResult.get("next").equals("")) {
            next.setEnabled(false);
        } else {
            next.setEnabled(true);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new pageRequest().execute(pageResult.get("next"));
                }
            });
        }
    }


    private class pageRequest extends AsyncTask<String, Void, String> {
        // create HttpClient
        @Override
        protected String doInBackground(String... urls) {
            String input;
            StringBuilder sb = new StringBuilder();

            try {
                URL httpUrl = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) httpUrl.openConnection();
                InputStreamReader streamReader = new InputStreamReader(urlConnection.getInputStream());
                BufferedReader bufReader = new BufferedReader(streamReader);

                try {
                    while ((input = bufReader.readLine()) != null) {
                        sb.append(input).append("\n");
                    }
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject tem = new JSONObject(result);
                transfer(tem);
            } catch (JSONException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private void transfer(JSONObject jsonResult) throws InterruptedException {
        ArrayList<Map<String, String>> everyShow = null;
        Map<String, String> everyone = null;
        try {
            JSONObject valuableJsonData;
            JSONArray valuableJsonArray;
            everyShow = new ArrayList<Map<String, String>>();
            valuableJsonData = jsonResult;
            String tem = valuableJsonData.getString("data");
            valuableJsonArray = new JSONArray(tem);

            for (int j = 0; j < valuableJsonArray.length(); j++) {
                everyone = new HashMap<>();
                JSONObject obj = valuableJsonArray.getJSONObject(j);
                everyone.put("id", obj.getString("id"));
                everyone.put("name", obj.getString("name"));
                everyone.put("url", obj.getJSONObject("picture").getJSONObject("data").getString("url"));
                everyShow.add(everyone);
            }
            everyone = new HashMap<>();
            tem = valuableJsonData.getString("paging");
            valuableJsonData = new JSONObject(tem);
            if(valuableJsonData.has("previous"))
                everyone.put("prev", valuableJsonData.getString("previous"));
            else
                everyone.put("prev","");
            if(valuableJsonData.has("next"))
                everyone.put("next", valuableJsonData.getString("next"));
            else
                everyone.put("next","");
        } catch (JSONException e) {
            e.printStackTrace();

        }
        ArrayList<ArrayList<Map<String, String>>> haha = getlistValue();
        haha.set(type, everyShow);
        setList(haha);
        
        ArrayList<Map<String, String>> haha2=getpagingValue();
        haha2.set(type,everyone);
        setPaging(haha2);

        ResultAdapter adapter = new ResultAdapter(getContext(), getList(type));
        ListView list = (ListView) view.findViewById(R.id.resultList);
        list.setAdapter(adapter);
        refreshButton();
    }


}


