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

/**
 * Activity which provides an interface for user to enter and edit product details
 */
public class ProductFormActivity extends AppCompatActivity {

    private ProductFormViewModel mViewModel; // View model to retain view state
    private ProductAction mAction; // Type of action which user intends to perform with Product Form
                                // either register new product or update details of existing product

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityProductFormBinding mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_product_form);

        if (mViewModel == null) {
            mViewModel = ViewModelProviders.of(this).get(ProductFormViewModel.class);
        }

        mAction = (ProductAction) getIntent().getExtras().get("action");

        if (mAction == ProductAction.EDIT_PRODUCT) {
            // User wishes to edit an existing product. Therefore load existing details of the product
            // onto the interface
            mViewModel.setProduct((Product) getIntent().getExtras().get("product"));
            TextInputEditText tv = findViewById(R.id.productform_edit_name);
            tv.setFocusable(false);
        }

        mDataBinding.setViewmodel(mViewModel);


    }

    public void submit(View view) {
        Product product = mViewModel.getProduct();
        if(product.getName().equals("")){
            // User is attempting to register a product with an empty name
            new AlertDialog.Builder(this).
                setTitle("Name must not be empty")
                .setMessage("Please provide a name for the product.").show();
        }else{
            mViewModel.submit();
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            if (mAction == ProductAction.REGISTER_PRODUCT) {
                // User performed action of registering product
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
                // User performed action of updating product
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

}
