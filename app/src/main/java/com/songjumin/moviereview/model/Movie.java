package com.songjumin.moviereview.model;

public class Movie {
    private int id;
    private int vote_count;
    private Double vote_average;
    private String title;
    private String original_title;
    private String release_date;
    private String overview;
    private String poster_path;
    private int is_favorite;


    public Movie(String title, String original_title, String release_date, String poster_path) {
        this.title = title;
        this.original_title = original_title;
        this.release_date = release_date;
        this.poster_path = poster_path;
    }

    public Movie(int id, String title, String original_title, String release_date, String overview, String poster_path, int is_favorite) {
        this.id = id;
        this.title = title;
        this.original_title = original_title;
        this.release_date = release_date;
        this.overview = overview;
        this.poster_path = poster_path;
        this.is_favorite = is_favorite;
    }

    public Movie(int id, int vote_count, Double vote_average, String title, String original_title, String release_date, String overview, String poster_path) {
        this.id = id;
        this.vote_count = vote_count;
        this.vote_average = vote_average;
        this.title = title;
        this.original_title = original_title;
        this.release_date = release_date;
        this.overview = overview;
        this.poster_path = poster_path;
    }

    public Movie(int id, int vote_count, Double vote_average, String title, String original_title, String release_date, String overview, String poster_path, int is_favorite) {
        this.id = id;
        this.vote_count = vote_count;
        this.vote_average = vote_average;
        this.title = title;
        this.original_title = original_title;
        this.release_date = release_date;
        this.overview = overview;
        this.poster_path = poster_path;
        this.is_favorite = is_favorite;
    }

    public Movie() {
    }

    public int getId() {
        return id;
    }

    public void setMovie_id(int Id) {
        this.id = id;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public Double getVote_average() {
        return vote_average;
    }

    public void setVote_average(Double vote_average) {
        this.vote_average = vote_average;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(int is_favorite) {
        this.is_favorite = is_favorite;
    }
}
