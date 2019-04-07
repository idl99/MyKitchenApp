package com.ihandilnath.mykitchenapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.ihandilnath.mykitchenapp.R;
import com.ihandilnath.mykitchenapp.db.Product;
import com.ihandilnath.mykitchenapp.viewmodel.ProductListViewModel;

import java.util.Iterator;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductListViewModel viewmodel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        recyclerView = findViewById(R.id.productlist_recycler_view);
        // improves performance of the Recycler View
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        if (viewmodel == null) {
            viewmodel = ViewModelProviders.of(this).get(ProductListViewModel.class);
        }

        if(getIntent().getExtras().getBoolean("filterAvailable")){
            viewmodel.getProducts().observe(this, new Observer<List<Product>>() {
                @Override
                public void onChanged(List<Product> products) {
                    for(Iterator it = products.iterator(); it.hasNext();){
                        Product product = ((Product) it.next());
                        if(!product.isAvailable()){
                            it.remove();
                        }
                    }
                    recyclerView.setAdapter(new ProductAdapter(products, viewmodel.getSelected()));
                }
            });
        } else{
            viewmodel.getProducts().observe(this, new Observer<List<Product>>() {
                @Override
                public void onChanged(List<Product> products) {
                    recyclerView.setAdapter(new ProductAdapter(products, viewmodel.getSelected()));
                }
            });
        }



    }

    public void submit(View view) {
        viewmodel.save();
        finish();
    }

}
