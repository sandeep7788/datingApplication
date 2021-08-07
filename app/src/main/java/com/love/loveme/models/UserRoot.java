package com.love.loveme.models;

import com.google.gson.annotations.SerializedName;

public class UserRoot {

    @SerializedName("data")
    private User data;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public User getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }


}