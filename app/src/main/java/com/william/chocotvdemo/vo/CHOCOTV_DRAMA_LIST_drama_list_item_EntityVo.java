package com.william.chocotvdemo.vo;

import java.io.Serializable;

/**
 * Created by William on 2016/5/4.
 */
public class CHOCOTV_DRAMA_LIST_drama_list_item_EntityVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private int drama_id;
    private String name;
    private int total_views;
    private String created_at;
    private String thumb;
    private float rating;

    public int getDrama_id() {
        return drama_id;
    }

    public void setDrama_id(int drama_id) {
        this.drama_id = drama_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotal_views() {
        return total_views;
    }

    public void setTotal_views(int total_views) {
        this.total_views = total_views;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
