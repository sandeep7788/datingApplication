package com.love.loveme.models;

import com.google.gson.annotations.SerializedName;

public class SettingRoot {

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

        @SerializedName("admob_banner_id")
        private String admobBannerId;

        @SerializedName("seconds_for_call")
        private String secondsForCall;

        @SerializedName("admob_publisher_id")
        private String admobPublisherId;

        @SerializedName("fb_rewarded_id")
        private String fbRewardedId;

        @SerializedName("fb_interestial_id")
        private String fbInterestialId;

        @SerializedName("razorpay_key_secret")
        private String razorpayKeySecret;

        @SerializedName("seconds_for_ad")
        private String secondsForAd;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("fb_banner_id")
        private String fbBannerId;

        @SerializedName("log_in_bonus")
        private String logInBonus;

        @SerializedName("video_ad_bonus")
        private String videoAdBonus;

        @SerializedName("refer_friend_bonus")
        private String referFriendBonus;

        @SerializedName("updated_at")
        private String updatedAt;

        @SerializedName("admob_rewarded_id")
        private String admobRewardedId;

        @SerializedName("privacy_policy")
        private String privacyPolicy;

        @SerializedName("admob_interestial_id")
        private String admobInterestialId;

        @SerializedName("razorpay_key_id")
        private String razorpayKeyId;

        @SerializedName("id")
        private String id;

        @SerializedName("more_apps_url")
        private String moreAppsUrl;

        public String getAdmobBannerId() {
            return admobBannerId;
        }

        public String getSecondsForCall() {
            return secondsForCall;
        }

        public String getAdmobPublisherId() {
            return admobPublisherId;
        }

        public String getFbRewardedId() {
            return fbRewardedId;
        }

        public String getFbInterestialId() {
            return fbInterestialId;
        }

        public String getRazorpayKeySecret() {
            return razorpayKeySecret;
        }

        public String getSecondsForAd() {
            return secondsForAd;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getFbBannerId() {
            return fbBannerId;
        }

        public String getLogInBonus() {
            return logInBonus;
        }

        public String getVideoAdBonus() {
            return videoAdBonus;
        }

        public String getReferFriendBonus() {
            return referFriendBonus;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public String getAdmobRewardedId() {
            return admobRewardedId;
        }

        public String getPrivacyPolicy() {
            return privacyPolicy;
        }

        public String getAdmobInterestialId() {
            return admobInterestialId;
        }

/*        public String getRazorpayKeyId() {
            return razorpayKeyId;
        }*/

        public String getId() {
            return id;
        }

        public String getMoreAppsUrl() {
            return moreAppsUrl;
        }
    }
}