package com.ihandilnath.mykitchenapp.viewmodel;

import android.app.Application;

import com.ihandilnath.mykitchenapp.db.ProductRepository;
import com.ihandilnath.mykitchenapp.model.Product;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


public class ProductListViewModel extends AndroidViewModel {

    private final ProductRepository repository;
    private LiveData<List<Product>> products;

    public ProductListViewModel(@NonNull Application application) {
        super(application);
        this.repository = new ProductRepository(application);
        this.products = repository.getAllProducts();
    }

    public LiveData<List<Product>> getProducts() {
        return this.products;
    }

    public LiveData<List<Product>> getAvailableProducts() {
        this.products = repository.getAvailableProducts();
        return this.products;
    }

    public void save() {
        for (Product product : products.getValue()) {
            repository.update(product);
        }
    }

}
