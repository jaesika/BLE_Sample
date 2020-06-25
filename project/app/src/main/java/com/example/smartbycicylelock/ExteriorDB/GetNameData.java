package com.example.smartbycicylelock.ExteriorDB;

import com.google.gson.annotations.SerializedName;

public class GetNameData {
    @SerializedName("userEmail")
    String userEmail;

    @SerializedName("userName")
    String userName;

    @SerializedName("userDevice")
    String userDevice;

    public GetNameData(String userEmail, String userName, String userDeivce) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.userDevice = userDeivce;
    }
}
