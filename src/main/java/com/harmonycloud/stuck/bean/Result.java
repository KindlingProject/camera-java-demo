package com.harmonycloud.stuck.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result implements Serializable {
    private static final long serialVersionUID = -3897739689636593413L;

    private final boolean success;
    private final Object data;
    private final String errorMsg;

    private Result(boolean success, Object data, String errorMsg) {
        this.success = success;
        this.data = data;
        this.errorMsg = errorMsg;
    }

    public static Result success(Object data) {
        return new Result(true, data, null);
    }

    public static Result fail(String errorMsg) {
        return new Result(false, null, errorMsg);
    }

    public boolean isSuccess() {
        return success;
    }

    public Object getData() {
        return data;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}