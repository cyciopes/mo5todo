package com.technology.gisgz.mo5todo.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.technology.gisgz.mo5todo.BuildConfig;
import com.technology.gisgz.mo5todo.R;
import com.technology.gisgz.mo5todo.model.NewVersionPOJO;
import com.technology.gisgz.mo5todo.model.UserLoginPOJO;
import com.technology.gisgz.mo5todo.utils.MyConfig;
import com.technology.gisgz.mo5todo.utils.MyHttpService;
import com.technology.gisgz.mo5todo.utils.common.FileUtil;
import com.technology.gisgz.mo5todo.utils.common.EncryptUtil;

import java.io.File;
import com.google.common.io.Files;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.*;
public class SplashActivity extends AppCompatActivity {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private ProgressDialog mPb;
    private final static int MINIMUM_DISPLAY_TIME = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handlePermission();
        try {
//            Log.i(MyConfig.TAG, EncryptUtil.aesEncrypt("admin","ijkloiuy*^%8U0i2"));
//            Log.i(MyConfig.TAG, EncryptUtil.aesDecrypt("a9Uib6usjrqg+ylMd4Crgg==","ijkloiuy*^%8U0i2"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                long startTime = System.currentTimeMillis();

                chooseBaseURL();
                int result;
                long loadingTime = System.currentTimeMillis() - startTime;
                if (loadingTime < MINIMUM_DISPLAY_TIME) {
                    try {
                        Thread.sleep(MINIMUM_DISPLAY_TIME - loadingTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                checkVersion();
//                Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
//                startActivity(intent);
//                //启动MainActivity后销毁自身
//                finish();
            }
        }.execute();
        //TODO; may be check version here

    }

    private void handlePermission() {
        Log.i(MyConfig.TAG,"handlePermission ~~");
        int permission = ActivityCompat.checkSelfPermission(this,WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(MyConfig.TAG,"permission ~~"+permission);
            // 無權限，向使用者請求
            ActivityCompat.requestPermissions(
                    this,
                    new String[] {WRITE_EXTERNAL_STORAGE,
                            READ_EXTERNAL_STORAGE},
                    REQUEST_EXTERNAL_STORAGE
            );
        }else{
            //已有權限，執行儲存程式
//            writeFile();
        }
    }

    private void checkVersion() {
        MyHttpService.CommonInterface apiService = MyHttpService.instanceRetrofit().create(MyHttpService.CommonInterface.class);
        Call<NewVersionPOJO> call = apiService.checkVersion(BuildConfig.VERSION_CODE);
        Log.i("MO5Todo", call.request().toString() + " url");
        call.enqueue(new Callback<NewVersionPOJO>() {
            @Override
            public void onResponse(Call<NewVersionPOJO> call, Response<NewVersionPOJO> response) {
                NewVersionPOJO pojo = response.body();
                if (response.isSuccessful()) {
                    if (pojo.getErrorMsg().getCode() == 0) {
                        if(pojo.getNewVersionReady() && !pojo.getRule().equalsIgnoreCase("ignore")){
                            showUpdateDialog(pojo);
                        }else {
                            goToLoginActivity();
                        }
                    } else {

//                        Snackbar.make(FormDetailPagerActivity.this.findViewById(R.id.main_content),resultMsg + " Failed." + "eCode="+pojo.getErrorMsg().getCode()+" "+pojo.getErrorMsg().getMsg(),Snackbar.LENGTH_LONG).show();
                    }
                }else{
//                    Snackbar.make(FormDetailPagerActivity.this.findViewById(R.id.main_content),R.string.login_toast_mo5_service_failed,Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<NewVersionPOJO> call, Throwable t) {

            }
        });
    }

    private void goToLoginActivity() {
        Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
        startActivity(intent);
        //启动MainActivity后销毁自身
        finish();
    }

    protected void showUpdateDialog(final NewVersionPOJO pojo) {
        //创建builder对象
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置标题
        builder.setTitle("Version Update");
        //设置对话框信息
        //builder.setMessage(pojo.getNewVersionName() +" are ready.Size is "+pojo.getApkSize()+ "M."+""+"Update now?");
        View view = LayoutInflater.from(SplashActivity.this).inflate(R.layout.version_update, null);
        ((TextView)view.findViewById(R.id.name)).setText(pojo.getNewVersionName());
        ((TextView)view.findViewById(R.id.size)).setText(pojo.getApkSize()+"M");
        ((TextView)view.findViewById(R.id.desc)).setText(pojo.getNewVersionDesc());
        builder.setView(view);
        //设置不可回退
        builder.setCancelable(false);
        //设置确定按钮
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which)  {
                //创建进度条对话框
                mPb = new ProgressDialog(SplashActivity.this);
                //设置进度条对话框不可回退
                mPb.setCancelable(false);
                //设置进度条对话框样式
                mPb.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                //设置进度条对话框的信息
                mPb.setMessage("Downloading");
                mPb.setMax(100);
                mPb.setProgressNumberFormat(String.format("%.2fM/%.2fM", 0.0, 0.0));
                //显示进度条对话框
                mPb.show();
                //开启显示进度条对话框线程
                //new Thread(new DownloadApkTask()).start();
                try {
                    downLoadApkFile(pojo.getNewVersionCode(),MyConfig.FILE_TYPE_APK);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,
                                int whichButton) {
                // 点击"取消"按钮之后退出程序
                dialog.dismiss();
                goToLoginActivity();
            }
        }).create().show();
        //创建更新信息提示对话框
//        mUpdateInfoDialog = builder.create();
        //显示更新信息提示对话框
//        mUpdateInfoDialog.show();
    }

