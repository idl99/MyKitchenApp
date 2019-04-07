package com.ihandilnath.mykitchenapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ihandilnath.mykitchenapp.R;
import com.ihandilnath.mykitchenapp.db.Product;
import com.ihandilnath.mykitchenapp.viewmodel.ProductListViewModel;

import java.util.Iterator;
import java.util.List;

import static com.ihandilnath.mykitchenapp.ui.ProductAction.ADD_TO_KITCHEN;
import static com.ihandilnath.mykitchenapp.ui.ProductAction.LIST_PRODUCTS;

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

        switch ((ProductAction)getIntent().getExtras().get("action")){

            case ADD_TO_KITCHEN:
                viewmodel.getProducts().observe(this, new Observer<List<Product>>() {
                    @Override
                    public void onChanged(List<Product> products) {
                        recyclerView.setAdapter(new ProductAdapter(products, ADD_TO_KITCHEN));
                    }
                });
                break;

            case EDIT_AVAILABILITY:
                viewmodel.getProducts().observe(this, new Observer<List<Product>>() {
                    @Override
                    public void onChanged(List<Product> products) {
                        for(Iterator it = products.iterator(); it.hasNext();){
                            Product product = ((Product) it.next());
                            if(!product.isAvailable()){
                                it.remove();
                            }
                        }
                        recyclerView.setAdapter(new ProductAdapter(products, ProductAction.EDIT_AVAILABILITY));
                    }
                });
                break;

            case LIST_PRODUCTS:

                Button button = findViewById(R.id.productlist_add);
                button.setEnabled(false);
                button.setVisibility(View.INVISIBLE);

                viewmodel.getProducts().observe(this, new Observer<List<Product>>() {
                    @Override
                    public void onChanged(final List<Product> products) {
                        recyclerView.setAdapter(new ProductAdapter(products, LIST_PRODUCTS));
                    }
                });
                break;

        }

    }

    public void submit(View view) {
        viewmodel.save();
        finish();
    }

}
