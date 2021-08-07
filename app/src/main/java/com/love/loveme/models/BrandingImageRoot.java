package com.love.loveme.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BrandingImageRoot {

    @SerializedName("data")
    private List<DataItem> data;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public List<DataItem> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }

    public static class DataItem {

        @SerializedName("image")
        private String image;

        @SerializedName("updated_at")
        private Object updatedAt;

        @SerializedName("branding_id")
        private String brandingId;

        @SerializedName("created_at")
        private String createdAt;

        public String getImage() {
            return image;
        }

        public Object getUpdatedAt() {
            return updatedAt;
        }

        public String getBrandingId() {
            return brandingId;
        }

        public String getCreatedAt() {
            return createdAt;
        }
    }
}