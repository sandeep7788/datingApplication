package com.love.loveme.models;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("user_email")
    private String userEmail;

    @SerializedName("full_name")
    private String fullName;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("login_type")
    private String loginType;

    @SerializedName("user_name")
    private String userName;

    @SerializedName("identity")
    private String identity;

    @SerializedName("my_wallet")
    private String myWallet;

    @SerializedName("user_profile")
    private String userProfile;

    @SerializedName("token")
    private String token;


    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getMyWallet() {
        return myWallet;
    }

    public void setMyWallet(String myWallet) {
        this.myWallet = myWallet;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
