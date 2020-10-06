package com.songjumin.moviereview.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewRes {

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("items")
    @Expose
    private List<Review> items = null;

    @SerializedName("cnt")
    @Expose
    private Integer cnt;


    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Review> getItems() {
        return items;
    }

    public void setItems(List<Review> items) {
        this.items = items;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }
}
