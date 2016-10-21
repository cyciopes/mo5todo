package com.technology.gisgz.mo5todo.utils.common;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.technology.gisgz.mo5todo.R;

/**
 * Created by Jim.Lee on 2016/4/20.
 */
public class TwiceBackPressLogout {
    private final static int MINIMUM_DISPLAY_TIME = Toast.LENGTH_LONG * 1000;
    boolean _isBacking = false;
    Activity _activity;
    Toast _toast;
    public TwiceBackPressLogout(Activity activity){
        _activity = activity;

    }
    public boolean isPressSuperBack(){
        AsyncTask _asyncTask = new AsyncTask<Void, Void, Void>()  {
            @Override
            protected Void doInBackground(Void... param) {
                long startTime = System.currentTimeMillis();
                long loadingTime = System.currentTimeMillis() - startTime;
                if (loadingTime < MINIMUM_DISPLAY_TIME) {
                    try {
                        Thread.sleep(MINIMUM_DISPLAY_TIME - loadingTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("MO5Todo","IN doInBackground");
                return null;
            }


            @Override
            protected void onPostExecute(Void v) {
                super.onPostExecute(v);
                if(_toast != null){
                    _toast.cancel();
                }
                _isBacking = false;
                Log.i("MO5Todo","IN onPostExecute");
            }
        };
        if(_isBacking){
            if(_toast!=null){
                _toast.cancel();
            }
            _asyncTask.cancel(true);
            Log.i("MO5Todo","IN _isBacking");
            return true;
        }else{
            _isBacking = true;
            if(_toast==null){
                _toast = Toast.makeText(_activity, R.string.PressBackTwice_logout, Toast.LENGTH_LONG);
            }
            _toast.show();
            _asyncTask.execute();
            Log.i("MO5Todo","IN no_isBacking");
            return false;
        }
    }
}
