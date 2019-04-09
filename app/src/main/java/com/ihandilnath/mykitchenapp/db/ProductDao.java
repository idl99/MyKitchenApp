package com.ihandilnath.mykitchenapp.db;

import com.ihandilnath.mykitchenapp.model.Product;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

/**
 *  Data Access Object interface with Room annotations as an abstraction layer over raw SQL queries
 */
@Dao
public interface ProductDao {

    /**
     * Get all products from database
     * @return - list of Product that can be observed for changes
     */
    @Query("SELECT * from product_table ORDER BY name ASC")
    LiveData<List<Product>> getAllProducts();

    /**
     * Gets only available products from database
     * @return - list of available Product that can be observed for changes
     */
    @Query("SELECT * from product_table WHERE available = 1 ORDER BY name ASC")
    LiveData<List<Product>> getAvailableProducts();

    /**
     * Insert a product into database
     * @param product - Product to be inserted into database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Product product);

    /**
     * Update an existing product in database
     * @param product - Product to be updated in database
     */
    @Update
    void update(Product product);

}