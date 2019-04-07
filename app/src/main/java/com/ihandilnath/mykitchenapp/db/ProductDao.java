package com.ihandilnath.mykitchenapp.db;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface ProductDao {

    @Query("SELECT * from product_table ORDER BY name ASC")
    LiveData<List<Product>> getAllProducts();

    @Insert
    void insert(Product product);

    @Query("DELETE FROM product_table")
    void deleteAll();

}