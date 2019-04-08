package com.ihandilnath.mykitchenapp.db;

import com.ihandilnath.mykitchenapp.model.Product;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ProductDao {

    @Query("SELECT * from product_table ORDER BY name ASC")
    LiveData<List<Product>> getAllProducts();

    @Query("SELECT * from product_table WHERE available = 1 ORDER BY name ASC")
    LiveData<List<Product>> getAvailableProducts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Product product);

    @Update
    void update(Product product);

    @Query("DELETE FROM product_table")
    void deleteAll();

}