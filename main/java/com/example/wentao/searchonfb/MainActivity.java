package com.example.wentao.searchonfb;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.os.AsyncTask;

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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String tag ="wwtdebug";
    ArrayList<Map> userList = new ArrayList<>();
    ArrayList<Map> pageList = new ArrayList<>();
    ArrayList<Map> eventList = new ArrayList<>();
    ArrayList<Map> placeList = new ArrayList<>();
    ArrayList<Map> groupList = new ArrayList<>();
    ArrayList<Map> pageButtonList = new ArrayList<>();
    ArrayList<ArrayList> allData = new ArrayList<>();
    AllData share = new AllData();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Button btn=(Button) findViewById(R.id.ClearButton);
        btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                EditText et=(EditText) findViewById(R.id.input);
                et.setText("");
            }
        });

//         btn2=(Button) findViewById(R.id.nav_aboutme);
//        btn2.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v){
//                startActivity(new Intent(MainActivity.this, AboutMe.class));
//            }
//        });

        Button searchBot = (Button) findViewById(R.id.SearchButton);

        Log.d(tag,"oncreate");
        //search button function
        searchBot.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                userList = new ArrayList<>();
                pageList = new ArrayList<>();
                eventList= new ArrayList<>();
                placeList= new ArrayList<>();
                groupList= new ArrayList<>();
                allData = new ArrayList<>();
                pageButtonList=new ArrayList<>();

                share.reset();
                EditText mEdit   = (EditText)findViewById(R.id.input);
                final String keyword=mEdit.getText().toString();
                if(TextUtils.isEmpty(keyword)) {
                    mEdit.setError("please input keyword");
                    return;
                }
                String url = "http://helloworldgnaw-env.us-west-2.elasticbeanstalk.com/index.php?lat=37.422&long=-122.084&keyword="+keyword;
                Log.d(tag,url);
                new AsyncTaskRunner().execute(url);

            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_set) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_favorites) {
            startActivity(new Intent(MainActivity.this, Favorites.class));

        } else if (id == R.id.nav_aboutme) {
            startActivity(new Intent(MainActivity.this, AboutMe.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //asyncTask
    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

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
            Log.d(tag,"response: "+resp);

            return resp;

        }


        @Override
        protected void onPostExecute(String result) {
            try {
                JSONArray jsonArr= new JSONArray(result);



                //user
                JSONObject userObj = jsonArr.getJSONObject(0);
//                Log.d(tag,userObj.toString());


                String userBody=userObj.getString("body");
//                Log.d(tag,userBody);

                JSONObject userData=new JSONObject(userBody);
//                Log.d(tag,userData.toString());


                if(userData.has("paging")){
                    String userPage = userData.getString("paging");
                    JSONObject userP= new JSONObject(userPage);
                    Map page1Map = new HashMap<>();
                    if(userP.has("next")){
                        page1Map.put("next",userP.getString("next"));
                    }else{
                        page1Map.put("next","");
                    }
                    if(userP.has("previous")){
                        page1Map.put("prev",userP.getString("previous"));
                    }else{
                        page1Map.put("prev","");
                    }
                    pageButtonList.add(page1Map);
                }else{
                    Map page1Map = new HashMap<>();
                    page1Map.put("next","");
                    page1Map.put("prev","");
                    pageButtonList.add(page1Map);

                }



                String userData2=userData.getString("data");
                JSONArray userDataArr= new JSONArray(userData2);
//                Log.d(tag,userDataArr.toString());

                for(int i=0; i<10;i++){
                    Map userMap = new HashMap<>();
                    String temp = userDataArr.getString(i);
                    JSONObject tempUser=new JSONObject(temp);
                    userMap.put("id",tempUser.getString("id"));
                    userMap.put("name",tempUser.getString("name"));

                    String pic=tempUser.getString("picture");
                    JSONObject picture=new JSONObject(pic);
                    String picData=picture.getString("data");
                    JSONObject pictureData=new JSONObject(picData);
                    userMap.put("picture",pictureData.getString("url"));




                    userList.add(userMap);
                }
                Log.d(tag,userList.toString());
                    allData.add(userList);

//                String test=userDataArr.getString(0);
//                Log.d(tag,test);
//
//                JSONObject user1=new JSONObject(test);
//                String pic=user1.getString("picture");
//                JSONObject picture=new JSONObject(pic);
//                String picData=picture.getString("data");
//                JSONObject pictureData=new JSONObject(picData);
//                Log.d(tag,pictureData.getString("url"));




                //page
                JSONObject pageObj = jsonArr.getJSONObject(1);
//                Log.d(tag,pageObj.toString());




                String pageBody=pageObj.getString("body");
//                Log.d(tag,pageBody);

                JSONObject pageData=new JSONObject(pageBody);
//                Log.d(tag,pageData.toString());


                if(pageData.has("paging")){
                    String pagePage = pageData.getString("paging");
                    JSONObject pageP= new JSONObject(pagePage);
                    Map page2Map = new HashMap<>();
                    if(pageP.has("next")){
                        page2Map.put("next",pageP.getString("next"));
                    }else{
                        page2Map.put("next","");
                    }
                    if(pageP.has("previous")){
                        page2Map.put("prev",pageP.getString("previous"));
                    }else{
                        page2Map.put("prev","");
                    }
                    pageButtonList.add(page2Map);
                }else{
                    Map page1Map = new HashMap<>();
                    page1Map.put("next","");
                    page1Map.put("prev","");
                    pageButtonList.add(page1Map);

                }


                String pageData2=pageData.getString("data");
                JSONArray pageDataArr= new JSONArray(pageData2);
//                Log.d(tag,pageDataArr.toString());

                for(int i=0; i<10;i++){
                    Map pageMap = new HashMap<>();
                    String temp = pageDataArr.getString(i);
                    JSONObject tempPage=new JSONObject(temp);
                    pageMap.put("id",tempPage.getString("id"));
                    pageMap.put("name",tempPage.getString("name"));

                    String pic=tempPage.getString("picture");
                    JSONObject picture=new JSONObject(pic);
                    String picData=picture.getString("data");
                    JSONObject pictureData=new JSONObject(picData);
                    pageMap.put("picture",pictureData.getString("url"));




                    pageList.add(pageMap);
                }
                allData.add(pageList);
//                Log.d(tag,allData.toString());




                //events
                JSONObject eventObj = jsonArr.getJSONObject(2);
//                Log.d(tag,pageObj.toString());






                String eventBody=eventObj.getString("body");
//                Log.d(tag,pageBody);

                JSONObject eventData=new JSONObject(eventBody);
//                Log.d(tag,pageData.toString());

                if(eventData.has("paging")){
                    String pagePage3 = eventData.getString("paging");
                    JSONObject pageP3= new JSONObject(pagePage3);
                    Map page3Map = new HashMap<>();
                    if(pageP3.has("next")){
                        page3Map.put("next",pageP3.getString("next"));
                    }else{
                        page3Map.put("next","");
                    }
                    if(pageP3.has("previous")){
                        page3Map.put("prev",pageP3.getString("previous"));
                    }else{
                        page3Map.put("prev","");
                    }
                    pageButtonList.add(page3Map);
                }else{
                    Map page1Map = new HashMap<>();
                    page1Map.put("next","");
                    page1Map.put("prev","");
                    pageButtonList.add(page1Map);

                }


                String eventData2=eventData.getString("data");
                JSONArray eventDataArr= new JSONArray(eventData2);
//                Log.d(tag,pageDataArr.toString());

                for(int i=0; i<10;i++){
                    Map eventMap = new HashMap<>();
                    String temp = eventDataArr.getString(i);
                    JSONObject tempEvent=new JSONObject(temp);
                    eventMap.put("id",tempEvent.getString("id"));
                    eventMap.put("name",tempEvent.getString("name"));

                    String pic=tempEvent.getString("picture");
                    JSONObject picture=new JSONObject(pic);
                    String picData=picture.getString("data");
                    JSONObject pictureData=new JSONObject(picData);
                    eventMap.put("picture",pictureData.getString("url"));




                    eventList.add(eventMap);
                }
                allData.add(eventList);
//                Log.d(tag,allData.toString());





                //places
                JSONObject placeObj = jsonArr.getJSONObject(3);
//                Log.d(tag,pageObj.toString());








                String placeBody=placeObj.getString("body");
//                Log.d(tag,pageBody);

                JSONObject placeData=new JSONObject(placeBody);
//                Log.d(tag,pageData.toString());


                if(placeData.has("paging")){
                    String pagePage4 = placeData.getString("paging");
                    JSONObject pageP4= new JSONObject(pagePage4);
                    Map page4Map = new HashMap<>();
                    if(pageP4.has("next")){
                        page4Map.put("next",pageP4.getString("next"));
                    }else{
                        page4Map.put("next","");
                    }
                    if(pageP4.has("previous")){
                        page4Map.put("prev",pageP4.getString("previous"));
                    }else{
                        page4Map.put("prev","");
                    }
                    pageButtonList.add(page4Map);
                }else{
                    Map page1Map = new HashMap<>();
                    page1Map.put("next","");
                    page1Map.put("prev","");
                    pageButtonList.add(page1Map);

                }





                String placeData2=placeData.getString("data");
                JSONArray placeDataArr= new JSONArray(placeData2);
//                Log.d(tag,pageDataArr.toString());

                for(int i=0; i<10;i++){
                    Map placeMap = new HashMap<>();
                    String temp = placeDataArr.getString(i);
                    JSONObject tempPlace=new JSONObject(temp);
                    placeMap.put("id",tempPlace.getString("id"));
                    placeMap.put("name",tempPlace.getString("name"));

                    String pic=tempPlace.getString("picture");
                    JSONObject picture=new JSONObject(pic);
                    String picData=picture.getString("data");
                    JSONObject pictureData=new JSONObject(picData);
                    placeMap.put("picture",pictureData.getString("url"));




                    placeList.add(placeMap);
                }
                allData.add(placeList);
//                Log.d(tag,allData.toString());




                //groups
                JSONObject groupObj = jsonArr.getJSONObject(4);
//                Log.d(tag,pageObj.toString());






                String groupBody=groupObj.getString("body");
//                Log.d(tag,pageBody);

                JSONObject groupData=new JSONObject(groupBody);
//                Log.d(tag,pageData.toString());


                if(groupData.has("paging")){
                    String pagePage5 = groupData.getString("paging");
                    JSONObject pageP5= new JSONObject(pagePage5);
                    Map page5Map = new HashMap<>();
                    if(pageP5.has("next")){
                        page5Map.put("next",pageP5.getString("next"));
                    }else{
                        page5Map.put("next","");
                    }
                    if(pageP5.has("previous")){
                        page5Map.put("prev",pageP5.getString("previous"));
                    }else{
                        page5Map.put("prev","");
                    }
                    pageButtonList.add(page5Map);
                }else{
                    Map page1Map = new HashMap<>();
                    page1Map.put("next","");
                    page1Map.put("prev","");
                    pageButtonList.add(page1Map);

                }


                String groupData2=groupData.getString("data");
                JSONArray groupDataArr= new JSONArray(groupData2);
//                Log.d(tag,pageDataArr.toString());

                for(int i=0; i<10;i++){
                    Map groupMap = new HashMap<>();
                    String temp = groupDataArr.getString(i);
                    JSONObject tempGroup=new JSONObject(temp);
                    groupMap.put("id",tempGroup.getString("id"));
                    groupMap.put("name",tempGroup.getString("name"));

                    String pic=tempGroup.getString("picture");
                    JSONObject picture=new JSONObject(pic);
                    String picData=picture.getString("data");
                    JSONObject pictureData=new JSONObject(picData);
                    groupMap.put("picture",pictureData.getString("url"));






                    groupList.add(groupMap);
                }
                allData.add(groupList);

                allData.add(pageButtonList);
                Log.d(tag,"alldata: "+allData.get(5).toString());
//                AllData share = new AllData();
                share.getData(allData);



                Intent intent=new Intent(getApplicationContext(),results.class);
                intent.putExtra("allData",allData);
                startActivity(intent);




            }
            catch (Exception e) {
                Log.d(tag, "error in main "+e.toString());
            }

        }


//        @Override
//        protected void onPreExecute() {
//            progressDialog = ProgressDialog.show(MainActivity.this,
//                    "ProgressDialog",
//                    "Wait for "+time.getText().toString()+ " seconds");
//        }


//        @Override
//        protected void onProgressUpdate(String... text) {
//            finalResult.setText(text[0]);
//
//        }
    }





}
