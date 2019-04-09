package com.ihandilnath.mykitchenapp.model;

import com.ihandilnath.mykitchenapp.BR;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "product_table")
public class Product extends BaseObservable implements Serializable {

    // Implements Serializable so Product can be passed as an intent extra among activities

    @PrimaryKey
    @NonNull
    private String name;
    private double weight;
    private double price;
    private String description;
    private boolean available;

    public Product(String name, double weight, double price, String description, boolean available) {
        setName(name);
        setWeight(weight);
        setPrice(price);
        setDescription(description);
        setAvailable(available);
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                ", price=" + price +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return this.name.equals(((Product) obj).getName());
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
        notifyPropertyChanged(BR.weight);
    }

    @Bindable
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
        notifyPropertyChanged(BR.price);
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

}
