package com.technology.gisgz.mo5todo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.technology.gisgz.mo5todo.R;
import com.technology.gisgz.mo5todo.adapter.FormDetailAdapter;
import com.technology.gisgz.mo5todo.adapter.decoration.DividerItemDecoration;
import com.technology.gisgz.mo5todo.dialog.ActionDialogFragment;
import com.technology.gisgz.mo5todo.model.FormDetailPOJO;
import com.technology.gisgz.mo5todo.utils.MyConfig;

import java.util.ArrayList;
import java.util.List;

public class FormDetailActivity extends MyBaseActivity implements ActionDialogFragment.IFormActionListener {
    public static int ACTION_APPROVE=1;
    public static int ACTION_REJECT=2;
    public static int ACTION_UNDO=3;
    private FormDetailPOJO formDetailPOJO;
    private RecyclerView recyclerView;
    private FormDetailAdapter adapter;
    private FloatingActionButton fab;
    private int formId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_detail);
        init();
        getFormDetailTest();
        handleRecylcer();
        handleFAB();
        // core logic

        //getFormListTest();
        //handleRecylcer();
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
        dialog.show(getSupportFragmentManager(),"actionDialog");
    }


    @Override
    protected void init(){
        super.init();
        initToolBar();
        Intent intent=getIntent();
        formId = intent.getExtras().getInt("");
        recyclerView= (RecyclerView) findViewById(R.id.formdetail_recyclerview);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        // totalPage = (totalRecord + maxResult -1) / maxResult;
    }
    private void initToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        handleTab(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // handle tab

    }

    private void handleTab(final Toolbar toolbar) {
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
                }else if(view.getId()==R.id.routing){
                    unSelected = (TextView)toolbar.findViewById(R.id.detail);
                    selected = (TextView)toolbar.findViewById(R.id.routing);
                }
                tabSelect(selected,unSelected);
                setContentView(R.layout.activity_form_routing_fragment);
                init();
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


    private void handleRecylcer(){
        final LinearLayoutManager layoutManager = new LinearLayoutManager(FormDetailActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));

        adapter = new FormDetailAdapter(formDetailPOJO,FormDetailActivity.this);
        recyclerView.setAdapter(adapter);


    }

    @Override
    public void onFormActionClick(String comment, int action) {
        Log.i("MO5Todo","onFormActionClick");
        //Toast.makeText(this,"in",Toast.LENGTH_LONG).show();
        Snackbar.make(this.findViewById(R.id.main_content),"Call"+ action + " comment:"+ comment,Snackbar.LENGTH_LONG).show();
    }



    private void getFormDetailTest() {
        Log.d("MO5Todo","@@@@@@@@@@@@@@@@@@@@@@@");
        Log.i("MO5Todo","getFormDetailTest@@@@@@@");
        FormDetailPOJO fd = new FormDetailPOJO();
        List<FormDetailPOJO.FormRows> rowList = new ArrayList<>();
        // normal
        FormDetailPOJO.FormRows fr1 = new FormDetailPOJO.FormRows();
        List<FormDetailPOJO.FormField> list1 = new ArrayList<>();
        FormDetailPOJO.FormField formFiled_1_1= new FormDetailPOJO.FormField();
        formFiled_1_1.setFieldName("field1");
        formFiled_1_1.setFieldType(MyConfig.CONTROL_TYPE_TEXT);
        formFiled_1_1.setFieldValue("Applicant:");
        list1.add(formFiled_1_1);
        FormDetailPOJO.FormField formFiled_1_2= new FormDetailPOJO.FormField();
        formFiled_1_2.setFieldName("field2");
        formFiled_1_2.setFieldType(MyConfig.CONTROL_TYPE_SELECT);
        formFiled_1_2.setFieldValue("MyOffice Admin(GIS)");
        list1.add(formFiled_1_2);
        fr1.setFormFields(list1);
        rowList.add(fr1);
//
        // normal
        FormDetailPOJO.FormRows fr2 = new FormDetailPOJO.FormRows();
        List<FormDetailPOJO.FormField> list2 = new ArrayList<>();
        FormDetailPOJO.FormField formFiled_2_1= new FormDetailPOJO.FormField();
        formFiled_2_1.setFieldName("field1");
        formFiled_2_1.setFieldType(MyConfig.CONTROL_TYPE_TEXT);
        formFiled_2_1.setFieldValue("Category:");
        list2.add(formFiled_2_1);
        FormDetailPOJO.FormField formFiled_2_2= new FormDetailPOJO.FormField();
        formFiled_2_2.setFieldName("field2");
        formFiled_2_2.setFieldType(MyConfig.CONTROL_TYPE_SELECT);
        formFiled_2_2.setFieldValue("MEBJ - Admin");
        list2.add(formFiled_2_2);
        fr2.setFormFields(list2);
        rowList.add(fr2);
//
        // Radio
        FormDetailPOJO.FormRows fr4 = new FormDetailPOJO.FormRows();
        List<FormDetailPOJO.FormField> list4 = new ArrayList<>();
        FormDetailPOJO.FormField formFiled_4_1= new FormDetailPOJO.FormField();
        formFiled_4_1.setFieldName("field1");
        formFiled_4_1.setFieldType(MyConfig.CONTROL_TYPE_TEXT);
        formFiled_4_1.setFieldValue("Use Previous Form As Template :");
        list4.add(formFiled_4_1);
        FormDetailPOJO.FormField formFiled_4_2= new FormDetailPOJO.FormField();
        formFiled_4_2.setFieldName("field2");
        formFiled_4_2.setFieldType(MyConfig.CONTROL_TYPE_RADIO);
        formFiled_4_2.setFieldValue("true:Yes|||false:No");
        list4.add(formFiled_4_2);
        fr4.setFormFields(list4);
        rowList.add(fr4);

        // normal
        FormDetailPOJO.FormRows fr5 = new FormDetailPOJO.FormRows();
        List<FormDetailPOJO.FormField> list5 = new ArrayList<>();
        FormDetailPOJO.FormField formFiled_5_1= new FormDetailPOJO.FormField();
        formFiled_5_1.setFieldName("field1");
        formFiled_5_1.setFieldType(MyConfig.CONTROL_TYPE_TEXT);
        formFiled_5_1.setFieldValue("Version:");
        list5.add(formFiled_5_1);
        FormDetailPOJO.FormField formFiled_5_2= new FormDetailPOJO.FormField();
        formFiled_5_2.setFieldName("field2");
        formFiled_5_2.setFieldType(MyConfig.CONTROL_TYPE_TEXT);
        formFiled_5_2.setFieldValue("Version No.5 01-21-2014(Is Latest)");
        list5.add(formFiled_5_2);
        fr5.setFormFields(list5);
        rowList.add(fr5);

        // Radio
        FormDetailPOJO.FormRows fr6 = new FormDetailPOJO.FormRows();
        List<FormDetailPOJO.FormField> list6 = new ArrayList<>();
        FormDetailPOJO.FormField formFiled_6_1= new FormDetailPOJO.FormField();
        formFiled_6_1.setFieldName("field1");
        formFiled_6_1.setFieldType(MyConfig.CONTROL_TYPE_TEXT);
        formFiled_6_1.setFieldValue("Auto Generate Form Title :");
        list6.add(formFiled_6_1);
        FormDetailPOJO.FormField formFiled_6_2= new FormDetailPOJO.FormField();
        formFiled_6_2.setFieldName("field2");
        formFiled_6_2.setFieldType(MyConfig.CONTROL_TYPE_RADIO);
        formFiled_6_2.setFieldValue("true:Yes|||false:No");
        list6.add(formFiled_6_2);
        fr6.setFormFields(list6);
        rowList.add(fr6);

        // normal
        FormDetailPOJO.FormRows fr7 = new FormDetailPOJO.FormRows();
        List<FormDetailPOJO.FormField> list7 = new ArrayList<>();
        FormDetailPOJO.FormField formFiled_7_1= new FormDetailPOJO.FormField();
        formFiled_7_1.setFieldName("field1");
        formFiled_7_1.setFieldType(MyConfig.CONTROL_TYPE_TEXT);
        formFiled_7_1.setFieldValue("Form Title:");
        list7.add(formFiled_7_1);
        FormDetailPOJO.FormField formFiled_7_2= new FormDetailPOJO.FormField();
        formFiled_7_2.setFieldName("field2");
        formFiled_7_2.setFieldType(MyConfig.CONTROL_TYPE_TEXT);
        formFiled_7_2.setFieldValue("MEBJ - Company Chop Application Form16ME00068");
        list7.add(formFiled_7_2);
        fr7.setFormFields(list7);
        rowList.add(fr7);

        // normal
        FormDetailPOJO.FormRows fr8 = new FormDetailPOJO.FormRows();
        List<FormDetailPOJO.FormField> list8 = new ArrayList<>();
        FormDetailPOJO.FormField formFiled_8_1= new FormDetailPOJO.FormField();
        formFiled_8_1.setFieldName("field1");
        formFiled_8_1.setFieldType(MyConfig.CONTROL_TYPE_TEXT);
        formFiled_8_1.setFieldValue("Form Template Description:");
        list8.add(formFiled_8_1);
        FormDetailPOJO.FormField formFiled_8_2= new FormDetailPOJO.FormField();
        formFiled_8_2.setFieldName("field2");
        formFiled_8_2.setFieldType(MyConfig.CONTROL_TYPE_TEXT);
        formFiled_8_2.setFieldValue("");
        list8.add(formFiled_8_2);
        fr8.setFormFields(list8);
        rowList.add(fr8);

        // normal
        FormDetailPOJO.FormRows fr9 = new FormDetailPOJO.FormRows();
        List<FormDetailPOJO.FormField> list9 = new ArrayList<>();
        FormDetailPOJO.FormField formFiled_9_1= new FormDetailPOJO.FormField();
        formFiled_9_1.setFieldName("field1");
        formFiled_9_1.setFieldType(MyConfig.CONTROL_TYPE_TEXT);
        formFiled_9_1.setFieldValue("Form Description:");
        list9.add(formFiled_9_1);
        FormDetailPOJO.FormField formFiled_9_2= new FormDetailPOJO.FormField();
        formFiled_9_2.setFieldName("field2");
        formFiled_9_2.setFieldType(MyConfig.CONTROL_TYPE_TEXT);
        formFiled_9_2.setFieldValue("这是一个测试的 form");
        list9.add(formFiled_9_2);
        fr9.setFormFields(list9);
        rowList.add(fr9);

        // normal
        FormDetailPOJO.FormRows fr11 = new FormDetailPOJO.FormRows();
        List<FormDetailPOJO.FormField> list11 = new ArrayList<>();
        FormDetailPOJO.FormField formFiled_11_1= new FormDetailPOJO.FormField();
        formFiled_11_1.setFieldName("field1");
        formFiled_11_1.setFieldType(MyConfig.CONTROL_TYPE_TEXT);
        formFiled_11_1.setFieldValue("Discipline:");
        list11.add(formFiled_11_1);
        FormDetailPOJO.FormField formFiled_11_2= new FormDetailPOJO.FormField();
        formFiled_11_2.setFieldName("field2");
        formFiled_11_2.setFieldType(MyConfig.CONTROL_TYPE_SELECT);
        formFiled_11_2.setFieldValue("CN30AA050 - Finance");
        list11.add(formFiled_11_2);
        fr11.setFormFields(list11);
        rowList.add(fr11);

        // normal
        FormDetailPOJO.FormRows fr12 = new FormDetailPOJO.FormRows();
        List<FormDetailPOJO.FormField> list12 = new ArrayList<>();
        FormDetailPOJO.FormField formFiled_12_0= new FormDetailPOJO.FormField();
        formFiled_12_0.setFieldName("field1");
        formFiled_12_0.setFieldType(MyConfig.CONTROL_TYPE_REMARK);
        formFiled_12_0.setFieldValue("*");
        list12.add(formFiled_12_0);
        FormDetailPOJO.FormField formFiled_12_1= new FormDetailPOJO.FormField();
        formFiled_12_1.setFieldName("field1");
        formFiled_12_1.setFieldType(MyConfig.CONTROL_TYPE_TEXT);
        formFiled_12_1.setFieldValue("Purpose:");
        list12.add(formFiled_12_1);
        FormDetailPOJO.FormField formFiled_12_2= new FormDetailPOJO.FormField();
        formFiled_12_2.setFieldName("field2");
        formFiled_12_2.setFieldType(MyConfig.CONTROL_TYPE_TEXT);
        formFiled_12_2.setFieldValue("エキサイト翻訳の翻訳サービスは、中国語の文章を日本語へ、日本語の文章を中国語");
        list12.add(formFiled_12_2);
        FormDetailPOJO.FormField formFiled_12_3= new FormDetailPOJO.FormField();
        formFiled_12_3.setFieldName("field3");
        formFiled_12_3.setFieldType(MyConfig.CONTROL_TYPE_REMARK);
        formFiled_12_3.setFieldValue("description of the document and/or name of parties involved ");
        list12.add(formFiled_12_3);
        fr12.setFormFields(list12);
        rowList.add(fr12);

        // normal
        FormDetailPOJO.FormRows fr14 = new FormDetailPOJO.FormRows();
        List<FormDetailPOJO.FormField> list14 = new ArrayList<>();
        FormDetailPOJO.FormField formFiled_14_1= new FormDetailPOJO.FormField();
        formFiled_14_1.setFieldName("field1");
        formFiled_14_1.setFieldType(MyConfig.CONTROL_TYPE_TEXT);
        formFiled_14_1.setFieldValue("If the document is not signed, please clarify:");
        list14.add(formFiled_14_1);
        FormDetailPOJO.FormField formFiled_14_2= new FormDetailPOJO.FormField();
        formFiled_14_2.setFieldName("field2");
        formFiled_14_2.setFieldType(MyConfig.CONTROL_TYPE_TEXT);
        formFiled_14_2.setFieldValue("this is a testing field");
        list14.add(formFiled_14_2);
        fr14.setFormFields(list14);
        rowList.add(fr14);

        // check Box
        FormDetailPOJO.FormRows fr13 = new FormDetailPOJO.FormRows();
        List<FormDetailPOJO.FormField> list13 = new ArrayList<>();
        FormDetailPOJO.FormField formFiled_13_0= new FormDetailPOJO.FormField();
        formFiled_13_0.setFieldName("field1");
        formFiled_13_0.setFieldType(MyConfig.CONTROL_TYPE_REMARK);
        formFiled_13_0.setFieldValue("*");
        list13.add(formFiled_13_0);
        FormDetailPOJO.FormField formFiled_13_1= new FormDetailPOJO.FormField();
        formFiled_13_1.setFieldName("field1");
        formFiled_13_1.setFieldType(MyConfig.CONTROL_TYPE_TEXT);
        formFiled_13_1.setFieldValue("Reminder note:");
        list13.add(formFiled_13_1);
        FormDetailPOJO.FormField formFiled_13_2= new FormDetailPOJO.FormField();
        formFiled_13_2.setFieldName("field2");
        formFiled_13_2.setFieldType(MyConfig.CONTROL_TYPE_CHECKBOX);
        formFiled_13_2.setFieldValue("true:You need to write down the Company Chop Application Form Reference No.on your documents before chop the company stamp");
        list13.add(formFiled_13_2);
        fr13.setFormFields(list13);
        rowList.add(fr13);

        // pure remark
//        FormDetailPOJO.FormRows fr3 = new FormDetailPOJO.FormRows();
//        List<FormDetailPOJO.FormField> list3 = new ArrayList<>();
//        FormDetailPOJO.FormField formFiled_3_1= new FormDetailPOJO.FormField();
//        formFiled_3_1.setFieldName("field1");
//        formFiled_3_1.setFieldType("Label");
//        formFiled_3_1.setFieldValue("Supplementary medical insurance applicable :");
//        list3.add(formFiled_3_1);
//        FormDetailPOJO.FormField formFiled_3_2= new FormDetailPOJO.FormField();
//        formFiled_3_2.setFieldName("field2");
//        formFiled_3_2.setFieldType("Radio");
//        formFiled_3_2.setFieldValue("true:Yes|||false:No");
//        list3.add(formFiled_3_2);
//        FormDetailPOJO.FormField formFiled_3_3= new FormDetailPOJO.FormField();
//        formFiled_3_3.setFieldName("field3");
//        formFiled_3_3.setFieldType("Remark");
//        formFiled_3_3.setFieldValue("Legal Name 合法姓名");
//        list3.add(formFiled_3_3);
//        fr3.setFormFields(list3);
//        rowList.add(fr3);

        fd.setRows(rowList);
        this.formDetailPOJO = fd;
    }
}
