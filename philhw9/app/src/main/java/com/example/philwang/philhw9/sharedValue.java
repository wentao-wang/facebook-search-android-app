package com.example.philwang.philhw9;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by philwang on 2017/4/23.
 */

public class sharedValue {
    private static ArrayList<ArrayList<Map<String, String>>> mapResult;
    private static ArrayList<Map<String, String>> pagingResult;
    private static ArrayList<Map<String,String>> album;
    private static ArrayList<Map<String,String>>post;
    private static Map<String,String> detailguy;

    public static ArrayList<ArrayList<Map<String, String>>> getlistValue()
    {
        return new ArrayList<ArrayList<Map<String, String>>> (mapResult);
    }

    public static ArrayList<Map<String, String>> getpagingValue()
    {
        return new ArrayList<Map<String, String>>(pagingResult);
    }

    public static ArrayList<Map<String, String>> getList(int type)
    {
        return new ArrayList<>(mapResult.get(type));
    }
    public static void setList(ArrayList<ArrayList<Map<String, String>>> tem)
    {
        mapResult=new ArrayList<ArrayList<Map<String, String>>>(tem);
    }

    public static void setPaging(ArrayList<Map<String, String>> tem)
    {
        pagingResult=new ArrayList<>(tem);
    }
    public static Map<String, String>getPaging(int type)
    {
       return new HashMap<>(pagingResult.get(type));
    }

    public static void setAlbum(ArrayList<Map<String,String>> a)
    {
        if(a!=null)
            album=new  ArrayList<Map<String,String>>(a);
        else
            album=null;
    }

    public  static ArrayList<Map<String,String>> getAlbum()
    {
        if(album==null)
            return null;
        else
            return new ArrayList<>(album);
    }
    public static void setPost(ArrayList<Map<String,String>> a)
    {
        if(a!=null)
            post=new ArrayList<>(a);
        else
            post=null;
    }

    public static ArrayList<Map<String,String>> getPost()
    {
        if(post!=null)
        {
            return new ArrayList<>(post);
        }
        else
        {
            return null;
        }
    }

    public static void setDetailguy( Map<String,String> a)
    {
        detailguy = new HashMap<>(a);
    }

    public static Map<String,String> getDetailguy()
    {
        return new HashMap<>(detailguy);
    }




}
