package com.koncle.teaencylopedia.bean;

/**
 * Created by 10976 on 2017/7/16.
 */

public class DetailJson {
    private Detail data;
    private String errorMessage;

    public Detail getDetial() {
        return data;
    }

    public void setDetial(Detail detial) {
        this.data = detial;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
