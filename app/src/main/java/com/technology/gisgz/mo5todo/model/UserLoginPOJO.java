package com.technology.gisgz.mo5todo.model;

/**
 * Created by Jim.Lee on 2016/3/22.
 */
public class UserLoginPOJO extends BasePOJO{
    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int id) {
        UserId = id;
    }

    public String getUserPassword() {
        return UserPassword;
    }

    public void setUserPassword(String userPassword) {
        UserPassword = userPassword;
    }

    public String getUserCompany() {
        return UserCompany;
    }

    public void setUserCompany(String userCompany) {
        UserCompany = userCompany;
    }

    public String getLoginType() {
        return LoginType;
    }

    public void setLoginType(String loginType) {
        LoginType = loginType;
    }

    public UserLoginPOJO() {

    }
    //TODO hardcode
    int UserId = 0;
    String UserName;
    String UserPassword;
    String LoginType;
    String UserCompany;

    @Override
    public String toString(){
        return "UserId:"+getUserId()+",CompanyName:"+getUserCompany()+",UserName:"+getUserName()+",Password:"+getUserPassword()+","+getErrorMsg().toString();
    }
}
