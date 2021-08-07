package com.love.loveme.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GiftCategoryRoot {

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

        @SerializedName("gift_cat_name")
        private String giftCatName;

        @SerializedName("updated_at")
        private String updatedAt;

        @SerializedName("gift_cat_media")
        private String giftCatMedia;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("gift_cat_id")
        private String giftCatId;

        public String getGiftCatName() {
            return giftCatName;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public String getGiftCatMedia() {
            return giftCatMedia;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getGiftCatId() {
            return giftCatId;
        }
    }
}