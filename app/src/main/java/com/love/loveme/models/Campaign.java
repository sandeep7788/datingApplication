package com.love.loveme.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Campaign {

    @SerializedName("data")
    private List<Data> data;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public List<Data> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }

    public static class Data {

        @SerializedName("campaign_title")
        private String campaignTitle;

        @SerializedName("interestial_image")
        private String interestialImage;

        @SerializedName("icon")
        private String icon;

        @SerializedName("description")
        private String description;

        @SerializedName("rewarded_video")
        private String rewardedVideo;

        @SerializedName("button_text")
        private String buttonText;

        @SerializedName("banner_image")
        private String bannerImage;

        @SerializedName("campaign_id")
        private String campaignId;

        @SerializedName("status")
        private String status;

        public String getCampaignTitle() {
            return campaignTitle;
        }

        public String getInterestialImage() {
            return interestialImage;
        }

        public String getIcon() {
            return icon;
        }

        public String getDescription() {
            return description;
        }

        public String getRewardedVideo() {
            return rewardedVideo;
        }

        public String getButtonText() {
            return buttonText;
        }

        public String getBannerImage() {
            return bannerImage;
        }

        public String getCampaignId() {
            return campaignId;
        }

        public String getStatus() {
            return status;
        }
    }
}