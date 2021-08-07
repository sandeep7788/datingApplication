package com.love.loveme.models;

import com.google.gson.annotations.SerializedName;

public class RestResponseNumber {

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public String getMessage() {
        return message;
    }

    public boolean getStatus() {
        return status;
    }
}