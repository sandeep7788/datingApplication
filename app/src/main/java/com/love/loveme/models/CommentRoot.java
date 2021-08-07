package com.love.loveme.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommentRoot {

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

        @SerializedName("comment")
        private String comment;

        String name;

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @SerializedName("comment_id")
        private String commentId;

        @SerializedName("country_id")
        private String countryId;

        public String getComment() {
            return comment;
        }

        public String getCommentId() {
            return commentId;
        }

        public String getCountryId() {
            return countryId;
        }
    }
}