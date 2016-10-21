package com.technology.gisgz.mo5todo.model;

/**
 * Created by Jim.Lee on 2016/3/22.
 */
public class NewVersionPOJO extends BasePOJO{
    private Boolean IsNewVersionReady;
    private int NewVersionCode;
    private String NewVersionName;
    private String NewVersionDesc;
    private String Rule;
    private float ApkSize;


    public float getApkSize() {
        return ApkSize;
    }

    public void setApkSize(float apkSize) {
        ApkSize = apkSize;
    }

    public String getRule() {
        return Rule;
    }

    public void setRule(String rule) {
        Rule = rule;
    }

    public Boolean getNewVersionReady() {
        return IsNewVersionReady;
    }

    public void setNewVersionReady(Boolean newVersionReady) {
        IsNewVersionReady = newVersionReady;
    }

    public int getNewVersionCode() {
        return NewVersionCode;
    }

    public void setNewVersionCode(int newVersionCode) {
        NewVersionCode = newVersionCode;
    }

    public String getNewVersionName() {
        return NewVersionName;
    }

    public void setNewVersionName(String newVersionName) {
        NewVersionName = newVersionName;
    }

    public String getNewVersionDesc() {
        return NewVersionDesc;
    }

    public void setNewVersionDesc(String newVersionDesc) {
        NewVersionDesc = newVersionDesc;
    }


}
