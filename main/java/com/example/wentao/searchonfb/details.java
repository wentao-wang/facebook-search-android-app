package com.example.wentao.searchonfb;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;

import static com.example.wentao.searchonfb.detailData.albumName2;
import static com.example.wentao.searchonfb.detailData.iconUrl2;
import static com.example.wentao.searchonfb.detailData.picture2;
import static com.example.wentao.searchonfb.detailData.postContent2;
import static com.example.wentao.searchonfb.detailData.times2;
import static com.example.wentao.searchonfb.detailData.userID2;
import static com.example.wentao.searchonfb.detailData.userName2;

public class details extends AppCompatActivity {
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    public ArrayList<String> albumName = new ArrayList<>();
    public HashMap<String, ArrayList<String>> picture = new HashMap<>();
    public String userName;
    public String userID;
    public String iconUrl;
    public HashMap<String, String> postContent = new HashMap<>();
    public ArrayList<String> times = new ArrayList<>();

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private static final String tag = "wwtdebug";
    private TabLayout tabLayout;
    public ArrayList<ArrayList> detailList;


    private void setupTabLayout() {
        tabLayout.getTabAt(0).setIcon(R.drawable.albums);
        tabLayout.getTabAt(1).setIcon(R.drawable.posts);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        callbackManager=CallbackManager.Factory.create();
        detailData shareDetail = new detailData();

        Intent intent = getIntent();
        String s = intent.getSerializableExtra("detailData").toString();
        Log.i(tag, "data in results: " + s);
        shareDetail.getData(albumName, picture, userName, iconUrl, postContent,times,userID);
        try {
            JSONObject j = new JSONObject(s);
            Log.d(tag, "json is " + j.getString("name"));
            userName = j.getString("name");
            userID = j.getString("id");
            String p = j.getString("picture");
            JSONObject jj = new JSONObject(p);
            String pData = jj.getString("url");
            Log.d(tag, pData);
            iconUrl = pData;

            if (j.getString("albums") != null) {
                String ab = j.getString("albums");
                JSONArray j2 = new JSONArray(ab);
                for (int i = 0; i < j2.length(); i++) {
                    String s2 = j2.getString(i);
                    JSONObject j3 = new JSONObject(s2);
                    Log.d(tag, "ab data " + j3.toString());
                    albumName.add(j3.getString("name"));

                    ArrayList<String> photoURL = new ArrayList<>();
                    if (j3.getString("photos") != null) {
                        String s3 = j3.getString("photos");
                        JSONArray a2 = new JSONArray(s3);
                        for (int i2 = 0; i2 < a2.length(); i2++) {
                            String ss = a2.getString(i2);
                            JSONObject j5 = new JSONObject(ss);
                            photoURL.add(j5.getString("picture"));
                        }
                        picture.put(j3.getString("name"), photoURL);
//                        Log.d(tag,"url: "+photoURL.toString());

                    }

                }
                Log.d(tag, "picture arraylist: " + picture.toString());
                Log.d(tag, albumName.toString());


            }


            if (j.getString("posts") != null) {
                String pp = j.getString("posts");
                JSONArray p1 = new JSONArray(pp);
                for (int i = 0; i < p1.length(); i++) {
                    String sp = p1.getString(i);
                    JSONObject pb = new JSONObject(sp);
                    if (pb.getString("message") != null) {
                        String message = pb.getString("message");
                        String sp2 = pb.getString("created_time");
                        JSONObject pb2 = new JSONObject(sp2);
                        String time = pb2.getString("date");
                        postContent.put(time, message);
                        times.add(time);
                    }

                }
                Log.d(tag, "post: " + postContent.toString());
            }

            shareDetail.getData(albumName, picture, userName, iconUrl, postContent,times,userID);

        } catch (Exception e) {
            Log.d(tag, "json" + e);
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.detailToolbar);
        setSupportActionBar(toolbar);
// add back arrow
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.detailContainer);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.detailTabs);
        tabLayout.setupWithViewPager(mViewPager);
        setupTabLayout();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            callbackManager = CallbackManager.Factory.create();
            shareDialog = new ShareDialog(this);

            shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {

                @Override
                public void onSuccess(Sharer.Result result){
                    Toast.makeText(getApplicationContext(), "Sharing "+userName2+"!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancel(){
                    Toast.makeText(getApplicationContext(), "Not share", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(FacebookException error){
                    Toast.makeText(getApplicationContext(), "Not share", Toast.LENGTH_SHORT).show();
                }



            });

            if (ShareDialog.canShow(ShareLinkContent.class)) {
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentTitle(userName2)
                        .setContentDescription("FB SEARCH FROM USC CSCI571")
                        .setContentUrl(Uri.parse("https://www.facebook.com/" + userID2))
//                        .setImageUrl(Uri.parse(iconUrl2))
                        .build();
                shareDialog.show(linkContent);
            }





            return true;
        }

        if (id==R.id.action_settings2) {
            Toast.makeText(getApplicationContext(), "Added to Favorites", Toast.LENGTH_SHORT).show();
        }



        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
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
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                if(albumName2.size()!=0&&picture2.size()!=0) {
                    View rootView = inflater.inflate(R.layout.fragment_details, container, false);
                    ExpandableListView ex = (ExpandableListView) rootView.findViewById(R.id.ex1);
                    ExpandableListAdapter ada = new ExpandableListAdapter(getContext(), albumName2, picture2);
                    ex.setAdapter(ada);
                    return rootView;
                }else{
                    View rootView = inflater.inflate(R.layout.fragment_details_empty, container, false);
                    return rootView;
                }

            } else {
                if(times2.size()!=0){
                    View rootView = inflater.inflate(R.layout.fragment_details2, container, false);
                    ListView poList = (ListView) rootView.findViewById(R.id.postListView);
                    postAdapter pAda = new postAdapter(getContext(),times2);
                    poList.setAdapter(pAda);
                    return rootView;
                }else{
                    View rootView = inflater.inflate(R.layout.fragment_details2_empty, container, false);
                    return rootView;
                }

            }


        }
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
            return PlaceholderFragment.newInstance(position + 1);
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


}









