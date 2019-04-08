package com.ihandilnath.mykitchenapp.ui;

import android.os.Bundle;

import com.google.android.material.textfield.TextInputEditText;
import com.ihandilnath.mykitchenapp.R;
import com.ihandilnath.mykitchenapp.databinding.ActivityProductFormBinding;
import com.ihandilnath.mykitchenapp.model.Product;
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
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_product_form);

        if(mViewModel == null){
            mViewModel = ViewModelProviders.of(this).get(ProductFormViewModel.class);
        }

        if(getIntent().getExtras().containsKey("product")){
            mViewModel.setProduct((Product) getIntent().getExtras().get("product"));
            TextInputEditText tv = findViewById(R.id.productform_edit_name);
            tv.setFocusable(false);
        }

        mDataBinding.setViewmodel(mViewModel);


    }

}
