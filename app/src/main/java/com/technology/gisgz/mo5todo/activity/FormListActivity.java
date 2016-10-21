package com.technology.gisgz.mo5todo.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;
import com.technology.gisgz.mo5todo.R;
import com.technology.gisgz.mo5todo.adapter.FormListAdapter;
import com.technology.gisgz.mo5todo.dialog.SortingDialogFrament;
import com.technology.gisgz.mo5todo.model.ApprovalActionPOJO;
import com.technology.gisgz.mo5todo.model.FormItemsListPOJO;
import com.technology.gisgz.mo5todo.model.FormItemsListPOJO.FormItem;
import com.technology.gisgz.mo5todo.model.PostParameterPOJO;
import com.technology.gisgz.mo5todo.utils.MyConfig;
import com.technology.gisgz.mo5todo.utils.MyHttpService;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormListActivity extends MyBaseActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private List<FormItem> formList = new ArrayList<>();;
    private List<FormItem> tempFormList = new ArrayList<>();;
    private RecyclerView recyclerView;
    private FormListAdapter adapter;
    private int page = 0;
    private int max_page;
    boolean isLoading = false;
    private Handler handler = new Handler();
    public boolean isActionHistory;
    public boolean isMyAction;
    private Toolbar toolbar;
    private PostParameterPOJO postParameterPOJO =  PostParameterPOJO.BlANK_PARAMETER;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(MyConfig.TAG, "onCreated formlist called.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formlist);
        init();
        myConfig.getBusInstance().register(this);
        handleRecylcer();
        // core logic
        getFormList();
        //getFormListTest();
        //handleRecylcer();
    }



    @Override
    protected void onStart() {
        super.onStart();
        Log.i(MyConfig.TAG, "onStart formlistcalled.");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(MyConfig.TAG, "onRestart formlistcalled.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(MyConfig.TAG, "onResumeformlist called.");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(MyConfig.TAG, "onPause formlistcalled.");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(MyConfig.TAG, "onStop formlistcalled.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myConfig.getBusInstance().unregister(this);
        Log.i(MyConfig.TAG, "onDestory formlistcalled.");
    }
    @Override
    protected void init(){
        super.init();

        recyclerView= (RecyclerView) findViewById(R.id.formlist_recyclerview);
        // totalPage = (totalRecord + maxResult -1) / maxResult;
        max_page = (myConfig.getFormCounts(MyConfig.MyAction_Flag) + MyConfig.Default_Page_Rows - 1) / MyConfig.Default_Page_Rows;
        Intent intent=getIntent();
        isActionHistory = intent.getExtras().getBoolean("ActionHistory");
        isMyAction = intent.getExtras().getBoolean("MyAction");
        initToolBar();
    }
    private void initToolBar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(isMyAction?R.string.MyAction_label:R.string.MyActionHaveDone_label);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bindSortBtn();
    }

    private void bindSortBtn() {
        View sortBtn  = toolbar.findViewById(R.id.icon);
        sortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SortingDialogFrament dialog = new SortingDialogFrament(postParameterPOJO);
                dialog.show(getSupportFragmentManager(),"sortingDialog");
            }
        });
    }

    @Subscribe
    public void handleAction(ApprovalActionPOJO aa){
        Log.i("MO5Todo","handleAction Subscribe @@@@@@@@@@@@@");
        adapter.makeChange(aa.getFormID());
        //Todo
//        if(aa.getAction()==1 && aa.getFormId()==6){
//            FormItem fi = formList.get(0);
//            fi.setApprovalOrReject(true);
//            fi.setCreatedBy("Otto Testing");
//            adapter.makeChange(aa.getFormId());
//        }
    }
    @Subscribe
    public void handleSorting(PostParameterPOJO ppp){
        this.postParameterPOJO = ppp;
        formList.clear();
        adapter.notifyDataSetChanged();
        page=0;
        getFormList();
    }


    private void handleRecylcer(){
        final LinearLayoutManager layoutManager = new LinearLayoutManager(FormListActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        if(formList == null){formList = new ArrayList();}
        adapter = new FormListAdapter(formList,FormListActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastVisibleItemPosition;
                lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
//                Log.i("MO5Todo", "StateChanged = " + newState);
//                Log.i("MO5Todo", "lastVisibleItemPosition = " + lastVisibleItemPosition);
//                Log.i("MO5Todo", "SCROLL_STATE_IDLE = " + RecyclerView.SCROLL_STATE_IDLE);

                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition == adapter.getItemCount()-1) {
//                    Log.i("MO5Todo", "loading executed");
                    if(FormListActivity.this.page +1 > FormListActivity.this.max_page){
                        return;
                    }
                    if (!isLoading) {
                        isLoading = true;
                        getFormList();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                Log.i("MO5Todo", "onScrolled");
                int lastVisibleItemPosition;
                lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

            }
        });


    }


    private void handleFormList(){
        if(tempFormList != null && tempFormList.size()!=0){
            if(formList == null) { formList = new ArrayList<>();}
            formList.addAll(tempFormList);
        }
    }

    private void handleLoadingForm() {
        int start = formList.size();
        handleFormList();
        adapter.notifyItemRangeInserted(formList.size(),tempFormList.size());
        FormListActivity.this.page++;
        isLoading = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tabed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getFormList(){

        showProgress(true);

        // Call<FormItemsListPOJO> getFormList(@Path("userid") int userId,@Path("page") int page,@Path("rows") int rows, @Path("myaction") String myActionFlag);

        MyHttpService.FormsInterfaceNew apiService =  MyHttpService.instanceRetrofit().create( MyHttpService.FormsInterfaceNew.class);
        Call<FormItemsListPOJO> call = null;
//        if(isMyAction){
//            call = apiService.getFormList(myConfig.getUserLoginPOJO().getUserId(),FormListActivity.this.page +1 , MyConfig.Default_Page_Rows,MyConfig.MyAction_Flag, PostParameterPOJO.BlANK_PARAMETER);
//        }else if(isActionHistory){
//            call = apiService.getActionHistoryFormList(myConfig.getUserLoginPOJO().getUserId(),FormListActivity.this.page +1 , MyConfig.Default_Page_Rows);
//        }
        call = apiService.getFormList(myConfig.getUserLoginPOJO().getUserId(),FormListActivity.this.page +1 ,
                MyConfig.Default_Page_Rows,isMyAction?MyConfig.MyAction_Flag:MyConfig.ActionHistory_Flag, postParameterPOJO);
       // Call<FormItemsListPOJO> call = apiService.getFormList(myConfig.getUserLoginPOJO().getUserId(),FormListActivity.this.page +1 , MyConfig.Default_Page_Rows,MyConfig.MyAction_Flag);
        Log.i("MO5Todo",call.request().toString()+" url");
        call.enqueue(new Callback<FormItemsListPOJO>() {
            @Override
            public void onResponse(Call<FormItemsListPOJO> call, Response<FormItemsListPOJO> response) {
                Log.i("MO5Todo",call.request().headers().toString()+"aaa");
                Log.i("MO5Todo",call.request().toString());
                FormItemsListPOJO pojo = response.body();
                if (response.isSuccessful()){
                    // login successfully, errorcode 0 means successfully
                    if(pojo.getErrorMsg().getCode()==0){
                        //handleRecylcer();
                        isLoading = true;
                        if(pojo.getFormItemList()==null){
                            tempFormList = new ArrayList<FormItem>();
                        }else {
                            tempFormList = pojo.getFormItemList();
                        }
                        handleLoadingForm();
                    }else{
                        Toast toast = Toast.makeText(getApplicationContext(),R.string.MyAction_formCounts_fail,Toast.LENGTH_LONG);
                        toast.show();
                    }
                }else{
                    Log.i("MO5Todo","ccccccc");
                    Toast toast = Toast.makeText(getApplicationContext(),R.string.login_toast_mo5_service_failed,Toast.LENGTH_LONG);
                    toast.show();
                }
                doAfterGetFormList();
            }
            @Override
            public void onFailure(Call<FormItemsListPOJO> call, Throwable t) {
                Toast toast = Toast.makeText(FormListActivity.this,getString(R.string.login_toast_network_failed),Toast.LENGTH_LONG);
                toast.show();
                doAfterGetFormList();
            }

        });
    }

    private void doAfterGetFormList() {
        isLoading = false;
        showProgress(false);
    }


    private void getFormListTest(){
        List<FormItemsListPOJO.FormItem> list = new ArrayList();
//        FormItemsListPOJO.FormItem a = new FormItemsListPOJO().new FormItem();
//        FormItemsListPOJO.FormItem b = new FormItemsListPOJO().new FormItem();
//        FormItemsListPOJO.FormItem c = new FormItemsListPOJO().new FormItem();
//        FormItemsListPOJO.FormItem d = new FormItemsListPOJO().new FormItem();
//        a.setFormName("FormTitle1 xxxxxxxxxxxxxxxxxxxxxx xxxxxxxx");
//        b.setFormName("FormTitle2 xxxxxxxxxxxxxxxxxxxxxx xxxxxxxx");
//        c.setFormName("FormTitle3 xxxxxxxxxxxxxxxxxxxxxx xxxxxxxx");
//        d.setFormName("FormTitle4 xxxxxxxxxxxxxxxxxxxxxx xxxxxxxx");
//        a.setFormDescription("FormDesc1 xxxxxxxxxxxxxxxxxxxxxx xxxxxxxx");
//        b.setFormDescription("FormDesc2 xxxxxxxxxxxxxxxxxxxxxx xxxxxxxx");
//        c.setFormDescription("FormDesc3 xxxxxxxxxxxxxxxxxxxxxx xxxxxxxx");
//        d.setFormDescription("FormDesc4 xxxxxxxxxxxxxxxxxxxxxx xxxxxxxx");
//        list.add(a);
//        list.add(b);
//        list.add(c);
//        list.add(d);
        for(int i=0;i<50;i++){
            FormItemsListPOJO.FormItem a = new FormItem();
            a.setFormName("FormTitle"+i+" xxxxxxxxxxxxxxxxxxxxxx xxxxxxxx");
            a.setFormDescription("FormDesc 中文测试\"+i+\" xxxxxxxxxxxxxxxxxxxxxx xxxxxxxx");
            a.setCreatedBy("gis admin mmm");
            a.setCreatedAt("2016-05-08 21:55:55");
            list.add(a);
        }
        this.formList = list;
        //handleRecylcer(this.formList);
    }

    public void goToTop(View view){
        Toast.makeText(this,"gototop",Toast.LENGTH_SHORT).show();
    }
}
