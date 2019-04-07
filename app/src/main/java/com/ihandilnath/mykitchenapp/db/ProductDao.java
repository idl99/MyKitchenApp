package com.ihandilnath.mykitchenapp.db;

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Product product);

    @Update
    void update(Product product);

    @Query("DELETE FROM product_table")
    void deleteAll();

}