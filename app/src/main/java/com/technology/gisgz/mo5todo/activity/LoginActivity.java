package com.technology.gisgz.mo5todo.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.technology.gisgz.mo5todo.BuildConfig;
import com.technology.gisgz.mo5todo.R;
import com.technology.gisgz.mo5todo.model.UserLoginPOJO;
import com.technology.gisgz.mo5todo.utils.MyHttpService;
import com.technology.gisgz.mo5todo.utils.MyConfig;
import com.technology.gisgz.mo5todo.utils.common.EncryptUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginActivity extends MyBaseActivity {
    private EditText vCompany;
    private EditText vUserName;
    private EditText vPassword;
    private Switch vSwitch;
    private TextView vAppversion;
    private TextInputLayout vCompanyWrapper;
    private TextInputLayout vUserNameWrapper;
    private TextInputLayout vPasswordWrapper;
    private Button vLoginBtn;
    private MyConfig myConfig;
    private SharedPreferences sp;

    protected void init(){
        super.init();
        vCompany = (EditText)findViewById(R.id.login_companyname_input);
        vUserName = (EditText)findViewById(R.id.login_username_input);
        vPassword = (EditText)findViewById(R.id.login_password_input);
        vSwitch = (Switch)findViewById(R.id.login_domianlogin_switch);
        vAppversion = (TextView)findViewById(R.id.login_appversion_text);
        vCompanyWrapper = (TextInputLayout)findViewById(R.id.login_companyname_Wrapper);
        vUserNameWrapper = (TextInputLayout)findViewById(R.id.login_username_Wrapper);
        vPasswordWrapper = (TextInputLayout)findViewById(R.id.login_password_Wrapper);
        vLoginBtn = (Button)findViewById(R.id.login_btn);
        myConfig = (MyConfig)getApplication();

    }

    private void handleRememberLogins() {
        sp = super.getSharedPreferences("MO5Todo",MODE_PRIVATE);

        int isDomain = sp.getInt("isDomain",1);
        String company = sp.getString("Company","");
        String loginName = sp.getString("LoginName","");
        //String loginName = sp.getString("LoginName","");
        if(isDomain==1) vSwitch.setChecked(true); else vSwitch.setChecked(false);
        vSwitch.callOnClick();
        vCompany.setText(company);
        vUserName.setText(loginName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // init
        init();
        // set version
        vAppversion.setText(BuildConfig.VERSION_NAME);
        // handle change of domain login switch
        handleSwitch();
        // handle login btn
        handleLoginBtn();
        //
        handleRememberLogins();
    }

    private void handleLoginBtn() {
        vCompany.clearFocus();
        vUserName.clearFocus();
        vPassword.clearFocus();
        vLoginBtn.setOnClickListener(new View.OnClickListener() {
            String errorMsg;
            @Override
            public void onClick(View view) {
                if(TextUtils.equals("a",vCompany.getText())){
                    // go to main activity
                    Intent intent=new Intent(LoginActivity.this,MyActionActivity.class);
                    startActivity(intent);
                    myConfig.setUserLoginPOJO(new UserLoginPOJO());
                    //启动MainActivity后销毁自身
                    finish();
                    return;
                }
                Log.i("MO5Todo","Befroe check");
                // check input
                if(checkInput()){
                    Log.i("MO5Todo","in check");
                    // show progress
                    Log.i("MO5Todo",MyHttpService.instanceRetrofit().baseUrl().toString()+" base url");
                    showProgress(true);
                    MyHttpService.UserLoginInterface apiService =  MyHttpService.instanceRetrofit().create( MyHttpService.UserLoginInterface.class);
                    Call<UserLoginPOJO> call = null;
                    try {
                        call = apiService.getUserLogin(isDomainLogin(),vCompany.getText().toString().trim(),
                                EncryptUtil.aesEncrypt(vUserName.getText().toString().trim(),MyConfig.Key).trim(),
                                EncryptUtil.aesEncrypt(vPassword.getText().toString().trim(),MyConfig.Key).trim());
                    } catch (Exception e) {
                        call = apiService.getUserLogin(isDomainLogin(),vCompany.getText().toString().trim(),
                                (vUserName.getText().toString().trim()),
                                (vPassword.getText().toString().trim()));
                    }
                    Log.i("MO5Todo",call.request().toString()+" url");
                    call.enqueue(new Callback<UserLoginPOJO>() {
                        @Override
                        public void onResponse(Call<UserLoginPOJO> call, Response<UserLoginPOJO> response) {
                            UserLoginPOJO pojo = response.body();
                            if (response.isSuccessful()){
                                Log.i("MO5Todo",pojo.getErrorMsg().getCode()+" pojo.getErrorMsg().getCode()");
                                // login successfully, errorcode 0 means successfully
                                if(pojo.getErrorMsg().getCode()==0){
                                    // go to main activity
                                    Intent intent=new Intent(LoginActivity.this,MyActionActivity.class);
                                    startActivity(intent);
                                    myConfig.setUserLoginPOJO(pojo);
                                    //启动MainActivity后销毁自身
                                    finish();
                                }else{

                                    // 1. Instantiate an AlertDialog.Builder with its constructor
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

                                    // 2. Chain together various setter methods to set the dialog characteristics
                                    builder.setMessage(pojo.getErrorMsg().getMsg());

                                    // 3. Get the AlertDialog from create()
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                            }else{
                                Log.i("MO5Todo","ccccccc");
                                Toast toast = Toast.makeText(getApplicationContext(),R.string.login_toast_mo5_service_failed,Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            showProgress(false);
                        }
                        @Override
                        public void onFailure(Call<UserLoginPOJO> call, Throwable t) {
                            Log.i("MO5Todo","dddddddd");
                            Log.i("MO5Todo","exception~~~~~"+t);
                            Toast toast = Toast.makeText(getApplicationContext(),getString(R.string.login_toast_network_failed),Toast.LENGTH_SHORT);
                            toast.show();
                            showProgress(false);
                        }

                    });
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(),errorMsg,Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            private boolean checkInput() {
                if(TextUtils.isEmpty(vCompany.getText()) || TextUtils.isEmpty(vUserName.getText()) || TextUtils.isEmpty(vPassword.getText())){
                    errorMsg = String.format(getString(R.string.login_toast_input_cant_empty),vCompanyWrapper.getHint(),vUserNameWrapper.getHint(),vPasswordWrapper.getHint());
                    return false;
                }else{
                    return true;
                }
            }
        });
    }
    @Override
    protected void onStop(){
//        int isDomain = sp.getInt("isDomain",1);
//        String company = sp.getString("Company","");
//        String loginName = sp.getString("LoginName","");
        Log.i("MO5Todo","In onStop #############");
        super.onStop();
        SharedPreferences.Editor et = sp.edit();
        et.putInt("isDomain",isDomainLogin());
        et.putString("Company",vCompany.getText().toString().trim());
        et.putString("LoginName",vUserName.getText().toString().trim());
        et.apply();
    }


    private void handleSwitch(){
        vSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // clear all text
                vCompany.setText("");
                vUserName.setText("");
                vPassword.setText("");
                vCompany.clearFocus();
                vUserName.clearFocus();
                vPassword.clearFocus();
                //((TextView)findViewById(R.id.login_appversion_text)).getLayout();

                if(compoundButton.isChecked()){
                    vCompanyWrapper.setHint(getString(R.string.login_input_CorD_domain));
                }else{
                    vCompanyWrapper.setHint(getString(R.string.login_input_CorD_company));
                }
            }
        });

    }
    public void onRemoveClick(View dd){
        // do nothing, avoid user click when loding
    }
    private int isDomainLogin(){
        return vSwitch.isChecked()?1:0;
    }
}
