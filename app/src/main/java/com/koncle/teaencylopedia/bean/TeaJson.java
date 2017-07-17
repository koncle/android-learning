package com.koncle.teaencylopedia.bean;

import java.util.List;

/**
 * Created by 10976 on 2017/7/16.
 */

public class TeaJson {
    private List<Tea> data;
    private String errorMessage;

    public List<Tea> getData() {
        return data;
    }

    public void setData(List<Tea> data) {
        this.data = data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
