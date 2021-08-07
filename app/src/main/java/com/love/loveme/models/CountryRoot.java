package com.love.loveme.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CountryRoot {

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

        @SerializedName("country")
        private String country;

        @SerializedName("updated_at")
        private String updatedAt;

        @SerializedName("country_media")
        private String countryMedia;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("country_id")
        private String countryId;

        public String getCountry() {
            return country;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public String getCountryMedia() {
            return countryMedia;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getCountryId() {
            return countryId;
        }
    }
}