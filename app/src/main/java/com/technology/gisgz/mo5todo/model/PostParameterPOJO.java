package com.technology.gisgz.mo5todo.model;

/**
 * Created by Jim.Lee on 2016/7/18.
 */
public class PostParameterPOJO {
    public static PostParameterPOJO BlANK_PARAMETER = new PostParameterPOJO();
    public String updatedAt;
    public String comment;
    public String OrderByStr = "UpdatedAt|||desc";
    public String whereStr = "";

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getOrderByStr() {
        return OrderByStr;
    }

    public void setOrderByStr(String orderByStr) {
        OrderByStr = orderByStr;
    }

    public String getWhereStr() {
        return whereStr;
    }

    public void setWhereStr(String whereStr) {
        this.whereStr = whereStr;
    }
}
