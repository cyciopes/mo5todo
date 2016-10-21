package com.technology.gisgz.mo5todo.adapter;

/**
 * Created by Jim.Lee on 2016/8/11.
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.inqbarna.tablefixheaders.adapters.BaseTableAdapter;
import com.technology.gisgz.mo5todo.R;
import com.technology.gisgz.mo5todo.utils.MyConfig;
import com.technology.gisgz.mo5todo.utils.common.DensityUtil;

import java.util.Arrays;

public class MatrixTableAdapter<T> extends BaseTableAdapter {

    private final static int WIDTH_DIP_SMALL = 80;
    private final static int WIDTH_DIP_MEDIUM = 110;
    private final static int WIDTH_DIP_LARGE = 150;
    private static final int WIDTH_DIP_LARGE2 = 200;
    private final static int HEIGHT_DIP = 38;
    private final static int HEIGHT_DIP_MEDIUM = 60;
    private final static int HEIGHT_DIP_LARGE = 130;
    private final static int HEIGHT_DIP_LARGE2 = 280;



    private final Context context;

    private T[][] table;

//    private final int width;
    private final int height;

    public MatrixTableAdapter(Context context) {
        this(context, null);
    }

    public MatrixTableAdapter(Context context, T[][] table) {
        this.context = context;
        Resources r = context.getResources();


        height = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, HEIGHT_DIP, r.getDisplayMetrics()));

        setInformation(table);
    }

    public void setInformation(T[][] table) {
        this.table = table;
    }

    @Override
    public int getRowCount() {
        return table.length - 1;
    }

    @Override
    public int getColumnCount() {
        return table[0].length - 1;
    }

    @Override
    public View getView(int row, int column, View convertView, ViewGroup parent) {
//        if (convertView == null) {
//            convertView = new TextView(context);
//            ((TextView) convertView).setGravity(Gravity.CENTER_VERTICAL);
//        }
        convertView = new TextView(context);
        ((TextView) convertView).setGravity(Gravity.CENTER_VERTICAL);
        ((TextView) convertView).setText(table[row + 1][column + 1].toString().trim());
        ((TextView) convertView).setTextSize(12);
//        ((TextView) convertView).setBackgroundResource(R.drawable.color_clicked);
        // header
        if(row==-1){
            convertView.setBackgroundResource(R.drawable.tableheader);
            ((TextView) convertView).setTextColor(Color.WHITE);
        }else
        // odd row
        if(row%2!=0){
            convertView.setBackgroundResource(R.drawable.tablerow_odd);
        }else
        // even row
        if(row%2==0){
            convertView.setBackgroundResource(R.drawable.tablerow_even);
        }
        if(column==0){
            convertView.setPadding(DensityUtil.dip2px(context,5),0,0,0);
        }

        return convertView;
    }

    @Override
    public int getHeight(int row) {
        if(row==-1){
            return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, HEIGHT_DIP, context.getResources().getDisplayMetrics()));
        }
        int[] lengthArr = new int[table[0].length];
//        String ss = "";
        for (int i=0;i<table[0].length;i++){
            lengthArr[i] = ((String)table[row+1][i]).length();
//            ss += (String)table[row+1][i];
        }
//        Log.i(MyConfig.TAG,"Table Row="+(row+1)+":" +ss);
        Arrays.sort(lengthArr);
        int maxlength = lengthArr[lengthArr.length-1];
        int heightTemp;
        if(maxlength<=42){
            heightTemp = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, HEIGHT_DIP, context.getResources().getDisplayMetrics()));
        }else if(maxlength<=72){
            heightTemp = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, HEIGHT_DIP_MEDIUM, context.getResources().getDisplayMetrics()));
        }else if(maxlength<=102){
            heightTemp = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, HEIGHT_DIP_LARGE, context.getResources().getDisplayMetrics()));
        }else{
            heightTemp = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, HEIGHT_DIP_LARGE2, context.getResources().getDisplayMetrics()));
        }
        return heightTemp;
    }

    @Override
    public int getWidth(int column) {
        if(column==-1){return 0;}
        int[] lengthArr = new int[table.length];
        for (int i=0;i<table.length;i++){
            lengthArr[i] = ((String)table[i][column+1]).length();
        }
        Arrays.sort(lengthArr);
        int maxlength = lengthArr[lengthArr.length-1];
        int widthTemp;
        if(maxlength<=10){
            widthTemp = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, WIDTH_DIP_SMALL, context.getResources().getDisplayMetrics()));
        }else if(maxlength<=15){
            widthTemp = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, WIDTH_DIP_MEDIUM, context.getResources().getDisplayMetrics()));
        }else if(maxlength<=23){
            widthTemp = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, WIDTH_DIP_LARGE, context.getResources().getDisplayMetrics()));
        }else{
            widthTemp = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, WIDTH_DIP_LARGE2, context.getResources().getDisplayMetrics()));
        }
        return widthTemp;
    }

    @Override
    public int getItemViewType(int row, int column) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }
}

