package com.example.wentao.searchonfb;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wentao on 4/25/17.
 */

public class detailData {
    public static ArrayList<String> albumName2 ;
    public static  HashMap<String, ArrayList<String>> picture2;
    public static String userName2;
    public static String iconUrl2;
    public static HashMap<String,String> postContent2;
    public static ArrayList<String> times2;
    public static String userID2;

    public static void getData(ArrayList<String> arg1,HashMap<String, ArrayList<String>> arg2,String arg3,String arg4,HashMap<String,String> arg5,ArrayList<String> arg6,String arg7){
        albumName2=arg1;
        picture2=arg2;
        userName2=arg3;
        iconUrl2=arg4;
        postContent2=arg5;
        times2=arg6;
        userID2=arg7;
    }

    public static ArrayList<String> returnData1(){
        return albumName2;
    }

    public  static  HashMap<String, ArrayList<String>> returnData2(){
        return picture2;
    }
    public static String returnData3(){
        return userName2;
    }
    public static String returnData4(){
        return iconUrl2;
    }
    public static HashMap<String,String> returnData5(){
        return postContent2;
    }
    public static ArrayList<String> returnData6(){
        return times2;
    }


}
