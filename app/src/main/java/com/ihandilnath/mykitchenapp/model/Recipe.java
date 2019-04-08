package com.ihandilnath.mykitchenapp.model;

import androidx.annotation.NonNull;

public class Recipe {

    String name;
    String url;

    public Recipe(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return name;
    }
}
