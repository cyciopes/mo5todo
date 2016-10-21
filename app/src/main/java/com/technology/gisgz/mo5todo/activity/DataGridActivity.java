package com.technology.gisgz.mo5todo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.inqbarna.tablefixheaders.TableFixHeaders;
import com.technology.gisgz.mo5todo.R;
import com.technology.gisgz.mo5todo.adapter.MatrixTableAdapter;
import com.technology.gisgz.mo5todo.model.FormDetailDataGirdPOJO;
import com.technology.gisgz.mo5todo.utils.MyConfig;
import com.technology.gisgz.mo5todo.utils.MyHttpService;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataGridActivity extends MyBaseActivity {

    private int formId;
    private int formTemplateDetailId;
    private List<String> summaryList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_grid);
        init();
        getDataGrid();

    }

    private void handleSummary(FormDetailDataGirdPOJO pojo) {
        if(summaryList.size()==0){
            findViewById(R.id.summarylabel).setVisibility(View.GONE);
            findViewById(R.id.summarysection).setVisibility(View.GONE);
            ((LinearLayout.LayoutParams)findViewById(R.id.summarysection).getLayoutParams()).weight = 0;
            ((LinearLayout.LayoutParams)findViewById(R.id.tablecontainer).getLayoutParams()).weight = 100;
            return;
        }

        float grandTotal = 0;
        DecimalFormat df = new DecimalFormat("###,##0.00");
        ViewGroup container = (ViewGroup)findViewById(R.id.summarycontainer);
        for (String summaryStr :
                summaryList) {
            String[] summaryArr = summaryStr.split("\\|\\|\\|");

            try {
                grandTotal+=df.parse(summaryArr[4]).floatValue();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ViewGroup summaryRow = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.formdetail_datagrid_summary_view, container,false);
            TextView ratelabel = (TextView)summaryRow.findViewById(R.id.ratelabel);
            ratelabel.setText(String.format(ratelabel.getText().toString(),summaryArr[1],summaryArr[0]));
            TextView rateValue = (TextView)summaryRow.findViewById(R.id.ratevalue);
            rateValue.setText(summaryArr[2]);
            TextView currencyFromLabel = (TextView)summaryRow.findViewById(R.id.currencyfromlabel);
            currencyFromLabel.setText(String.format(currencyFromLabel.getText().toString(),summaryArr[1]));
            TextView currencyFromValue = (TextView)summaryRow.findViewById(R.id.currencyfromvalue);
            currencyFromValue.setText(summaryArr[3]);
            TextView currencyToLabel = (TextView)summaryRow.findViewById(R.id.currencytolabel);
            currencyToLabel.setText(String.format(currencyToLabel.getText().toString(),summaryArr[1]));
            TextView currencytoValue = (TextView)summaryRow.findViewById(R.id.currencytovalue);
            currencytoValue.setText(summaryArr[4]);
            container.addView(summaryRow,container.indexOfChild(container.findViewById(R.id.grandrow)));
        }
        TextView grandValue = (TextView)container.findViewById(R.id.grandtotal);
        grandValue.setText(df.format(grandTotal));
    }

    private void showTable(FormDetailDataGirdPOJO pojo){
        TableFixHeaders tableFixHeaders = (TableFixHeaders) findViewById(R.id.table);
        MatrixTableAdapter<String> matrixTableAdapter = new MatrixTableAdapter<String>(this, transferToArray(pojo.getDataGridTable()));
//        String[][] a = transferToArray(pojo.getDataGridTable());
//        for (String[] aa:
//             a) {
//            for (String b:
//                 aa) {
//                Log.i(MyConfig.TAG,b+" #####");
//            }
//        }
//        Log.i(MyConfig.TAG,transferToArray(pojo.getDataGridTable()).);

        tableFixHeaders.setAdapter(matrixTableAdapter);
    }

    private String[][] transferToArray(List<List<String>> dataGridTable) {
        int length = 0;
        for (int i=0;i<dataGridTable.size();i++){
            if(dataGridTable.get(i).get(0).equalsIgnoreCase("9999")) {
                for (int j=1;j<dataGridTable.get(i).size();j++){
                    if(!TextUtils.isEmpty(dataGridTable.get(i).get(j))){
                        length++;
                        break;
                    }
                }
            }else if(!dataGridTable.get(i).get(0).equalsIgnoreCase("10000")) {
                length++;
            }else{
                summaryList.add(dataGridTable.get(i).get(1));
            }

            for(int j=0;j<dataGridTable.get(i).size();j++){
                dataGridTable.get(i).set(j,dataGridTable.get(i).get(j).replace("<br/>","\n"));
            }
        }

        String[][] resultArr = new String[length][dataGridTable.get(0).size()];
        for (int i=0;i<resultArr.length;i++){
                resultArr[i] = dataGridTable.get(i).toArray(new String[resultArr[i].length]);
        }
        return resultArr;
    }


    @Override
    protected void init(){
        super.init();

        Intent intent=getIntent();
        formId = intent.getExtras().getInt("formId");
        formTemplateDetailId = intent.getExtras().getInt("formTemplateDetailId");

        initToolBar();
    }
    private void initToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Data Grid");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    private void getDataGrid(){

        showProgress(true);

        MyHttpService.FormsInterfaceNew apiService =  MyHttpService.instanceRetrofit().create( MyHttpService.FormsInterfaceNew.class);
        Call<FormDetailDataGirdPOJO> call = null;

        call = apiService.getFormDetailDataGird(myConfig.getUserLoginPOJO().getUserId(),formId,formTemplateDetailId);

        Log.i("MO5Todo",call.request().toString()+" url");
        call.enqueue(new Callback<FormDetailDataGirdPOJO>() {
            @Override
            public void onResponse(Call<FormDetailDataGirdPOJO> call, Response<FormDetailDataGirdPOJO> response) {
                FormDetailDataGirdPOJO pojo = response.body();
                if (response.isSuccessful()){
                    // login successfully, errorcode 0 means successfully
                    if(pojo.getErrorMsg().getCode()==0){
                        showTable(pojo);
                        handleSummary(pojo);
                    }else{
                        Toast toast = Toast.makeText(getApplicationContext(),R.string.FormDetail_formdetail_datagrid_fail,Toast.LENGTH_LONG);
                        toast.show();
                    }
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(),R.string.login_toast_mo5_service_failed,Toast.LENGTH_LONG);
                    toast.show();
                }
                showProgress(false);
            }
            @Override
            public void onFailure(Call<FormDetailDataGirdPOJO> call, Throwable t) {
                Toast toast = Toast.makeText(getApplicationContext(),getString(R.string.login_toast_network_failed),Toast.LENGTH_LONG);
                toast.show();
                showProgress(false);
            }

        });
    }
}
