package com.example.smartbycicylelock.ExteriorDB;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("username")
    private String username;

    @SerializedName("user_email")
    private String userEmail;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getUserName() {
        return username;
    }

    public String getUserEmail(){return userEmail;}
}
