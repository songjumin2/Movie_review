package com.songjumin.moviereview.model;

public class UserReq {

    private int user_id;
    private String email;
    private String passwd;

    public UserReq(int user_id, String email, String passwd) {
        this.user_id = user_id;
        this.email = email;
        this.passwd = passwd;
    }

    public UserReq(String email, String passwd) {
        this.email = email;
        this.passwd = passwd;
    }

    public UserReq(){

    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
