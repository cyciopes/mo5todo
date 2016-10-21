package com.technology.gisgz.mo5todo.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.technology.gisgz.mo5todo.R;
import com.technology.gisgz.mo5todo.activity.FormListActivity;
import com.technology.gisgz.mo5todo.model.PostParameterPOJO;
import com.technology.gisgz.mo5todo.utils.MyConfig;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialIconView;

/**
 * Created by Jim.Lee on 2016/8/22.
 */
public class SortingDialogFrament extends DialogFragment implements View.OnClickListener {
    private View layout;
    private PostParameterPOJO postParameterPOJO;
    private View updatedAtView;
    private View createdAtView;
    private View formTitleView;
    private View createdByView;
    private View updatedByView;
    private View applicantView;
    private View updatedAtViewRow;
    private View createdAtViewRow;
    private View formTitleViewRow;
    private View createdByViewRow;
    private View updatedByViewRow;
    private View applicantViewRow;

    public SortingDialogFrament(){
    }


    public SortingDialogFrament(PostParameterPOJO pp){
        postParameterPOJO = pp;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        init();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        handleSortingView();
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(layout);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((FormListActivity)getActivity()).myConfig.getBusInstance().post(postParameterPOJO);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,
                                int whichButton) {
                // 点击"取消"按钮之后退出程序
                dialog.dismiss();
            }
        });
        return builder.create();
    }
    private void init() {
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        layout = inflater.inflate(R.layout.formlist_sorting, null);
        updatedAtView = layout.findViewById(R.id.updatedAt);
        createdAtView = layout.findViewById(R.id.createdAt);
        formTitleView = layout.findViewById(R.id.formTitle);
        createdByView = layout.findViewById(R.id.createdBy);
        updatedByView = layout.findViewById(R.id.updatedBy);
        applicantView = layout.findViewById(R.id.applicant);

        updatedAtViewRow = layout.findViewById(R.id.updatedAtRow);
        createdAtViewRow = layout.findViewById(R.id.createdAtRow);
        formTitleViewRow = layout.findViewById(R.id.formTitleRow);
        createdByViewRow = layout.findViewById(R.id.createdByRow);
        updatedByViewRow = layout.findViewById(R.id.updatedByRow);
        applicantViewRow = layout.findViewById(R.id.applicantRow);
        updatedAtViewRow.setOnClickListener(this);
        createdAtViewRow.setOnClickListener(this);
        formTitleViewRow.setOnClickListener(this);
        createdByViewRow.setOnClickListener(this);
        updatedByViewRow.setOnClickListener(this);
        applicantViewRow.setOnClickListener(this);
    }
    private void handleSortingView() {
        updatedAtView.setVisibility(View.GONE);
        createdAtView.setVisibility(View.GONE);
        formTitleView.setVisibility(View.GONE);
        createdByView.setVisibility(View.GONE);
        updatedByView.setVisibility(View.GONE);
        applicantView.setVisibility(View.GONE);

        String orderByField = postParameterPOJO.getOrderByStr().split("\\|\\|\\|")[0];
        String orderByType = postParameterPOJO.getOrderByStr().split("\\|\\|\\|")[1];
        View usingField = null;
        if(orderByField.equalsIgnoreCase("UpdatedAt")){
            usingField = updatedAtView;
        }else if (orderByField.equalsIgnoreCase("CreatedAt")){
            usingField = createdAtView;
        }else if (orderByField.equalsIgnoreCase("EntityTitle")){
            usingField = formTitleView;
        }else if (orderByField.equalsIgnoreCase("CreatedUser")){
            usingField = createdByView;
        }else if (orderByField.equalsIgnoreCase("UpdatedUser")){
            usingField = updatedByView;
        }else if (orderByField.equalsIgnoreCase("Applicant")){
            usingField = applicantView;
        }
        if(orderByType.equalsIgnoreCase("asc")){
            ((MaterialIconView)usingField).setIcon(MaterialDrawableBuilder.IconValue.SORT_ASCENDING);
        }else{
            ((MaterialIconView)usingField).setIcon(MaterialDrawableBuilder.IconValue.SORT_DESCENDING);
        }
        usingField.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        String orderByField = "";
        String orderByType = "";
        switch(view.getId()){
            case R.id.updatedAtRow:
                orderByField = "UpdatedAt";
                break;
            case R.id.createdAtRow:
                orderByField = "CreatedAt";
                break;
            case R.id.formTitleRow:
                orderByField = "EntityTitle";
                break;
            case R.id.createdByRow:
                orderByField = "CreatedUser";
                break;
            case R.id.updatedByRow:
                orderByField = "UpdatedUser";
                break;
            case R.id.applicantRow:
                orderByField = "Applicant";
                break;
        }
        if(orderByField.equalsIgnoreCase(postParameterPOJO.getOrderByStr().split("\\|\\|\\|")[0])){
            if("desc".equalsIgnoreCase(postParameterPOJO.getOrderByStr().split("\\|\\|\\|")[1])){
                orderByType = "asc";
            }else{
                orderByType = "desc";
            }
        }else{
            orderByType = "desc";
        }
        postParameterPOJO.setOrderByStr(orderByField+"|||"+orderByType);
        handleSortingView();
    }
}
