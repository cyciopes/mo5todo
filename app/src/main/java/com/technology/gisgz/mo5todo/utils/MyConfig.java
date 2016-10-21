package com.technology.gisgz.mo5todo.utils;


import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

import com.liulishuo.filedownloader.FileDownloader;
import com.squareup.otto.Bus;
import com.technology.gisgz.mo5todo.BuildConfig;
import com.technology.gisgz.mo5todo.model.UserLoginPOJO;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jim.Lee on 2016/3/18.
 */
public class MyConfig extends Application {
    public final static String[] BASE_URL_LIST = {};
    public final static String APP_KEY = "";
    public static String BASE_URL = "";
    public final static String Key = "";

    public final static String MyAction_Flag = "MyAction";
    public final static String ActionHistory_Flag = "ActionHistory";



    public static int Default_Page_Rows = 30;
    public static final int NORMAL_ROW = 1;
    public static final int RADIO_ROW = 2;
    public static final int CHECKBOX_ROW = 3;
    public static final int PUREMARK_ROW = 4;
    public static final int MORETHAN2_ROW = 5;
    public static final int LAST_BLANK_ROW = 6;
    public static final int DATAGRID_ROW = 7;
    public static final int LEAVE_YEARDATES_ROW = 8;
    public static final int ATTACHMENT_ROW = 9;
    public static final int RTF_ROW = 10;
    public static final int CONTROL_TYPE_TEXT = 3;
    public static final int CONTROL_TYPE_TEXTAREA = 10;
    public static final int CONTROL_TYPE_RADIO = 2;
    public static final int CONTROL_TYPE_CHECKBOX = 14;
    public static final int CONTROL_TYPE_CHECKBOX_2 = 1;
    public static final int CONTROL_TYPE_SELECT = 4;
    public static final int CONTROL_TYPE_SELECTWITHSEARCH = 20;
    public static final int CONTROL_TYPE_REMARK = 11;
    public static final int CONTROL_TYPE_CALENDAR = 5;
    public static final int CONTROL_TYPE_TIME = 18;
    public static final int CONTROL_TYPE_DATAGRID = 12;
    public static final int CONTROL_TYPE_LEAVE_YEARDATES = 80;
    public static final int CONTROL_TYPE_RTFTEXT = 8;
    public static final int CONTROL_TYPE_JOBLIST = 17;
    public static final String TAG = "MO5Todo";

    private static final  Bus bus = new Bus();
    public static final int ACTION_APPROVE=1;
    public static final int ACTION_REJECT=2;
    public static final int ACTION_UNDO=3;
    public static final int ACTION_EXECUTE = 4;
    public static final int FILE_TYPE_APK = 1;



    public Bus getBusInstance(){
        return bus;
    }

    private Map<String,Integer> formCountsMap = new HashMap<>();

    public void setFormCounts(String flag,int counts){
        formCountsMap.put(flag,new Integer(counts));
    }
    public int getFormCounts(String flag){
        return formCountsMap.get(flag).intValue();
    }

    public UserLoginPOJO getUserLoginPOJO() {
        return userLoginPOJO;
    }

    public void setUserLoginPOJO(UserLoginPOJO userLoginPOJO) {
        this.userLoginPOJO = userLoginPOJO;
    }

    private UserLoginPOJO userLoginPOJO;


    @Override
    public void onCreate(){
        super.onCreate();
        //init
        init();

    }

    private void init() {
        MyConfig.BASE_URL = MyConfig.BASE_URL_LIST[0];
        FileDownloader.init(getApplicationContext());
    }


    public static String  RTF_TEMP= "";
}
