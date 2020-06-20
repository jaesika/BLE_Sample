package com.example.smartbycicylelock.ExteriorDB;

import com.google.gson.annotations.SerializedName;

public class GetNameResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("UserName")
    private String UserName;

    @SerializedName("UserEmail")
    private String UserEmail;

    @SerializedName("UserDevice")
    private String UserDevice;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getUserName() {return UserName;}

    public String getUserEmail() {return UserEmail;}

    public String getUserDevice() {return UserDevice;}
}
