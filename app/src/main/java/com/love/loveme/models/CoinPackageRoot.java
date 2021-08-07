package com.love.loveme.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CoinPackageRoot {

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

        @SerializedName("coin_amount")
        private String coinAmount;

        @SerializedName("image")
        private String image;

        @SerializedName("coin_pkg_id")
        private String coinPkgId;

        @SerializedName("updated_at")
        private Object updatedAt;

        @SerializedName("price")
        private String price;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("playstore_product_id")
        private String playstoreProductId;

        public String getCoinAmount() {
            return coinAmount;
        }

        public String getImage() {
            return image;
        }

        public String getCoinPkgId() {
            return coinPkgId;
        }

        public Object getUpdatedAt() {
            return updatedAt;
        }

        public String getPrice() {
            return price;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getPlaystoreProductId() {
            return playstoreProductId;
        }
    }
}