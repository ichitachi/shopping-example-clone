package com.teikaiz.quiz.model;

import java.math.BigDecimal;

public class Products {
    private String title;
    private String description;
    private Double price;
    private String date;
    private String time;
    private Long category;
    private String pid;
    private String image;

    public Products() {
    }


    public Products(String title, String description, Double price, String date, String time, Long category, String pid, String image) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.date = date;
        this.time = time;
        this.category = category;
        this.pid = pid;
        this.image = image;
    }

    public Products(String title, String description, Double price) {
        this.title = title;
        this.description = description;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
