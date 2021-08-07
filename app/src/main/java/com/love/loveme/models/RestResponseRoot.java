package com.love.loveme.models;

import com.google.gson.annotations.SerializedName;

public class RestResponseRoot {

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }
}