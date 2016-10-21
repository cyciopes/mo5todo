package com.technology.gisgz.mo5todo.activity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;
import com.technology.gisgz.mo5todo.R;
import com.technology.gisgz.mo5todo.model.ApprovalActionPOJO;
import com.technology.gisgz.mo5todo.model.FormCountsPOJO;
import com.technology.gisgz.mo5todo.model.PostParameterPOJO;
import com.technology.gisgz.mo5todo.utils.MyConfig;
import com.technology.gisgz.mo5todo.utils.MyHttpService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyActionActivity extends MyHomeActivity {
    TextView vMyActionCounts;


//    @Subscribe
//    public void handleTAction(ApprovalActionPOJO aa){
//        Log.i("MO5Todo","MyActionActivity Subscribe @@@@@@@@@@@@@"+vMyActionCounts.getText().toString());
//        if(aa.getAction()==MyConfig.ACTION_APPROVE ||aa.getAction()==MyConfig.ACTION_REJECT||aa.getAction()==MyConfig.ACTION_EXECUTE ) {
//            this.vMyActionCounts.setText((Integer.parseInt(vMyActionCounts.getText().toString()) - 1) + "");
//        }else if(aa.getAction()==MyConfig.ACTION_UNDO){
//            this.vMyActionCounts.setText((Integer.parseInt(vMyActionCounts.getText().toString()) + 1) + "");
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("MO5Todo","In MyAction onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_action);
        this.init();
//        myConfig.getBusInstance().register(this);

//        TextView tv = (TextView) findViewById(R.id.myaction_myactioin_value);
//        tv.setText(myConfig.getUserLoginPOJO().getUserCompany()+"\\"+myConfig.getUserLoginPOJO().getUserName());

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(MyConfig.TAG, "MyAction onStart called.");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(MyConfig.TAG, "MyAction onRestart called.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFirst();
        Log.i(MyConfig.TAG, "MyAction onResume called.");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(MyConfig.TAG, "MyAction onPause called.");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(MyConfig.TAG, "MyAction onStop called.");
    }


    @Override
    protected void onDestroy() {
        Log.i("MO5Todo","In MyAction onDestroy");
        super.onDestroy();
//        myConfig.getBusInstance().unregister(this);
    }

    @Override
    protected void init(){
        super.init();
        initToolBar();
        vMyActionCounts = (TextView) findViewById(R.id.myaction_myactioin_value);

    }
    private void initToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("MO5 ToDo");
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void loadFirst(){
        showProgress(true);
        MyHttpService.FormsInterfaceNew apiService =  MyHttpService.instanceRetrofit().create( MyHttpService.FormsInterfaceNew.class);
        Call<FormCountsPOJO> call = apiService.getFormCounts(myConfig.getUserLoginPOJO().getUserId(), MyConfig.MyAction_Flag, PostParameterPOJO.BlANK_PARAMETER);
        Log.i("MO5Todo",call.request().toString()+" url");
        Log.i("MO5Todo",call.request().body().contentType()+" contentType");
        try {
            Log.i("MO5Todo",call.request().body().contentLength()+" contentType");
            Log.i("MO5Todo",call.request().body().toString()+" contentType");
        } catch (IOException e) {
            e.printStackTrace();
        }
        call.enqueue(new Callback<FormCountsPOJO>() {
            @Override
            public void onResponse(Call<FormCountsPOJO> call, Response<FormCountsPOJO> response) {
                Log.i("MO5Todo",call.request().headers().toString()+"aaa");
                Log.i("MO5Todo",call.request().toString());
                FormCountsPOJO pojo = response.body();
                if (response.isSuccessful()){
                    // login successfully, errorcode 0 means successfully
                    if(pojo.getErrorMsg().getCode()==0){
                        myConfig.setFormCounts(MyConfig.MyAction_Flag,pojo.getFormCount());
                        vMyActionCounts.setText(Integer.toString(pojo.getFormCount()));
                    }else{
                        Toast toast = Toast.makeText(getApplicationContext(),R.string.MyAction_formCounts_fail,Toast.LENGTH_LONG);
                        toast.show();
                    }
                }else{
                    Log.i("MO5Todo","ccccccc");
                    Toast toast = Toast.makeText(getApplicationContext(),R.string.login_toast_mo5_service_failed,Toast.LENGTH_LONG);
                    toast.show();
                }
                showProgress(false);
            }
            @Override
            public void onFailure(Call<FormCountsPOJO> call, Throwable t) {
                Toast toast = Toast.makeText(MyActionActivity.this,getString(R.string.login_toast_network_failed),Toast.LENGTH_LONG);
                toast.show();
                showProgress(false);
            }

        });
    }
    public void test(View v){

    }
    public void test1(View v){
       // go to main activity
        Intent intent=new Intent(MyActionActivity.this,NavigationDrawerActivity.class);
        startActivity(intent);
        //启动MainActivity后销毁自身
        //finish();
    }

    public void test2(View v){
        final DownloadManager dManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        String dowloadPath = "http://144.210.190.84/MyOffice_API/api/v1/common/Abc/getfile/5/1";
        Uri uri = Uri.parse(dowloadPath);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setDestinationInExternalPublicDir("download", "ttttttttt.apk");
        request.setTitle("MMMMMMMM");
        request.setDescription("DOTA2资料库新版本下载");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setMimeType("application/vnd.android.package-archive");
         // 设置为可被媒体扫描器找到
        request.allowScanningByMediaScanner();
        // 设置为可见和可管理
        request.setVisibleInDownloadsUi(true);
        long refernece = dManager.enqueue(request);
    }

    public void test3(View v){
        // go to main activity
        Intent intent=new Intent(MyActionActivity.this,TestWevViewActivity.class);
        startActivity(intent);
        //启动MainActivity后销毁自身
        //finish();
    }

    public void click(View v){
        // go to main activity
        Intent intent=new Intent(MyActionActivity.this,FormListActivity.class);
        intent.putExtra("MyAction", true);
        intent.putExtra("ActionHistory", false);
        startActivity(intent);
        //启动MainActivity后销毁自身
        //finish();
    }
    public void click1(View v){
        // go to main activity
        Intent intent=new Intent(MyActionActivity.this,FormListActivity.class);
        intent.putExtra("ActionHistory", true);
        intent.putExtra("MyAction", false);
        startActivity(intent);
        //启动MainActivity后销毁自身
        //finish();
    }
}
