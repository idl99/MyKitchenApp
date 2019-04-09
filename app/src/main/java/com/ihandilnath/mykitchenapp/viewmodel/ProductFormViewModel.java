package com.ihandilnath.mykitchenapp.viewmodel;

import android.app.Application;

import com.ihandilnath.mykitchenapp.db.ProductRepository;
import com.ihandilnath.mykitchenapp.model.Product;

import androidx.lifecycle.AndroidViewModel;

/**
 * ViewModel class which stores the state of ProductFormActivity
 */
public class ProductFormViewModel extends AndroidViewModel {

    private final ProductRepository mRepository;
    private Product mProduct; // Product for which details are being entered to either register or update in database

    public ProductFormViewModel(Application application) {
        super(application);
        mRepository = new ProductRepository(application);
        mProduct = new Product("", 0, 0, "", false);
    }

    public Product getProduct() {
        return mProduct;
    }

    public String getName() {
        return mProduct.getName();
    }

    public String getWeight() {
        return String.valueOf(mProduct.getWeight());
    }

    public String getPrice() {
        return String.valueOf(mProduct.getPrice());
    }

    public String getDescription() {
        return mProduct.getDescription();
    }

    public void setProduct(Product product) {
        this.mProduct = product;
    }

    public void setName(CharSequence s, int start, int before, int count) {
        mProduct.setName(s.toString());
    }

    public void setWeight(CharSequence s, int start, int before, int count) {
        String value = s.toString();
        if (value.equals("")) {
            mProduct.setWeight(0.0);
        } else {
            mProduct.setWeight(Double.valueOf(value));
        }
    }

    public void setPrice(CharSequence s, int start, int before, int count) {
        String value = s.toString();
        if (value.equals("")) {
            mProduct.setPrice(0.0);
        } else {
            mProduct.setPrice(Double.valueOf(value));
        }
    }

    public void setDescription(CharSequence s, int start, int before, int count) {
        mProduct.setDescription(s.toString());
    }

    public void submit() {
        mRepository.insert(mProduct);
    }

}
