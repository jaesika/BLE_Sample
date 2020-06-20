package com.example.smartbycicylelock.service;

import com.example.smartbycicylelock.ExteriorDB.GetNameData;
import com.example.smartbycicylelock.ExteriorDB.GetNameResponse;
import com.example.smartbycicylelock.ExteriorDB.JoinData;
import com.example.smartbycicylelock.ExteriorDB.JoinResponse;
import com.example.smartbycicylelock.ExteriorDB.LoginData;
import com.example.smartbycicylelock.ExteriorDB.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServiceApi {
    @POST("/user/login") // 로그인시 보내는 data
    Call<LoginResponse> userLogin(@Body LoginData data);

    @POST("/user/join") // 회원가입시 보내는 data
    Call<JoinResponse> userJoin(@Body JoinData data);

    @POST("/user/get_name") // 회원정보 출력 data
    Call<GetNameResponse> userGetName(@Body GetNameData data);



}
