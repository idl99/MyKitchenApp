package com.ihandilnath.mykitchenapp.viewmodel;

import android.app.Application;

import com.ihandilnath.mykitchenapp.db.Product;
import com.ihandilnath.mykitchenapp.db.ProductRepository;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


public class ProductListViewModel extends AndroidViewModel {

    private ProductRepository repository;
    private LiveData<List<Product>> products;
    private List<Product> selected;

    public ProductListViewModel(@NonNull Application application) {
        super(application);
        this.repository = new ProductRepository(application);
        this.products = repository.getAllProducts();
        this.selected = new ArrayList<>();
    }

    public LiveData<List<Product>> getProducts() {
        return this.products;
    }

    public List<Product> getSelected() {
        return selected;
    }

    public void save(){
        for(Product product: products.getValue()){
            if(selected.contains(product)){
                product.setAvailable(true);
            }else{
                product.setAvailable(false);
            }
            repository.update(product);
        }
    }
}
