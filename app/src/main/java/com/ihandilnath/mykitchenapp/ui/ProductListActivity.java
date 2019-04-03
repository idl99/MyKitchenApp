package com.ihandilnath.mykitchenapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.ihandilnath.mykitchenapp.R;
import com.ihandilnath.mykitchenapp.db.Product;
import com.ihandilnath.mykitchenapp.db.ProductRepository;
import com.ihandilnath.mykitchenapp.viewmodel.ProductListViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductListViewModel viewmodel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        recyclerView = (RecyclerView) findViewById(R.id.productlist_recycler_view);
        // improves performance of the Recycler View
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        if (viewmodel == null) {
            viewmodel = ViewModelProviders.of(this).get(ProductListViewModel.class);
        }

        viewmodel.getProducts().observe(this, new Observer<List<Product>>() {

            @Override
            public void onChanged(List<Product> products) {

                // refresh recycler view data
                recyclerView.setAdapter(new ProductAdapter(products, viewmodel.getSelected(),
                        new ProductAdapter.OnProductCheckListener() {
                    @Override
                    public void onProductCheck(Product product) {
                        Log.i("MY_TAG", "Product added to selection");
                        ProductListActivity.this.viewmodel.addCheckedItem(product);
                    }

                    @Override
                    public void onProductUncheck(Product product) {
                        Log.i("MY_TAG", "Product removed from selection");
                        ProductListActivity.this.viewmodel.removeCheckedItem(product);
                    }
                }));

            }
        });

    }

}
