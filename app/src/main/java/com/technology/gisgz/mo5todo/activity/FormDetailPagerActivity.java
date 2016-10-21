package com.technology.gisgz.mo5todo.activity;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.TextView;
import android.widget.Toast;

import com.technology.gisgz.mo5todo.R;
import com.technology.gisgz.mo5todo.adapter.FormDetailPagerAdapter;
import com.technology.gisgz.mo5todo.dialog.ActionDialogFragment;
import com.technology.gisgz.mo5todo.fragment.FormDetailFragment;
import com.technology.gisgz.mo5todo.fragment.FormRoutingFragment;
import com.technology.gisgz.mo5todo.model.ApprovalActionPOJO;
import com.technology.gisgz.mo5todo.model.FormDetailPOJO;
import com.technology.gisgz.mo5todo.model.FormRoutingPOJO;
import com.technology.gisgz.mo5todo.model.PostParameterPOJO;
import com.technology.gisgz.mo5todo.utils.MyConfig;
import com.technology.gisgz.mo5todo.utils.MyHttpService;
import com.technology.gisgz.mo5todo.viewpager.CustomViewPager;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.POST;

public class FormDetailPagerActivity extends MyBaseActivity implements ActionDialogFragment.IFormActionListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private FormDetailPagerAdapter formDetailPagerAdapter;
    private FloatingActionButton fab;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private CustomViewPager mViewPager;
    private Toolbar toolbar;
    public FormDetailPOJO formDetailPOJO = new FormDetailPOJO();
    public FormRoutingPOJO formRoutingPOJO = new FormRoutingPOJO();
    private int formId;
    private Boolean isApprovedOrRejected = false;
    private Map<String,Boolean> callingFlagMap = new HashMap();
    private ApprovalActionPOJO approvalActionPOJO;
    private boolean isGetActionStatusFailed = true;
    private FormDetailFragment formDetailFragment;
    private FormRoutingFragment formRoutingFragment;
    private boolean isHidenEmpty = false;
    private String formType;
    private String updatedAtForProgram;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pager);
        init();
        handleFAB();
        //getFormDetailTest();
        handelPager();
        getForm();
        getRouting();
        getActionStatus();
        Log.i(MyConfig.TAG, "FormDetailPager onCreate called.");


    }



    @Override
    protected void onStart() {
        super.onStart();
        Log.i(MyConfig.TAG, "FormDetailPager onStart called.");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(MyConfig.TAG, "FormDetailPager onRestart called.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(MyConfig.TAG, "FormDetailPager onResume called.");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(MyConfig.TAG, "FormDetailPager onPause called.");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(MyConfig.TAG, "FormDetailPager onStop called.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(MyConfig.TAG, "FormDetailPager onDestory called.");
    }
    @Override
    public void onFormActionClick(String comment, final int action) {
        doBeforeApiCall("formActionClick");
        Log.i("MO5Todo","onFormActionClick");
        //Toast.makeText(this,"in",Toast.LENGTH_LONG).show();
        //Snackbar.make(this.findViewById(R.id.main_content),"Call"+ action + " comment:"+ comment,Snackbar.LENGTH_LONG).show();
        MyHttpService.FormsInterfaceNew apiService =  MyHttpService.instanceRetrofit().create( MyHttpService.FormsInterfaceNew.class);
        String approvalTypeStr = "";
        String actionType = "";
        PostParameterPOJO ppm = new PostParameterPOJO();
        ppm.setComment(comment);
        ppm.setUpdatedAt(updatedAtForProgram);
        Call<ApprovalActionPOJO> call = null;

        if(action==MyConfig.ACTION_APPROVE){
//            call = apiService.actionToForm(myConfig.getUserLoginPOJO().getUserId(),formId,comment);
            actionType = "approve";
            approvalTypeStr = getString(R.string.FormApproval_approval_result);
        }else if(action==MyConfig.ACTION_REJECT){
//            call = apiService.rejectForm(myConfig.getUserLoginPOJO().getUserId(),formId,comment);
            actionType = "reject";
            approvalTypeStr = getString(R.string.FormApproval_rejcect_result);
        }else if(action==MyConfig.ACTION_UNDO){
//            call = apiService.undoForm(myConfig.getUserLoginPOJO().getUserId(),formId);
            actionType = "undo";
            approvalTypeStr = getString(R.string.FormApproval_undo_result);
            //Snackbar.make(FormDetailPagerActivity.this.findViewById(R.id.main_content),"Undo clilck.",Snackbar.LENGTH_LONG).show();
        }else if(action==MyConfig.ACTION_EXECUTE){
//            call = apiService.executeForm(myConfig.getUserLoginPOJO().getUserId(),formId);
            actionType = "execute";
            approvalTypeStr = getString(R.string.FormApproval_execute_result);
            //Snackbar.make(FormDetailPagerActivity.this.findViewById(R.id.main_content),"Undo clilck.",Snackbar.LENGTH_LONG).show();
        }
        call = apiService.actionToForm(myConfig.getUserLoginPOJO().getUserId(),formId,formType,actionType,ppm);
        Log.i(MyConfig.TAG,call.request().toString()+" url");
        Log.i(MyConfig.TAG,call.request().body().toString()+" @@body");
        final String resultMsg =  approvalTypeStr;
        call.enqueue(new Callback<ApprovalActionPOJO>() {
            @Override
            public void onResponse(Call<ApprovalActionPOJO> call, Response<ApprovalActionPOJO> response) {
                ApprovalActionPOJO pojo = response.body();
                if (response.isSuccessful()) {
                    if (pojo.getErrorMsg().getCode() == 0) {
                        approvalActionPOJO = pojo;
                        FormDetailPagerActivity.this.updatedAtForProgram = approvalActionPOJO.getUpdatedAtForProgram();
//                        handleActionStatus(action);
                        getActionStatus();
                        getRouting();
                        createApprovalAction(action);
                        Snackbar.make(FormDetailPagerActivity.this.findViewById(R.id.main_content),resultMsg + " Successfully.",Snackbar.LENGTH_LONG).show();
                    } else {

//                        Snackbar.make(FormDetailPagerActivity.this.findViewById(R.id.main_content),resultMsg + " Failed."+" "+pojo.getErrorMsg().getMsg(),Snackbar.LENGTH_LONG).show();
                        Snackbar sbar = Snackbar.make(FormDetailPagerActivity.this.findViewById(R.id.main_content),resultMsg + " Failed." + "Because this \""+" "+pojo.getErrorMsg().getMsg()+"\"",Snackbar.LENGTH_LONG);
                        ((TextView)sbar.getView().findViewById(R.id.snackbar_text)).setMaxLines(4);
                        sbar.show();
                        //sbar.
                    }
                }else{
                    Snackbar.make(FormDetailPagerActivity.this.findViewById(R.id.main_content),R.string.login_toast_mo5_service_failed,Snackbar.LENGTH_LONG).show();
                }
                doAfterApiCall("formActionClick");
            }

            @Override
            public void onFailure(Call<ApprovalActionPOJO> call, Throwable t) {
                Snackbar.make(FormDetailPagerActivity.this.findViewById(R.id.main_content),getString(R.string.login_toast_network_failed),Snackbar.LENGTH_LONG).show();
                doAfterApiCall("formActionClick");
            }
        });
    }


    private synchronized void doBeforeApiCall(String key) {
        this.callingFlagMap.put(key,Boolean.TRUE);
        showProgress(true);
        fab.setVisibility(View.GONE);
    }
    private synchronized void doAfterApiCall(String key) {
        this.callingFlagMap.put(key,Boolean.FALSE);
        if (callingFlagMap.containsValue(Boolean.TRUE)){

        }else{
            showProgress(false);
            fab.setVisibility(View.VISIBLE);
        }
    }


    public void createApprovalAction(int action){
        Log.i("MO5Todo","handleAction Produce @@@@@@@@@@@@@");
        approvalActionPOJO.setAction(action);
        approvalActionPOJO.setFormID(formId);
        myConfig.getBusInstance().post(approvalActionPOJO);
        //myConfig.getBusInstance().post(aa);
    }

    private void handelPager() {
        formDetailPagerAdapter = new FormDetailPagerAdapter(getSupportFragmentManager(),formDetailFragment,formRoutingFragment);
        mViewPager.setAdapter(formDetailPagerAdapter);
        mViewPager.setPagingEnabled(false);
    }



    @Override
    protected void init(){
        super.init();
        Intent intent=getIntent();
        formId = intent.getExtras().getInt("formId");
        formType = intent.getExtras().getString("formType");
//        isApprovedOrRejected = intent.getExtras().getBoolean("isApprovedOrRejected");
        mViewPager = (CustomViewPager) findViewById(R.id.container);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        formDetailFragment = new FormDetailFragment();
        formRoutingFragment = new FormRoutingFragment();
        initToolBar();
        // totalPage = (totalRecord + maxResult -1) / maxResult;
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        handleTab();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void handleTab() {
        TextView detail = (TextView)toolbar.findViewById(R.id.detail);
        detail.setBackgroundResource(R.drawable.border_selected);
        detail.setTextColor(getResources().getColor(R.color.colorPrimary));
        TextView routing = (TextView)toolbar.findViewById(R.id.routing);
        routing.setBackgroundResource(R.drawable.border_unselected);
        routing.setTextColor(Color.parseColor("#FFFFFFFF"));
        View.OnClickListener tabOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//            tabSelect();
                TextView selected = null;
                TextView unSelected = null;
                if(view.getId()==R.id.detail){
                    selected = (TextView)toolbar.findViewById(R.id.detail);
                    unSelected = (TextView)toolbar.findViewById(R.id.routing);
                    mViewPager.setCurrentItem(0);
                }else if(view.getId()==R.id.routing){
                    unSelected = (TextView)toolbar.findViewById(R.id.detail);
                    selected = (TextView)toolbar.findViewById(R.id.routing);
                    mViewPager.setCurrentItem(1);
                }
                tabSelect(selected,unSelected);
                //setContentView(R.layout.activity_form_routing_fragment);
                //init();
            }

            private void tabSelect(TextView selected, TextView unSelected) {
                selected.setBackgroundResource(R.drawable.border_selected);
                selected.setTextColor(getResources().getColor(R.color.colorPrimary));
                unSelected.setBackgroundResource(R.drawable.border_unselected);
                unSelected.setTextColor(Color.parseColor("#FFFFFFFF"));
            }

        };
        detail.setOnClickListener(tabOnClickListener);
        routing.setOnClickListener(tabOnClickListener);
    }

    private void handleFAB() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showActionDialog();
            }
        });
    }

    private void showActionDialog() {

        ActionDialogFragment dialog = new ActionDialogFragment();

        if(!approvalActionPOJO.isApprove() && !approvalActionPOJO.isExecute() && !approvalActionPOJO.isReject() && !approvalActionPOJO.isUndo()){
            Snackbar.make(FormDetailPagerActivity.this.findViewById(R.id.main_content),getString(R.string.FormApproval_lastlevel_result),Snackbar.LENGTH_LONG).show();
            return;
        }else{
            dialog.setIsShowButton(approvalActionPOJO.isCommentBox(),approvalActionPOJO.isApprove(), approvalActionPOJO.isReject(), approvalActionPOJO.isUndo(),approvalActionPOJO.isExecute());
        }
//
//        int actionStatus = approvalActionPOJO.getStatus();
//        switch (actionStatus){
//            case 0:{
//                Snackbar.make(FormDetailPagerActivity.this.findViewById(R.id.main_content),getString(R.string.FormApproval_lastlevel_result),Snackbar.LENGTH_LONG).show();
//                return;
//            }
//            case 1:{
//                dialog.setIsShowButton(true,true, true, false,false);
//                break;
//            }
//            case 2:{
//                dialog.setIsShowButton(false,false, false, true,false);
//                break;
//            }
//            case 3:{
//                Snackbar.make(FormDetailPagerActivity.this.findViewById(R.id.main_content),getString(R.string.FormApproval_rejectafter_result),Snackbar.LENGTH_LONG).show();
//                return;
//                //dialog.setIsShowButton(false,false, false, false);
//            }
//            case 4:{
//                dialog.setIsShowButton(true,false, true, false,true);
//                break;
//            }
//            case 5:{
//                Snackbar.make(FormDetailPagerActivity.this.findViewById(R.id.main_content),getString(R.string.FormApproval_executeafter_result),Snackbar.LENGTH_LONG).show();
//                return;
//            }
//        }
        dialog.show(getSupportFragmentManager(),"actionDialog");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_hide) {
            handleHiden();
            if(this.isHidenEmpty==true){
                item.setTitle(getString(R.string.FormDetail_showempty));
            }else{
                item.setTitle(getString(R.string.FormDetail_hideempty));
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void handleHiden() {
        isHidenEmpty = !isHidenEmpty;
        formDetailFragment.makeChange(isHidenEmpty);
    }

    private void getForm(){

        doBeforeApiCall("getForm");

        // Call<FormItemsListPOJO> getFormList(@Path("userid") int userId,@Path("page") int page,@Path("rows") int rows, @Path("myaction") String myActionFlag);

        MyHttpService.FormsInterfaceNew apiService =  MyHttpService.instanceRetrofit().create( MyHttpService.FormsInterfaceNew.class);
        Call<FormDetailPOJO> call = apiService.getForm(myConfig.getUserLoginPOJO().getUserId(),formId,formType);
        Log.i("MO5Todo",call.request().toString()+" url");
        call.enqueue(new Callback<FormDetailPOJO>() {
            @Override
            public void onResponse(Call<FormDetailPOJO> call, Response<FormDetailPOJO> response) {
                FormDetailPOJO pojo = response.body();
                if (response.isSuccessful()){
                    // login successfully, errorcode 0 means successfully
                    if(pojo.getErrorMsg().getCode()==0){
                        Log.i("MO5Todo",pojo.getErrorMsg().getCode()+" getCode");
                        //handleRecylcer();
//                        isLoading = true;
//                        tempFormList = pojo.getFormItemList();
//                        handleLoadingForm();
                        formDetailPOJO = pojo;
                        FormDetailPagerActivity.this.updatedAtForProgram = formDetailPOJO.getUpdatedAtForProgram();
                        formDetailFragment.makeChange(isHidenEmpty);
                    }else{
                        //Log.i("MO5Todo",pojo.getErrorMsg().getCode()+" getCode");
                        String alertMsg = "";
                        if(pojo.getErrorMsg().getCode()==-1237){
                            alertMsg = pojo.getErrorMsg().getMsg()+"  This form has been deleted.";
                        }else{
                            alertMsg = pojo.getErrorMsg().getMsg();
                        }
                        Toast toast = Toast.makeText(FormDetailPagerActivity.this,alertMsg,Toast.LENGTH_LONG);
                        toast.show();
                    }
                }else{
                    Toast toast = Toast.makeText(FormDetailPagerActivity.this,R.string.login_toast_mo5_service_failed,Toast.LENGTH_LONG);
                    toast.show();
                }
                doAfterApiCall("getForm");
            }
            @Override
            public void onFailure(Call<FormDetailPOJO> call, Throwable t) {
                Toast toast = Toast.makeText(FormDetailPagerActivity.this,getString(R.string.login_toast_network_failed),Toast.LENGTH_LONG);
                toast.show();
                doAfterApiCall("getForm");
            }

        });
    }

    private void getRouting() {
        doBeforeApiCall("getRouting");

        // Call<FormItemsListPOJO> getFormList(@Path("userid") int userId,@Path("page") int page,@Path("rows") int rows, @Path("myaction") String myActionFlag);

        MyHttpService.FormsInterfaceNew apiService =  MyHttpService.instanceRetrofit().create( MyHttpService.FormsInterfaceNew.class);
        Call<FormRoutingPOJO> call = apiService.getRouting(myConfig.getUserLoginPOJO().getUserId(),formId,formType);
        Log.i("MO5Todo",call.request().toString()+" url");
        call.enqueue(new Callback<FormRoutingPOJO>() {
            @Override
            public void onResponse(Call<FormRoutingPOJO> call, Response<FormRoutingPOJO> response) {
                FormRoutingPOJO pojo = response.body();
                if (response.isSuccessful()){
                    // login successfully, errorcode 0 means successfully
                    if(pojo.getErrorMsg().getCode()==0){
                        formRoutingPOJO = pojo;
                        formRoutingFragment.makeChange();
                    }else{
                        Toast toast = Toast.makeText(getApplicationContext(),R.string.FormDetail_formrouting_fail,Toast.LENGTH_LONG);
                        toast.show();
                    }
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(),R.string.login_toast_mo5_service_failed,Toast.LENGTH_LONG);
                    toast.show();
                }
                doAfterApiCall("getRouting");
            }
            @Override
            public void onFailure(Call<FormRoutingPOJO> call, Throwable t) {
                Toast toast = Toast.makeText(FormDetailPagerActivity.this,getString(R.string.login_toast_network_failed),Toast.LENGTH_LONG);
                toast.show();
                doAfterApiCall("getRouting");
            }

        });
    }

    private void getActionStatus() {
        doBeforeApiCall("getActionType");
//        fab.setClickable(false);

        // Call<FormItemsListPOJO> getFormList(@Path("userid") int userId,@Path("page") int page,@Path("rows") int rows, @Path("myaction") String myActionFlag);

        MyHttpService.FormsInterfaceNew apiService =  MyHttpService.instanceRetrofit().create( MyHttpService.FormsInterfaceNew.class);
        Call<ApprovalActionPOJO> call = apiService.getActionStatus(myConfig.getUserLoginPOJO().getUserId(),formId,formType);
        Log.i("MO5Todo",call.request().toString()+" url");
        call.enqueue(new Callback<ApprovalActionPOJO>() {
            @Override
            public void onResponse(Call<ApprovalActionPOJO> call, Response<ApprovalActionPOJO> response) {
                ApprovalActionPOJO pojo = response.body();
                if (response.isSuccessful()){
                    // login successfully, errorcode 0 means successfully
                    if(pojo.getErrorMsg().getCode()==0){
                        approvalActionPOJO = pojo;
//                        fab.setClickable(true);
                    }else{
                        Toast toast = Toast.makeText(getApplicationContext(),R.string.FormDetail_formdetail_fail,Toast.LENGTH_LONG);
                        toast.show();
                    }
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(),R.string.login_toast_mo5_service_failed,Toast.LENGTH_LONG);
                    toast.show();
                }
                doAfterApiCall("getActionType");
            }
            @Override
            public void onFailure(Call<ApprovalActionPOJO> call, Throwable t) {
                Toast toast = Toast.makeText(FormDetailPagerActivity.this,getString(R.string.login_toast_network_failed),Toast.LENGTH_LONG);
                toast.show();
                doAfterApiCall("getActionType");
            }

        });
    }



