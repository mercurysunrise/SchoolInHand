package com.zhilianxinke.schoolinhand.domain;

import java.io.Serializable;

/**
 * Created by hh on 2015-04-19.
 */
public class SdkHttpResult implements Serializable {

    private int code;
    private String result;

    public SdkHttpResult(int code, String result) {
        this.code = code;
        this.result = result;
    }

    public SdkHttpResult() {
    }

    public String getResult() {
        return result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setResult(String result) {
        this.result = result;
    }
}