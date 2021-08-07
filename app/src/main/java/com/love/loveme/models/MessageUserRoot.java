package com.love.loveme.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MessageUserRoot implements Serializable {

    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<DataItem> data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataItem> getData() {
        return data;
    }

    public void setData(List<DataItem> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return
                "MessageUserRoot{" +
                        "status = '" + status + '\'' +
                        ",message = '" + message + '\'' +
                        ",data = '" + data + '\'' +
                        "}";
    }


    public static class DataItem implements Serializable {

        @SerializedName("profile_id")
        private String profileId;

        @SerializedName("name")
        private String name;

        @SerializedName("image")
        private String image;

        @SerializedName("bio")
        private String bio;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("updated_at")
        private String updatedAt;

        public String getProfileId() {
            return profileId;
        }

        public void setProfileId(String profileId) {
            this.profileId = profileId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        @Override
        public String toString() {
            return
                    "DataItem{" +
                            "profile_id = '" + profileId + '\'' +
                            ",name = '" + name + '\'' +
                            ",image = '" + image + '\'' +
                            ",bio = '" + bio + '\'' +
                            ",created_at = '" + createdAt + '\'' +
                            ",updated_at = '" + updatedAt + '\'' +
                            "}";
        }
    }
}