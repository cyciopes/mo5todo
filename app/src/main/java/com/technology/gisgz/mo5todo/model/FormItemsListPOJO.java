package com.technology.gisgz.mo5todo.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jim.Lee on 2016/4/21.
 */
public class FormItemsListPOJO extends BasePOJO {
    public static class FormItem{
        private String FormName;
        private String FormDescription;
        private String Applicant;
        private String CreatedAt;
        @SerializedName("CreatedUser")
        private String CreatedBy;
        private String UpdatedAt;
        @SerializedName("UpdatedUser")
        private String UpdatedBy;
        private int FormId;
        private String TypeName;
        @SerializedName("StatusName")
        private String ApprovalStatusName;
        private String LeaveType;
        private String LeaveStartDate;
        private String LeaveEndDate;

        public String getApprovalStatusName() {
            return ApprovalStatusName;
        }

        public void setApprovalStatusName(String approvalStatusName) {
            ApprovalStatusName = approvalStatusName;
        }
        public String getTypeName() {
            return TypeName;
        }

        public void setTypeName(String typeName) {
            TypeName = typeName;
        }

        public String getLeaveType() {
            return LeaveType;
        }

        public void setLeaveType(String leaveType) {
            LeaveType = leaveType;
        }

        public String getLeaveStartDate() {
            return LeaveStartDate;
        }

        public void setLeaveStartDate(String leaveStartDate) {
            LeaveStartDate = leaveStartDate;
        }

        public String getLeaveEndDate() {
            return LeaveEndDate;
        }

        public void setLeaveEndDate(String leaveEndDate) {
            LeaveEndDate = leaveEndDate;
        }

        private boolean isApprovalOrReject = false;

        public boolean isApprovalOrReject() {
            return isApprovalOrReject;
        }

        public void setApprovalOrReject(boolean approvalOrReject) {
            isApprovalOrReject = approvalOrReject;
        }

        public String getFormName() {
            return FormName;
        }

        public void setFormName(String formName) {
            FormName = formName;
        }

        public String getFormDescription() {
            return FormDescription;
        }

        public void setFormDescription(String formDescription) {
            FormDescription = formDescription;
        }

        public String getApplicant() {
            return Applicant;
        }

        public void setApplicant(String applicant) {
            Applicant = applicant;
        }

        public String getCreatedAt() {
            return CreatedAt;
        }

        public void setCreatedAt(String createdAt) {
            CreatedAt = createdAt;
        }

        public String getCreatedBy() {
            return CreatedBy;
        }

        public void setCreatedBy(String createdBy) {
            CreatedBy = createdBy;
        }

        public String getUpdatedAt() {
            return UpdatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            UpdatedAt = updatedAt;
        }

        public String getUpdatedBy() {
            return UpdatedBy;
        }

        public void setUpdatedBy(String updatedBy) {
            UpdatedBy = updatedBy;
        }

        public int getFormId() {
            return FormId;
        }

        public void setFormId(int formId) {
            FormId = formId;
        }
    }
    public FormItemsListPOJO(){
        formItemList = new ArrayList<FormItem>();
    }

    public List<FormItem> getFormItemList() {
        return formItemList;
    }

    public void setFormItemList(List<FormItem> formItemList) {
        this.formItemList = formItemList;
    }
    @SerializedName("ListForm")
    private List<FormItem> formItemList;
}
