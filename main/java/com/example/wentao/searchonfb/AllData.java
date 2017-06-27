package com.example.wentao.searchonfb;

import java.util.ArrayList;

/**
 * Created by wentao on 4/23/17.
 */

public class AllData {
    public static ArrayList<ArrayList> dataList;

    public static void getData(ArrayList<ArrayList> args){
        dataList=args;
    }
    public static ArrayList<ArrayList> returnData(int i){
        return dataList.get(i);
    }
    public static void reset(){
        dataList=new ArrayList<ArrayList>();
    }


}
