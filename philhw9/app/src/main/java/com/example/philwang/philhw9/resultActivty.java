package com.example.philwang.philhw9;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.philwang.philhw9.sharedValue.*;

public class resultActivty extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private JSONArray jsonResult;
    //for every tab
    //the last one is paging
    private ArrayList<ArrayList<Map<String, String>>> mapResult;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_activty);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.users);
        tabLayout.getTabAt(1).setIcon(R.drawable.pages);
        tabLayout.getTabAt(2).setIcon(R.drawable.events);
        tabLayout.getTabAt(3).setIcon(R.drawable.places);
        tabLayout.getTabAt(4).setIcon(R.drawable.groups);


        Intent intent = getIntent();
        String tem = intent.getStringExtra("result");


        try {
            jsonResult = new JSONArray(tem);
            transfer();
        } catch (JSONException e) {
            System.exit(-1);
        } 
        

    }

    private void transfer() {
        mapResult = new ArrayList<ArrayList<Map<String, String>>>();
        ArrayList<Map<String, String>> everyShow;
        ArrayList<Map<String, String>> everypaging = new ArrayList<Map<String, String>>();;
        Map<String, String> everyone;
        try {
            for (int i = 0; i < jsonResult.length(); i++) {
                JSONObject valuableJsonData;
                JSONArray valuableJsonArray;
                JSONObject valueablePage;


                everyShow= new ArrayList<Map<String, String>>();
                valuableJsonData=jsonResult.getJSONObject(i);
                String tem = valuableJsonData.getString("body");
                valuableJsonData = new JSONObject(tem);
                tem = valuableJsonData.getString("data");
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
                System.out.println(""+valuableJsonData.toString());
                if(valuableJsonData.has("previous"))
                    everyone.put("prev", valuableJsonData.getString("previous"));
                else
                    everyone.put("prev","");
                if(valuableJsonData.has("next"))
                    everyone.put("next", valuableJsonData.getString("next"));
                else
                    everyone.put("next","");

                everypaging.add(everyone);
                mapResult.add(everyShow);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setList(mapResult);
        setPaging(everypaging);
        System.out.println(getpagingValue().toString());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result_activty, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Bundle tem=new Bundle();
            tem.putInt("type",position);
            return resultFrag.newInstance(tem);
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Users";
                case 1:
                    return "Pages";
                case 2:
                    return "Events";
                case 3:
                    return "Places";
                case 4:
                    return "Groups";
            }
            return null;
        }
    }
}
