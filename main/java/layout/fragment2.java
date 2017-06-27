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
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.wentao.searchonfb.MainActivity;
import com.example.wentao.searchonfb.R;
import com.example.wentao.searchonfb.details;
import com.example.wentao.searchonfb.fragAdapter;
import com.example.wentao.searchonfb.results;


import org.json.JSONArray;
import org.json.JSONException;
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

import static com.example.wentao.searchonfb.AllData.*;
import static com.example.wentao.searchonfb.R.id.all;
import static com.example.wentao.searchonfb.R.id.container;

public class fragment2 extends Fragment {
    private static final String tag ="wwtdebug";
private ArrayList<ArrayList> allData;
    private int type;
    private Button prev;
    private Button next;
    View view;
    public fragment2() {
        // Required empty public constructor
    }



    public static fragment2 newInstance(Bundle args) {
        fragment2 fragment = new fragment2();
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_fragment2, container, false);
        type=getArguments().getInt("type");
        allData=dataList;
        Log.e(tag,allData.toString());
        prev= (Button) view.findViewById(R.id.prev);
        next=(Button)view.findViewById(R.id.next);
        refreshButton();

        ListView listview = (ListView) view.findViewById(R.id.pageList);
        fragAdapter ada = new fragAdapter(getContext(),allData.get(type));
        listview.setAdapter(ada);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url="http://helloworldgnaw-env.us-west-2.elasticbeanstalk.com/index.php?id=";
//                JSONArray jsonArr= new JSONArray(allData);
                try{
                   Map<String,String> a=(Map<String,String>) allData.get(type).get(position);
                    String ID=  a.get("id");

                    Log.d(tag,"idd is: "+ID);
                    url+=ID;

                }catch(Exception e ) {
                    Log.d(tag, "exception"+e);
                }


                new sendDetails().execute(url);
            }
        });

        return view;

    }

private void refreshButton(){
    final Map<String,String> pageResult= (Map<String, String>) allData.get(5).get(type);
    if (pageResult.get("prev").equals("")) {
        prev.setEnabled(false);
    } else {
        prev.setEnabled(true);
        prev.setOnClickListener(new View.OnClickListener() {
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
        ArrayList<Map<String, String>> allLine = null;
        Map<String, String> oneLine = null;
        try {
            JSONObject valuableJsonData;
            JSONArray valuableJsonArray;
            allLine = new ArrayList<Map<String, String>>();
            valuableJsonData = jsonResult;
            String tem = valuableJsonData.getString("data");
            valuableJsonArray = new JSONArray(tem);

            for (int j = 0; j < valuableJsonArray.length(); j++) {
                oneLine = new HashMap<>();
                JSONObject obj = valuableJsonArray.getJSONObject(j);
                oneLine.put("id", obj.getString("id"));
                oneLine.put("name", obj.getString("name"));
                oneLine.put("picture", obj.getJSONObject("picture").getJSONObject("data").getString("url"));
                allLine.add(oneLine);
            }
            oneLine = new HashMap<>();
            tem = valuableJsonData.getString("paging");
            valuableJsonData = new JSONObject(tem);
            if(valuableJsonData.has("previous"))
                oneLine.put("prev", valuableJsonData.getString("previous"));
            else
                oneLine.put("prev","");
            if(valuableJsonData.has("next"))
                oneLine.put("next", valuableJsonData.getString("next"));
            else
                oneLine.put("next","");
        } catch (JSONException e) {
            e.printStackTrace();

        }

        allData.set(type, allLine);
        ArrayList<Map> page = allData.get(5);
        page.set(type,oneLine);
        allData.set(5, page);


        fragAdapter adapter = new fragAdapter(getContext(),allData.get(type));
        ListView list = (ListView) view.findViewById(R.id.pageList);
        list.setAdapter(adapter);
        refreshButton();
    }














//request details
    private  class sendDetails extends AsyncTask<String,Void,String>{
        private String resp;
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection c =null;
            try {
                String val=params[0];
                URL myurl=new URL(val.toString());
                c=(HttpURLConnection) myurl.openConnection();
                c.setRequestMethod("GET");
                c.setRequestProperty("Content-length","0");
                c.setUseCaches(false);
                c.setAllowUserInteraction(false);
                c.connect();
                int resCode= c.getResponseCode();
                if(resCode==HttpURLConnection.HTTP_OK){
                    BufferedReader r = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String result;
                    while(((result=r.readLine()))!=null){
                        sb.append(result);
                    }
                    resp= sb.toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
                resp = e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            Log.d(tag,"details: "+resp);
            if(resp==null){
                resp="null";
            }

            return resp;

        }


        protected void onPostExecute(String result) {
            Intent intent=new Intent(getContext(),details.class);
            intent.putExtra("detailData",result);
            startActivity(intent);

        }



    }










//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }


//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//         TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
