package com.songjumin.moviereview.model;

import retrofit2.http.PUT;

public class Favorite {

    int id;
    int favorite_id;
    String title;
    String release_date;
    String poster_path;


    public Favorite(int id, int favorite_id, String title, String release_date, String poster_path) {
        this.id = id;
        this.favorite_id = favorite_id;
        this.title = title;
        this.release_date = release_date;
        this.poster_path = poster_path;
    }

    public Favorite(){
    }

    public int getMovie_id() {
        return id;
    }

    public void setMovie_id(int id) {
        this.id = id;
    }

    public int getFavorite_id() {
        return favorite_id;
    }

    public void setFavorite_id(int favorite_id) {
        this.favorite_id = favorite_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }
}
