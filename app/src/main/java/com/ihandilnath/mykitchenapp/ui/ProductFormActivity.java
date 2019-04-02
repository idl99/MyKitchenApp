package com.ihandilnath.mykitchenapp.ui;

import android.os.Bundle;

import com.ihandilnath.mykitchenapp.R;
import com.ihandilnath.mykitchenapp.databinding.ActivityProductFormBinding;
import com.ihandilnath.mykitchenapp.viewmodel.ProductFormViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

public class ProductFormActivity extends AppCompatActivity {

    ProductFormViewModel mViewModel;
    ActivityProductFormBinding mDataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(mViewModel == null){
            mViewModel = ViewModelProviders.of(this).get(ProductFormViewModel.class);
        }

        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_product_form);
        mDataBinding.setViewmodel(mViewModel);
    }

}