    private void downLoadApkFile(int newVersionCode, int fileType) throws IOException {
        final File apkFile = FileUtil.createFile("apk"+File.separator+"MO5ToDo.apk");
        FileDownloader.getImpl().create(String.format(MyConfig.BASE_URL+MyHttpService.CommonInterface.getFileURL,1,newVersionCode,fileType)).setForceReDownload(true)
                .setPath(apkFile.getAbsolutePath())
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.i(MyConfig.TAG,"pending");
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                        Log.i(MyConfig.TAG,"connected");
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        mPb.setProgress((int)(((float)soFarBytes/(float)totalBytes)*100));
                        mPb.setProgressNumberFormat(String.format("%.2fM/%.2fM", soFarBytes/1024.0/1024.0, totalBytes/1024.0/1024.0));
                    }


                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                        Log.i(MyConfig.TAG,"blockComplete");
                    }

                    @Override
                    protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                        Log.i(MyConfig.TAG,"retry");
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        mPb.dismiss();
                        installApk(apkFile);
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.i(MyConfig.TAG,"paused");
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                          Log.i(MyConfig.TAG,e.getMessage());
                          e.printStackTrace();
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        Log.i(MyConfig.TAG,"warn");
                    }
                }).start();
    }

    private void installApk(File apkFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private void chooseBaseURL() {
        for (String tempURL :
                MyConfig.BASE_URL_LIST) {
            MyConfig.BASE_URL=tempURL;
            MyHttpService.setSingle(false);
            MyHttpService.UserLoginInterface apiService = MyHttpService.instanceRetrofit().create(MyHttpService.UserLoginInterface.class);
            Call<UserLoginPOJO> call = apiService.getUserLogin(0, "gis".toString().trim(), "admin".toString().trim(), "xxxxxx".toString().trim());
            Log.i("MO5Todo", call.request().toString() + " url");
            Log.i("MO5Todo", tempURL + " tempURL ");
            try {
                call.execute();
                return;
            } catch (IOException e) {
                Log.i(MyConfig.TAG,e.getMessage());
            }finally {
                if(!call.isCanceled()){call.cancel();}
            }
        }
        MyHttpService.setSingle(true);
        Toast toast = Toast.makeText(getApplicationContext(),R.string.login_toast_network_failed,Toast.LENGTH_SHORT);
        toast.show();
    }
}
