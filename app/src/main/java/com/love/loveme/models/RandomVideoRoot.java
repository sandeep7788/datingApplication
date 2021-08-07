package com.love.loveme.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RandomVideoRoot {

    @SerializedName("data")
    private Data data;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public Data getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }

    public static class Data {

        CountryRoot.DataItem country;

        public CountryRoot.DataItem getCountry() {
            return country;
        }

        public void setCountry(CountryRoot.DataItem country) {
            this.country = country;
        }

        @SerializedName("rate")
        private String rate;

        @SerializedName("video_gallery")
        private List<String> videoGallery;

        @SerializedName("name")
        private String name;

        @SerializedName("bio")
        private String bio;

        @SerializedName("video")
        private String video;

        @SerializedName("thumb_img")
        private String thumbImg;

        @SerializedName("country_id")
        private String countryId;

        @SerializedName("video_id")
        private String videoId;

        @SerializedName("image_gallery")
        private List<String> imageGallery;

        public String getRate() {
            return rate;
        }

        public List<String> getVideoGallery() {
            return videoGallery;
        }

        public String getName() {
            return name;
        }

        public String getBio() {
            return bio;
        }

        public String getVideo() {
            return video;
        }

        public String getThumbImg() {
            return thumbImg;
        }

        public String getCountryId() {
            return countryId;
        }

        public String getVideoId() {
            return videoId;
        }

        public List<String> getImageGallery() {
            return imageGallery;
        }
    }
}