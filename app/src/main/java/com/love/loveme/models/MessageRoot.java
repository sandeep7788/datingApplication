package com.love.loveme.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class MessageRoot implements Serializable {

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
                "MessageRoot{" +
                        "status = '" + status + '\'' +
                        ",message = '" + message + '\'' +
                        ",data = '" + data + '\'' +
                        "}";
    }


    public static class DataItem implements Serializable {

        @SerializedName("message_id")
        private String messageId;

        @SerializedName("type")
        private String type;

        @SerializedName("message_file_text")
        private String messageFileText;

        @SerializedName("content")
        private String content;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("updated_at")
        private String updatedAt;

        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMessageFileText() {
            return messageFileText;
        }

        public void setMessageFileText(String messageFileText) {
            this.messageFileText = messageFileText;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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
                            "message_id = '" + messageId + '\'' +
                            ",type = '" + type + '\'' +
                            ",message_file_text = '" + messageFileText + '\'' +
                            ",content = '" + content + '\'' +
                            ",created_at = '" + createdAt + '\'' +
                            ",updated_at = '" + updatedAt + '\'' +
                            "}";
        }
    }
}