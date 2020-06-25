package com.example.smartbycicylelock.Usermodule;

public class StartLogin {

    private String email;
    private String password;

    public StartLogin(String email, String password)
    {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
