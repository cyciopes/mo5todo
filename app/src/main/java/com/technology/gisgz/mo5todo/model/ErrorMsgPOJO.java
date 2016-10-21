package com.technology.gisgz.mo5todo.model;

/**
 * Created by Jim.Lee on 2016/3/22.
 */
public class ErrorMsgPOJO {
    int Code;
    String Msg;

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        this.Code = code;
    }

    public ErrorMsgPOJO() {
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        this.Msg = msg;
    }
    @Override
    public String toString(){
        return "errorcode:"+getCode()+",Msg:"+getMsg();
    }
}
