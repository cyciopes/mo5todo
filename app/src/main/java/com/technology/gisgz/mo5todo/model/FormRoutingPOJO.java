package com.technology.gisgz.mo5todo.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Jim.Lee on 2016/5/31.
 */
public class FormRoutingPOJO extends BasePOJO {
    @SerializedName("ApprovalPathRowList")
    private List<ApprovalPathRow> approvalPathRowList;
    @SerializedName("ApprovalHisotyRowList")
    private List<ApprovalHisotyRow> approvalHisotyRowList;

    public List<ApprovalPathRow> getApprovalPathRowList() {
        return approvalPathRowList;
    }

    public void setApprovalPathRowList(List<ApprovalPathRow> approvalPathRow) {
        this.approvalPathRowList = approvalPathRow;
    }

    public List<ApprovalHisotyRow> getApprovalHisotyRowList() {
        return approvalHisotyRowList;
    }

    public void setApprovalHisotyRowList(List<ApprovalHisotyRow> approvalHisotyRowList) {
        this.approvalHisotyRowList = approvalHisotyRowList;
    }

    public static class ApprovalPathRow {
        @SerializedName("Sequence")
        private int sequence;
        @SerializedName("LevelDescription")
        private String levelDescription;
        @SerializedName("UserName")
        private String userName;
        @SerializedName("ActionType")
        private String actionType;
        @SerializedName("Comment")
        private String comment;
        @SerializedName("ActionDateTime")
        private String actionDateTime;

        public int getSequence() {
            return sequence;
        }

        public void setSequence(int sequence) {
            this.sequence = sequence;
        }

        public String getLevelDescription() {
            return levelDescription;
        }

        public void setLevelDescription(String levelDescription) {
            this.levelDescription = levelDescription;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getActionType() {
            return actionType;
        }

        public void setActionType(String actionType) {
            this.actionType = actionType;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getActionDateTime() {
            return actionDateTime;
        }

        public void setActionDateTime(String actionDateTime) {
            this.actionDateTime = actionDateTime;
        }
    }

    public static class ApprovalHisotyRow {
        private String UpdatedBy;
        private String UpdatedAt;
        private String ActionType;
        private String LogInfo;
        private boolean IsCausedLevelChange;
        private String LevelBefore;
        private String LevelAfter;

        public String getUpdatedBy() {
            return UpdatedBy;
        }

        public void setUpdatedBy(String updatedBy) {
            UpdatedBy = updatedBy;
        }

        public String getUpdatedAt() {
            return UpdatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            UpdatedAt = updatedAt;
        }

        public String getActionType() {
            return ActionType;
        }

        public void setActionType(String actionType) {
            ActionType = actionType;
        }

        public String getLogInfo() {
            return LogInfo;
        }

        public void setLogInfo(String logInfo) {
            LogInfo = logInfo;
        }

        public boolean isCausedLevelChange() {
            return IsCausedLevelChange;
        }

        public void setCausedLevelChange(boolean causedLevelChange) {
            IsCausedLevelChange = causedLevelChange;
        }

        public String getLevelBefore() {
            return LevelBefore;
        }

        public void setLevelBefore(String levelBefore) {
            LevelBefore = levelBefore;
        }

        public String getLevelAfter() {
            return LevelAfter;
        }

        public void setLevelAfter(String levelAfter) {
            LevelAfter = levelAfter;
        }
    }
}
