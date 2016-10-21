package com.technology.gisgz.mo5todo.activity;


import android.widget.Toast;

import com.technology.gisgz.mo5todo.R;

/**
 * Created by Jim.Lee on 2016/4/20.
 */
public class MyHomeActivity extends MyBaseActivity {
//    TwiceBackPressLogout _tbp;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        _tbp = new TwiceBackPressLogout(this);
//    }
//
//    @Override
//    public void onBackPressed() {
//        if(_tbp.isPressSuperBack()){
//            super.onBackPressed();
//            return;
//        }else{
//             Log.i(MyConfig.TAG,"xxxxxxxxxxx");
//            return;
//            // nothing
//        }
//
//    }
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;
    @Override
    public void onBackPressed(){
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), R.string.PressBackTwice_logout, Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }

 /*   1 private boolean doubleBackToExitPressedOnce;
    2 private Handler mHandler = new Handler();
    3
            4 private final Runnable mRunnable = new Runnable() {
        5     @Override
        6     public void run() {
            7         doubleBackToExitPressedOnce = false;
            8     }
        9 };
    10
            11 @Override
    12 protected void onDestroy()
    13 {
        14     super.onDestroy();
        15
        16     if (mHandler != null) { mHandler.removeCallbacks(mRunnable); }
        17 }
    18
            19 @Override
    20 public void onBackPressed() {
        21     if (doubleBackToExitPressedOnce) {
            22         super.onBackPressed();
            23         return;
            24     }
        25
        26     this.doubleBackToExitPressedOnce = true;
        27     Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        28
        29     mHandler.postDelayed(mRunnable, 2000);
        30 }*/



}
