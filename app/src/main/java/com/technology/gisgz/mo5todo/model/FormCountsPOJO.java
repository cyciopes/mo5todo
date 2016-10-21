package com.technology.gisgz.mo5todo.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jim.Lee on 2016/4/20.
 */
public class FormCountsPOJO extends BasePOJO{
    public int getFormCount() {
        return FormCount;
    }

    public void setFormCount(int formCount) {
        this.FormCount = formCount;
    }


    @SerializedName("Count")
    private int FormCount;

}