//    private void getFormDetailTest() {
//        Log.d("MO5Todo","@@@@@@@@@@@@@@@@@@@@@@@");
//        Log.i("MO5Todo","getFormDetailTest@@@@@@@");
//        FormDetailPOJO fd = new FormDetailPOJO();
//        List<FormDetailPOJO.FormRows> rowList = new ArrayList<>();
//        // normal
//        FormDetailPOJO.FormRows fr1 = new FormDetailPOJO.FormRows();
//        List<FormDetailPOJO.FormField> list1 = new ArrayList<>();
//        FormDetailPOJO.FormField formFiled_1_1= new FormDetailPOJO.FormField();
//        formFiled_1_1.setFieldName("field1");
//        formFiled_1_1.setFieldType("Label");
//        formFiled_1_1.setFieldValue("Applicant:");
//        list1.add(formFiled_1_1);
//        FormDetailPOJO.FormField formFiled_1_2= new FormDetailPOJO.FormField();
//        formFiled_1_2.setFieldName("field2");
//        formFiled_1_2.setFieldType("Select");
//        formFiled_1_2.setFieldValue("MyOffice Admin(GIS)");
//        list1.add(formFiled_1_2);
//        fr1.setFormFields(list1);
//        rowList.add(fr1);
////
//        // normal
//        FormDetailPOJO.FormRows fr2 = new FormDetailPOJO.FormRows();
//        List<FormDetailPOJO.FormField> list2 = new ArrayList<>();
//        FormDetailPOJO.FormField formFiled_2_1= new FormDetailPOJO.FormField();
//        formFiled_2_1.setFieldName("field1");
//        formFiled_2_1.setFieldType("Label");
//        formFiled_2_1.setFieldValue("Category:");
//        list2.add(formFiled_2_1);
//        FormDetailPOJO.FormField formFiled_2_2= new FormDetailPOJO.FormField();
//        formFiled_2_2.setFieldName("field2");
//        formFiled_2_2.setFieldType("Select");
//        formFiled_2_2.setFieldValue("MEBJ - Admin");
//        list2.add(formFiled_2_2);
//        fr2.setFormFields(list2);
//        rowList.add(fr2);
////
//        // Radio
//        FormDetailPOJO.FormRows fr4 = new FormDetailPOJO.FormRows();
//        List<FormDetailPOJO.FormField> list4 = new ArrayList<>();
//        FormDetailPOJO.FormField formFiled_4_1= new FormDetailPOJO.FormField();
//        formFiled_4_1.setFieldName("field1");
//        formFiled_4_1.setFieldType("Label");
//        formFiled_4_1.setFieldValue("Use Previous Form As Template :");
//        list4.add(formFiled_4_1);
//        FormDetailPOJO.FormField formFiled_4_2= new FormDetailPOJO.FormField();
//        formFiled_4_2.setFieldName("field2");
//        formFiled_4_2.setFieldType("Radio");
//        formFiled_4_2.setFieldValue("true:Yes|||false:No");
//        list4.add(formFiled_4_2);
//        fr4.setFormFields(list4);
//        rowList.add(fr4);
//
//        // normal
//        FormDetailPOJO.FormRows fr5 = new FormDetailPOJO.FormRows();
//        List<FormDetailPOJO.FormField> list5 = new ArrayList<>();
//        FormDetailPOJO.FormField formFiled_5_1= new FormDetailPOJO.FormField();
//        formFiled_5_1.setFieldName("field1");
//        formFiled_5_1.setFieldType("Label");
//        formFiled_5_1.setFieldValue("Version:");
//        list5.add(formFiled_5_1);
//        FormDetailPOJO.FormField formFiled_5_2= new FormDetailPOJO.FormField();
//        formFiled_5_2.setFieldName("field2");
//        formFiled_5_2.setFieldType("Text");
//        formFiled_5_2.setFieldValue("Version No.5 01-21-2014(Is Latest)");
//        list5.add(formFiled_5_2);
//        fr5.setFormFields(list5);
//        rowList.add(fr5);
//
//        // Radio
//        FormDetailPOJO.FormRows fr6 = new FormDetailPOJO.FormRows();
//        List<FormDetailPOJO.FormField> list6 = new ArrayList<>();
//        FormDetailPOJO.FormField formFiled_6_1= new FormDetailPOJO.FormField();
//        formFiled_6_1.setFieldName("field1");
//        formFiled_6_1.setFieldType("Label");
//        formFiled_6_1.setFieldValue("Auto Generate Form Title :");
//        list6.add(formFiled_6_1);
//        FormDetailPOJO.FormField formFiled_6_2= new FormDetailPOJO.FormField();
//        formFiled_6_2.setFieldName("field2");
//        formFiled_6_2.setFieldType("Radio");
//        formFiled_6_2.setFieldValue("true:Yes|||false:No");
//        list6.add(formFiled_6_2);
//        fr6.setFormFields(list6);
//        rowList.add(fr6);
//
//        // normal
//        FormDetailPOJO.FormRows fr7 = new FormDetailPOJO.FormRows();
//        List<FormDetailPOJO.FormField> list7 = new ArrayList<>();
//        FormDetailPOJO.FormField formFiled_7_1= new FormDetailPOJO.FormField();
//        formFiled_7_1.setFieldName("field1");
//        formFiled_7_1.setFieldType("Label");
//        formFiled_7_1.setFieldValue("Form Title:");
//        list7.add(formFiled_7_1);
//        FormDetailPOJO.FormField formFiled_7_2= new FormDetailPOJO.FormField();
//        formFiled_7_2.setFieldName("field2");
//        formFiled_7_2.setFieldType("Text");
//        formFiled_7_2.setFieldValue("MEBJ - Company Chop Application Form16ME00068");
//        list7.add(formFiled_7_2);
//        fr7.setFormFields(list7);
//        rowList.add(fr7);
//
//        // normal
//        FormDetailPOJO.FormRows fr8 = new FormDetailPOJO.FormRows();
//        List<FormDetailPOJO.FormField> list8 = new ArrayList<>();
//        FormDetailPOJO.FormField formFiled_8_1= new FormDetailPOJO.FormField();
//        formFiled_8_1.setFieldName("field1");
//        formFiled_8_1.setFieldType("Label");
//        formFiled_8_1.setFieldValue("Form Template Description:");
//        list8.add(formFiled_8_1);
//        FormDetailPOJO.FormField formFiled_8_2= new FormDetailPOJO.FormField();
//        formFiled_8_2.setFieldName("field2");
//        formFiled_8_2.setFieldType("Text");
//        formFiled_8_2.setFieldValue("");
//        list8.add(formFiled_8_2);
//        fr8.setFormFields(list8);
//        rowList.add(fr8);
//
//        // normal
//        FormDetailPOJO.FormRows fr9 = new FormDetailPOJO.FormRows();
//        List<FormDetailPOJO.FormField> list9 = new ArrayList<>();
//        FormDetailPOJO.FormField formFiled_9_1= new FormDetailPOJO.FormField();
//        formFiled_9_1.setFieldName("field1");
//        formFiled_9_1.setFieldType("Label");
//        formFiled_9_1.setFieldValue("Form Description:");
//        list9.add(formFiled_9_1);
//        FormDetailPOJO.FormField formFiled_9_2= new FormDetailPOJO.FormField();
//        formFiled_9_2.setFieldName("field2");
//        formFiled_9_2.setFieldType("Textarea");
//        formFiled_9_2.setFieldValue("这是一个测试的 form");
//        list9.add(formFiled_9_2);
//        fr9.setFormFields(list9);
//        rowList.add(fr9);
//
//        // normal
//        FormDetailPOJO.FormRows fr11 = new FormDetailPOJO.FormRows();
//        List<FormDetailPOJO.FormField> list11 = new ArrayList<>();
//        FormDetailPOJO.FormField formFiled_11_1= new FormDetailPOJO.FormField();
//        formFiled_11_1.setFieldName("field1");
//        formFiled_11_1.setFieldType("Label");
//        formFiled_11_1.setFieldValue("Discipline:");
//        list11.add(formFiled_11_1);
//        FormDetailPOJO.FormField formFiled_11_2= new FormDetailPOJO.FormField();
//        formFiled_11_2.setFieldName("field2");
//        formFiled_11_2.setFieldType("Select");
//        formFiled_11_2.setFieldValue("CN30AA050 - Finance");
//        list11.add(formFiled_11_2);
//        fr11.setFormFields(list11);
//        rowList.add(fr11);
//
//        // normal
//        FormDetailPOJO.FormRows fr12 = new FormDetailPOJO.FormRows();
//        List<FormDetailPOJO.FormField> list12 = new ArrayList<>();
//        FormDetailPOJO.FormField formFiled_12_0= new FormDetailPOJO.FormField();
//        formFiled_12_0.setFieldName("field1");
//        formFiled_12_0.setFieldType("Remark");
//        formFiled_12_0.setFieldValue("*");
//        list12.add(formFiled_12_0);
//        FormDetailPOJO.FormField formFiled_12_1= new FormDetailPOJO.FormField();
//        formFiled_12_1.setFieldName("field1");
//        formFiled_12_1.setFieldType("Label");
//        formFiled_12_1.setFieldValue("Purpose:");
//        list12.add(formFiled_12_1);
//        FormDetailPOJO.FormField formFiled_12_2= new FormDetailPOJO.FormField();
//        formFiled_12_2.setFieldName("field2");
//        formFiled_12_2.setFieldType("Textarea");
//        formFiled_12_2.setFieldValue("エキサイト翻訳の翻訳サービスは、中国語の文章を日本語へ、日本語の文章を中国語");
//        list12.add(formFiled_12_2);
//        FormDetailPOJO.FormField formFiled_12_3= new FormDetailPOJO.FormField();
//        formFiled_12_3.setFieldName("field3");
//        formFiled_12_3.setFieldType("Remark");
//        formFiled_12_3.setFieldValue("description of the document and/or name of parties involved ");
//        list12.add(formFiled_12_3);
//        fr12.setFormFields(list12);
//        rowList.add(fr12);
//
//        // normal
//        FormDetailPOJO.FormRows fr14 = new FormDetailPOJO.FormRows();
//        List<FormDetailPOJO.FormField> list14 = new ArrayList<>();
//        FormDetailPOJO.FormField formFiled_14_1= new FormDetailPOJO.FormField();
//        formFiled_14_1.setFieldName("field1");
//        formFiled_14_1.setFieldType("Label");
//        formFiled_14_1.setFieldValue("If the document is not signed, please clarify:");
//        list14.add(formFiled_14_1);
//        FormDetailPOJO.FormField formFiled_14_2= new FormDetailPOJO.FormField();
//        formFiled_14_2.setFieldName("field2");
//        formFiled_14_2.setFieldType("Textarea");
//        formFiled_14_2.setFieldValue("this is a testing field");
//        list14.add(formFiled_14_2);
//        fr14.setFormFields(list14);
//        rowList.add(fr14);
//
//        // check Box
//        FormDetailPOJO.FormRows fr13 = new FormDetailPOJO.FormRows();
//        List<FormDetailPOJO.FormField> list13 = new ArrayList<>();
//        FormDetailPOJO.FormField formFiled_13_0= new FormDetailPOJO.FormField();
//        formFiled_13_0.setFieldName("field1");
//        formFiled_13_0.setFieldType("Remark");
//        formFiled_13_0.setFieldValue("*");
//        list13.add(formFiled_13_0);
//        FormDetailPOJO.FormField formFiled_13_1= new FormDetailPOJO.FormField();
//        formFiled_13_1.setFieldName("field1");
//        formFiled_13_1.setFieldType("Label");
//        formFiled_13_1.setFieldValue("Reminder note:");
//        list13.add(formFiled_13_1);
//        FormDetailPOJO.FormField formFiled_13_2= new FormDetailPOJO.FormField();
//        formFiled_13_2.setFieldName("field2");
//        formFiled_13_2.setFieldType("Checkbox");
//        formFiled_13_2.setFieldValue("true:You need to write down the Company Chop Application Form Reference No.on your documents before chop the company stamp");
//        list13.add(formFiled_13_2);
//        fr13.setFormFields(list13);
//        rowList.add(fr13);
//
//        // pure remark
////        FormDetailPOJO.FormRows fr3 = new FormDetailPOJO.FormRows();
////        List<FormDetailPOJO.FormField> list3 = new ArrayList<>();
////        FormDetailPOJO.FormField formFiled_3_1= new FormDetailPOJO.FormField();
////        formFiled_3_1.setFieldName("field1");
////        formFiled_3_1.setFieldType("Label");
////        formFiled_3_1.setFieldValue("Supplementary medical insurance applicable :");
////        list3.add(formFiled_3_1);
////        FormDetailPOJO.FormField formFiled_3_2= new FormDetailPOJO.FormField();
////        formFiled_3_2.setFieldName("field2");
////        formFiled_3_2.setFieldType("Radio");
////        formFiled_3_2.setFieldValue("true:Yes|||false:No");
////        list3.add(formFiled_3_2);
////        FormDetailPOJO.FormField formFiled_3_3= new FormDetailPOJO.FormField();
////        formFiled_3_3.setFieldName("field3");
////        formFiled_3_3.setFieldType("Remark");
////        formFiled_3_3.setFieldValue("Legal Name 合法姓名");
////        list3.add(formFiled_3_3);
////        fr3.setFormFields(list3);
////        rowList.add(fr3);
//
//        fd.setRows(rowList);
//        formDetailPOJO = fd;
//    }
//
}
