package com.ihandilnath.mykitchenapp.viewmodel;

import android.app.Application;
import android.util.Log;
import android.view.View;

import com.ihandilnath.mykitchenapp.db.Product;
import com.ihandilnath.mykitchenapp.db.ProductRepository;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class ProductFormViewModel extends AndroidViewModel{

    private ProductRepository mRepository;
    private MutableLiveData<String> mName;
    private MutableLiveData<Double> mWeight;
    private MutableLiveData<Double> mPrice;
    private MutableLiveData<String> mDescription;

    public ProductFormViewModel (Application application) {
        super(application);
        mRepository = new ProductRepository(application);
        mName = new MutableLiveData<>();
        mWeight = new MutableLiveData<>();
        mPrice = new MutableLiveData<>();
        mDescription = new MutableLiveData<>();
    }

    public void setName(CharSequence s, int start, int before, int count) {
        mName.postValue(s.toString());
    }

    public void setWeight(CharSequence s, int start, int before, int count){
        mWeight.postValue(Double.valueOf(s.toString()));
    }

    public void setPrice(CharSequence s, int start, int before, int count){
        mPrice.postValue(Double.valueOf(s.toString()));
    }

    public void setDescription(CharSequence s, int start, int before, int count){
        mDescription.postValue(s.toString());
    }

    public void submit(View view) {
        Product product = new Product(mName.getValue(), mWeight.getValue(), mPrice.getValue(), mDescription.getValue());
        Log.i("MY_TAG", product.toString());
        mRepository.insert(product);
    }

}
