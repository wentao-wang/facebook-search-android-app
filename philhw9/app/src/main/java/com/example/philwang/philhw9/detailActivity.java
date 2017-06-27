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

public class detailActivity extends AppCompatActivity {

    int type;

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
            // Return a PlaceholderFragment (defined as a static inner class below).Bundle args
            if(position==0) {
                if(getAlbum()!=null&&type!=2) {
                    Bundle args = new Bundle();
                    args.putInt("type", type);
                    return albumFrag.newInstance(args);
                }
                else
                {
                    return null;
                }
            }
            else
            {
                if(getPost()!=null&&type!=2) {

                    Bundle args = new Bundle();
                    args.putInt("type", type);
                    return postFrag.newInstance(args);
                }
                else
                {
                    return null;
                }
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Albums";
                case 1:
                    return "Posts";

            }
            return null;
        }
    }
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private detailActivity.SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

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
        tabLayout.getTabAt(0).setIcon(R.drawable.albums);
        tabLayout.getTabAt(1).setIcon(R.drawable.posts);

        Intent intent = getIntent();
        String tem = intent.getStringExtra("result");
        type=intent.getIntExtra("type",0);

        try {
            JSONObject jsonResult = new JSONObject(tem);
            transfer(jsonResult);
        } catch (JSONException e) {
            System.out.println(e.getMessage());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void transfer(JSONObject jsonResult) throws InterruptedException {

        HashMap<String, String> theGuy = new HashMap<String, String>();
        ArrayList<Map<String, String>> album=new ArrayList<>();
        ArrayList<Map<String, String>> post=new ArrayList<Map<String, String>>();

        try {
            theGuy.put("name", jsonResult.getString("name"));
            theGuy.put("id", jsonResult.getString("id"));
            theGuy.put("url", jsonResult.getJSONObject("picture").getString("url"));
            theGuy.put("type",type+"");
            setDetailguy(theGuy);
            if (jsonResult.has("posts")) {

                JSONArray jsArr = jsonResult.getJSONArray("posts");
                for (int i = 0; i < jsArr.length(); i++) {
                    HashMap<String, String> tem = new HashMap<>();
                    JSONObject o=jsArr.getJSONObject(i);
                    if (o.has("message")) {

                        tem.put("message", o.getString("message"));
                    }
                    if (o.has("created_time")) {
                        tem.put("date",o.getJSONObject("created_time").getString("date").substring(0,19));
                    }
                    post.add(tem);

                }
//                System.out.println(post.toString());
                setPost(post);
            } else {
//                System.out.println("nothing found post");
                setPost(null);
            }
            if (jsonResult.has("albums")) {

                JSONArray jsArr = jsonResult.getJSONArray("albums");
                for (int i = 0; i < jsArr.length(); i++) {
                    HashMap<String, String> tem = new HashMap<>();
                    JSONObject o=jsArr.getJSONObject(i);
                    if (o.has("name")) {
                        tem.put("title", o.getString("name"));
                    }
                    if (o.has("photos")) {
                        JSONArray jsArr2 = o.getJSONArray("photos");
                        for(int j=0;j<jsArr2.length();j++)
                        {
                            JSONObject o2 = jsArr2.getJSONObject(j);
                            if(o2.has("picture"))
                                tem.put("pic"+j,o2.getString("picture"));
                        }
                    }
                    album.add(tem);
                }
//                System.out.println(album.toString());
                setAlbum(album);
            } else {
//                System.out.println("nothing found album");
                setAlbum(null);
            }
        } catch (JSONException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(getAlbum());
        System.out.println(getPost());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
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
     * A placeholder fragment containing a simple view.
     */
    /*public static class PlaceholderFragment extends Fragment {
        *//**
         * The fragment argument representing the section number for this
         * fragment.
         *//*
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        *//**
         * Returns a new instance of this fragment for the given section
         * number.
         *//*
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }*/


}
