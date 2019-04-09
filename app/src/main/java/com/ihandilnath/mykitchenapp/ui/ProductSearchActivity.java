package com.ihandilnath.mykitchenapp.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.textfield.TextInputEditText;
import com.ihandilnath.mykitchenapp.R;
import com.ihandilnath.mykitchenapp.db.ProductRepository;
import com.ihandilnath.mykitchenapp.model.Product;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

/**
 * Activity which provides the user with an interface to search for Products by providing a query string
 */
public class ProductSearchActivity extends AppCompatActivity {

    private List<Product> mProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_search);

        new ProductRepository(getApplication()).getAllProducts().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                ProductSearchActivity.this.mProducts = products;
            }
        });

    }

    /**
     * Performs business logic of searching for products, and refreshes the List View
     * by setting new adapter containing new list of filtered products
     * @param view - the Button whose onClick event fires this method
     */
    public void lookup(View view) {

        ListView listView = findViewById(R.id.productsearch_list_view);

        // Getting the user query
        TextInputEditText editQuery = findViewById(R.id.productsearch_edit_query);
        String query = editQuery.getText().toString().toLowerCase();

        List<String> filteredProductList = new ArrayList<>();
        for (Product product : mProducts) {
            // If product name or product description contains query as subsequence
            if (product.getName().toLowerCase().contains(query) || product.getDescription().toLowerCase().contains(query)) {
                filteredProductList.add(product.getName());
            }
        }
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                filteredProductList));
    }

}
