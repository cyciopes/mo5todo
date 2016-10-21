package com.technology.gisgz.mo5todo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.technology.gisgz.mo5todo.R;
import com.technology.gisgz.mo5todo.activity.FormDetailPagerActivity;
import com.technology.gisgz.mo5todo.activity.FormListActivity;
import com.technology.gisgz.mo5todo.model.FormItemsListPOJO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jim.Lee on 2016/4/21.
 */
public class FormListAdapter extends RecyclerView.Adapter<FormListAdapter.FormItemViewHolder> {
    private List<FormItemsListPOJO.FormItem> formList;
    private Context context;
    private List<Integer> highLighList = new ArrayList();
    private static int LeaveFormType = 0;
    private static int NormalFormType = 1;

    public FormListAdapter(List formList, Context context){
        this.formList = formList;
        this.context = context;
    }

    public void makeChange(int formId){
        highLighList.add(new Integer(formId));
        for (int i=0;i<formList.size();i++){
            if(formId==formList.get(i).getFormId()){
                this.notifyItemChanged(i);
                break;
            }
        }
    }


    @Override
    public void onBindViewHolder(FormListAdapter.FormItemViewHolder vh, final int i) {
        if(highLighList.contains(formList.get(i).getFormId())){
            vh.setHighLigth(true);
        }else{
            vh.setHighLigth(false);
        }
        vh.bind(i);
        vh.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,FormDetailPagerActivity.class);
                intent.putExtra("formId", formList.get(i).getFormId());
                intent.putExtra("formType", getItemViewType(i)==LeaveFormType?"leave":"normal");
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return formList.size();
    }

    @Override
    public void onViewRecycled(FormItemViewHolder holder) {
        super.onViewRecycled(holder);
        holder.setHighLigth(false);
    }


    // return, 0 is normal form, 1 is leave form
    @Override
    public int getItemViewType(int position) {
        if(formList.get(position).getTypeName().equalsIgnoreCase("Leave Form")){
            return LeaveFormType;
        }else if (formList.get(position).getTypeName().equalsIgnoreCase("Form")){
            return NormalFormType;
        }
        return 0;
    }

    @Override
    public FormListAdapter.FormItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if(((FormListActivity)context).isActionHistory){
            View v = LayoutInflater.from(context).inflate(R.layout.formlist_item_viewholder_actionhistory,viewGroup,false);
            return new FormItemViewHolder(v);
        }

        if (i==LeaveFormType){
            View v = LayoutInflater.from(context).inflate(R.layout.formlist_item_viewholder_leave,viewGroup,false);
            return new FormItemViewHolder(v);
        }else if(i==NormalFormType){
            View v = LayoutInflater.from(context).inflate(R.layout.formlist_item_viewholder_normal,viewGroup,false);
            return new FormItemViewHolder(v);
        }else{
            View v = LayoutInflater.from(context).inflate(R.layout.formlist_item_viewholder_normal,viewGroup,false);
            return new FormItemViewHolder(v);
        }
    }
    //自定义ViewHolder类
    public class FormItemViewHolder extends RecyclerView.ViewHolder{

//        TextView formTitle;
//        TextView formDescription;
//        TextView CreatedUser;
//        TextView CreatedAt;
        View cardView;


        public FormItemViewHolder(final View itemView) {
            super(itemView);
            cardView =itemView;
//            formTitle = (TextView) itemView.findViewById(R.id.formListItem_formtitle);
//            formDescription = (TextView) itemView.findViewById(R.id.formListItem_formDescription);
//            CreatedUser = (TextView) itemView.findViewById(R.id.formListItem_CreatedUser);
//            CreatedAt = (TextView) itemView.findViewById(R.id.formListItem_CreatedAt);
        }

        public void setHighLigth(boolean b) {
            if(b){
                cardView.setBackgroundResource(R.drawable.color_clicked);
            }else{
                cardView.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            }
        }
        public void bind(int i){
            FormItemsListPOJO.FormItem fi = formList.get(i);
            if(((FormListActivity)context).isMyAction){
                ((TextView)cardView.findViewById(R.id.formListItem_formtitle)).setText(fi.getFormName());
                ((TextView)cardView.findViewById(R.id.formListItem_Applicant)).setText(fi.getApplicant());
                ((TextView)cardView.findViewById(R.id.formListItem_approvalstatus)).setText(fi.getApprovalStatusName());
                ((TextView)cardView.findViewById(R.id.formListItem_UpdatedUser)).setText(fi.getUpdatedBy());
                ((TextView)cardView.findViewById(R.id.formListItem_UpdatedAt)).setText(fi.getUpdatedAt());
                if(FormListAdapter.this.getItemViewType(i)==LeaveFormType){
                    ((TextView)cardView.findViewById(R.id.formListItem_LeaveStartDate)).setText(fi.getLeaveStartDate());
                    ((TextView)cardView.findViewById(R.id.formListItem_LeaveEndDate)).setText(fi.getLeaveEndDate());
                }else if(FormListAdapter.this.getItemViewType(i)==NormalFormType){
                    ((TextView)cardView.findViewById(R.id.formListItem_formDescription)).setText(fi.getFormDescription());
                }
            }else if(((FormListActivity)context).isActionHistory){
                ((TextView)cardView.findViewById(R.id.formListItem_formtitle)).setText(fi.getFormName());
                ((TextView)cardView.findViewById(R.id.formListItem_Applicant)).setText(fi.getApplicant());
                ((TextView)cardView.findViewById(R.id.formListItem_approvalstatus)).setText(fi.getApprovalStatusName());
                ((TextView)cardView.findViewById(R.id.formListItem_UpdatedUser)).setText(fi.getUpdatedBy());
                ((TextView)cardView.findViewById(R.id.formListItem_UpdatedAt)).setText(fi.getUpdatedAt());
                if(FormListAdapter.this.getItemViewType(i)==LeaveFormType){
                    (cardView.findViewById(R.id.formListItem_formDescription)).setVisibility(View.GONE);
                }else if(FormListAdapter.this.getItemViewType(i)==NormalFormType){
                    ((TextView)cardView.findViewById(R.id.formListItem_formDescription)).setText(fi.getFormDescription());
                }
            }
        }
    }
}
