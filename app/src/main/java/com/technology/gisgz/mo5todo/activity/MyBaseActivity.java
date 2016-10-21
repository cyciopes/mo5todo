package com.technology.gisgz.mo5todo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.technology.gisgz.mo5todo.R;
import com.technology.gisgz.mo5todo.utils.MyConfig;

/**
 * Created by Jim.Lee on 2016/3/24.
 */
public class MyBaseActivity extends AppCompatActivity {
    protected View vHelpProgressText;
    protected View vProgress;
    public MyConfig myConfig;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    protected void init() {
        myConfig = (MyConfig)getApplication();
        vHelpProgressText = (View)findViewById(R.id.login_helpprogress_text);
        vProgress = (View)findViewById(R.id.login_progress);
    }
    public void onRemoveClick(View dd){
        // do nothing, avoid user click when loding
    }
    protected void showProgress(boolean isShow) {

        if (isShow) {
            vHelpProgressText.setVisibility(View.VISIBLE);
            vProgress.setVisibility(View.VISIBLE);
        }else{
            vHelpProgressText.setVisibility(View.GONE);
            vProgress.setVisibility(View.GONE);
        }
    }
}
