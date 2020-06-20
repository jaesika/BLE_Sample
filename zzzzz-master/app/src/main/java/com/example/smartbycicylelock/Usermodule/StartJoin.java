package com.example.smartbycicylelock.Usermodule;

public class StartJoin {
    private String name;
    private String password;
    private String email;
    private String code;

    public StartJoin(String name, String password, String email, String code)
    {
        this.name = name;
        this.password = password;
        this.email = email;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getCode() {
        return code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
