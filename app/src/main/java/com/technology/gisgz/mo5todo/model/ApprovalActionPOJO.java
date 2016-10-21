package com.technology.gisgz.mo5todo.model;

/**
 * Created by Jim.Lee on 2016/5/24.
 */
public class ApprovalActionPOJO extends BasePOJO {
    int formID;
    int action;
    int Status;
//    "Approve": false,
//            "Reject": true,
//            "Undo": false,
//            "Execute": true,
//            "CommentBox": true,
    boolean Approve;
    boolean Reject;
    boolean Undo;
    boolean Execute;
    boolean CommentBox;

    public String getUpdatedAtForProgram() {
        return updatedAtForProgram;
    }

    public void setUpdatedAtForProgram(String updatedAtForProgram) {
        this.updatedAtForProgram = updatedAtForProgram;
    }

    private String updatedAtForProgram;

    public boolean isApprove() {
        return Approve;
    }

    public void setApprove(boolean approve) {
        Approve = approve;
    }

    public boolean isReject() {
        return Reject;
    }

    public void setReject(boolean reject) {
        Reject = reject;
    }

    public boolean isUndo() {
        return Undo;
    }

    public void setUndo(boolean undo) {
        Undo = undo;
    }

    public boolean isExecute() {
        return Execute;
    }

    public void setExecute(boolean execute) {
        Execute = execute;
    }

    public boolean isCommentBox() {
        return CommentBox;
    }

    public void setCommentBox(boolean commentBox) {
        CommentBox = commentBox;
    }

    public int getFormID() {
        return formID;
    }

    public void setFormID(int formID) {
        this.formID = formID;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }


}
