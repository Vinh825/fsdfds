package com.example.IAM.utils.constant;

public enum ErrorInfo {

    UNAUTHORIZED("ERR_0001", "Unauthorized");

    private String code;
    private String text;

    private ErrorInfo(String code, String text) {
        this.code = code;
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
