package com.ihandilnath.mykitchenapp.viewmodel;

import android.app.Application;

import com.ihandilnath.mykitchenapp.db.ProductRepository;
import com.ihandilnath.mykitchenapp.model.Product;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

/**
 * ViewModel class which stores state of the ProductListActivity
 */
public class ProductListViewModel extends AndroidViewModel {

    private final ProductRepository mRepository;
    private LiveData<List<Product>> mProducts; // List of products that can be observed and used by activity

    public ProductListViewModel(@NonNull Application application) {
        super(application);
        this.mRepository = new ProductRepository(application);
        this.mProducts = mRepository.getAllProducts();
    }

    public LiveData<List<Product>> getProducts() {
        return this.mProducts;
    }

    public LiveData<List<Product>> getAvailableProducts() {
        this.mProducts = mRepository.getAvailableProducts();
        return this.mProducts;
    }

    public void save() {
        for (Product product : mProducts.getValue()) {
            mRepository.update(product);
        }
    }

}
