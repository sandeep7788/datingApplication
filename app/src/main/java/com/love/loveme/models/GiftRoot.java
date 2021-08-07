package com.love.loveme.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GiftRoot {

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

        @SerializedName("gift_media")
        private String giftMedia;

        @SerializedName("coins")
        private String coins;

        @SerializedName("gift_id")
        private String giftId;

        public String getGiftMedia() {
            return giftMedia;
        }

        public String getCoins() {
            return coins;
        }

        public String getGiftId() {
            return giftId;
        }
    }
}