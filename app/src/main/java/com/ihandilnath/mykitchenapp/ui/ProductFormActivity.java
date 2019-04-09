package com.ihandilnath.mykitchenapp.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import com.ihandilnath.mykitchenapp.R;
import com.ihandilnath.mykitchenapp.databinding.ActivityProductFormBinding;
import com.ihandilnath.mykitchenapp.model.Product;
import com.ihandilnath.mykitchenapp.viewmodel.ProductFormViewModel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

public class ProductFormActivity extends AppCompatActivity {

    ProductFormViewModel mViewModel;
    ProductAction action;
    ActivityProductFormBinding mDataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_product_form);

        if (mViewModel == null) {
            mViewModel = ViewModelProviders.of(this).get(ProductFormViewModel.class);
        }

        action = (ProductAction) getIntent().getExtras().get("action");

        if (action == ProductAction.EDIT_PRODUCT) {
            mViewModel.setProduct((Product) getIntent().getExtras().get("product"));
            TextInputEditText tv = findViewById(R.id.productform_edit_name);
            tv.setFocusable(false);
        }

        mDataBinding.setViewmodel(mViewModel);


    }

    public void submit(View view) {
        mViewModel.submit();
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        if (action == ProductAction.REGISTER_PRODUCT) {
            alertDialogBuilder.setTitle("Registered Product")
                    .setMessage(String.format("Registered details of %s in the Product database. ", mViewModel.getName()))
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ProductFormActivity.this.finish();
                        }
                    })
                    .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ProductFormActivity.this.finish();
                        }
                    });
        } else {
            alertDialogBuilder.setTitle("Updated Product")
                    .setMessage(String.format("Updated details of %s in the Product database.", mViewModel.getName()))
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ProductFormActivity.this.finish();
                        }
                    });
        }
        alertDialogBuilder.show();
    }

}
