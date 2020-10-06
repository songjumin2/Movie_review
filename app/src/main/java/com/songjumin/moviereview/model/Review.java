package com.songjumin.moviereview.model;

public class Review {

    private int reply_id;
    private String title;
    private String email;
    private String content;
    private int rating;
    private String created_at;

    public Review(int reply_id, String title, String email, String content, int rating, String created_at) {
        this.reply_id = reply_id;
        this.title = title;
        this.email = email;
        this.content = content;
        this.rating = rating;
        this.created_at = created_at;
    }

    public Review(String content, int rating) {
        this.content = content;
        this.rating = rating;
    }

    public Review(){
    }


    public int getReply_id() {
        return reply_id;
    }

    public void setReply_id(int reply_id) {
        this.reply_id = reply_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
