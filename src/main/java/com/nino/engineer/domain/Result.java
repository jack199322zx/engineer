package com.nino.engineer.domain;

public class Result {

    private String code;
    private String message;
    private Object data;


    private Result(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private Result(String code, String message, Object obj) {
        this.code = code;
        this.message = message;
        this.data = obj;
    }

    public static Result ok(Object data) {
        return new Result("0", "请求成功", data);
    }

    public static Result ok(String code, String message) {
        return new Result(code, message);
    }

    public static Result ok(String code, String message, Object data) {
        return new Result(code, message, data);
    }

    public static Result fail(Object data) {
        return new Result("1", "请求失败", data);
    }

    public static Result fail(String code, String message) {
        return new Result(code, message);
    }

    public static Result fail(String code, String message, Object data) {
        return new Result(code, message, data);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
