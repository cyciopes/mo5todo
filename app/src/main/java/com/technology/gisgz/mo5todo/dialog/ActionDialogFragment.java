package com.technology.gisgz.mo5todo.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.technology.gisgz.mo5todo.R;
import com.technology.gisgz.mo5todo.activity.FormDetailActivity;
import com.technology.gisgz.mo5todo.utils.MyConfig;

public class ActionDialogFragment extends DialogFragment implements View.OnClickListener {
    private View layout;

    private boolean isShowApprove = true;
    private boolean isShowReject = true;
    private boolean isShowUndo = true;
    private boolean isShowComment = true;
    private boolean isShowExecute = true;

    public void setIsShowButton(boolean isShowComment,boolean isShowApprove,boolean isShowReject,boolean isShowUndo,boolean isShowExecute){
        this.isShowApprove =isShowApprove;
        this.isShowReject =isShowReject;
        this.isShowUndo =isShowUndo;
        this.isShowComment=isShowComment;
        this.isShowExecute=isShowExecute;
    }
    private void showButton(){
        layout.findViewById(R.id.approve).setVisibility(isShowApprove?View.VISIBLE:View.GONE);
        layout.findViewById(R.id.reject).setVisibility(isShowReject?View.VISIBLE:View.GONE);
        layout.findViewById(R.id.undo).setVisibility(isShowUndo?View.VISIBLE:View.GONE);
        layout.findViewById(R.id.comment).setVisibility(isShowComment?View.VISIBLE:View.GONE);
        layout.findViewById(R.id.execute).setVisibility(isShowExecute?View.VISIBLE:View.GONE);
    }

    @Override
    public void onClick(View view) {
        int action = -1;
        switch(view.getId()){
            case R.id.approve:
                action = MyConfig.ACTION_APPROVE;
                break;
            case R.id.reject:
                action = MyConfig.ACTION_REJECT;
                break;
            case R.id.undo:
                action = MyConfig.ACTION_UNDO;
                break;
            case R.id.execute:
                action = MyConfig.ACTION_EXECUTE;
                break;
        }
        if (action!=-1){
            String comment = ((TextView)layout.findViewById(R.id.comment)).getText().toString();
            if(action == MyConfig.ACTION_REJECT && TextUtils.isEmpty(comment)) {
                //Snackbar.make(getActivity().findViewById(R.id.main_content), getString(R.string.FormApproval_commentcannotempty_result), Snackbar.LENGTH_LONG).show();
                // 1. Instantiate an AlertDialog.Builder with its constructor
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());

                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage(getString(R.string.FormApproval_commentcannotempty_result));

                // 3. Get the AlertDialog from create()
                android.app.AlertDialog dialog = builder.create();
                dialog.show();
                return;
            }
            ((IFormActionListener) getActivity()).onFormActionClick(comment,action);
            dismiss();
        }
    }

    public interface IFormActionListener
    {
        void onFormActionClick(String comment, int action);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        init();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(layout)
                // Add action buttons
                .setNegativeButton("Cancel", null);
        return builder.create();
    }


    private void init() {
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        layout = inflater.inflate(R.layout.approvalaction_popup, null);
        showButton();
        bindButtonClick();
    }

    private void bindButtonClick() {
        Button approveButton = (Button)layout.findViewById(R.id.approve);
        Button rejectButton = (Button)layout.findViewById(R.id.reject);
        Button undoButton = (Button)layout.findViewById(R.id.undo);
        Button executeButton = (Button)layout.findViewById(R.id.execute);
        approveButton.setOnClickListener(this);
        rejectButton.setOnClickListener(this);
        undoButton.setOnClickListener(this);
        executeButton.setOnClickListener(this);
    }

}