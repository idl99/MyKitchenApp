package com.ihandilnath.mykitchenapp.viewmodel;

import android.app.Application;
import android.util.Log;
import android.view.View;

import com.ihandilnath.mykitchenapp.db.Product;
import com.ihandilnath.mykitchenapp.db.ProductRepository;

import androidx.lifecycle.AndroidViewModel;

public class ProductFormViewModel extends AndroidViewModel{

    private ProductRepository repository;
    private Product product;

    public ProductFormViewModel (Application application) {
        super(application);
        repository = new ProductRepository(application);
        product = new Product("",0,0,"", false);
    }

    public String getName(){
        return product.getName();
    }

    public String getWeight(){
        return String.valueOf(product.getWeight());
    }

    public String getPrice(){
        return String.valueOf(product.getPrice());
    }

    public String getDescription(){
        return product.getDescription();
    }

    public void setName(CharSequence s, int start, int before, int count) {
        product.setName(s.toString());
    }

    public void setWeight(CharSequence s, int start, int before, int count){
        String value = s.toString();
        if(value.equals("")){
            product.setWeight(0.0);
        }else{
            product.setWeight(Double.valueOf(value));
        }
    }

    public void setPrice(CharSequence s, int start, int before, int count){
        String value = s.toString();
        if(value.equals("")){
            product.setPrice(0.0);
        }else{
            product.setPrice(Double.valueOf(value));
        }
    }

    public void setDescription(CharSequence s, int start, int before, int count){
        product.setDescription(s.toString());
    }

    public void submit(View view) {
        Log.i("MY_TAG", product.toString());
        repository.insert(product);
    }

}
