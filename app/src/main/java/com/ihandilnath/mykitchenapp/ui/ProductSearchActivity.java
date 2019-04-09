package com.ihandilnath.mykitchenapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.textfield.TextInputEditText;
import com.ihandilnath.mykitchenapp.R;
import com.ihandilnath.mykitchenapp.db.ProductRepository;
import com.ihandilnath.mykitchenapp.model.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

public class ProductSearchActivity extends AppCompatActivity {

    private List<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_search);

        new ProductRepository(getApplication()).getAllProducts().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                ProductSearchActivity.this.products = products;
            }
        });

    }

    public void lookup(View view) {

        ListView listView = findViewById(R.id.productsearch_list_view);
        TextInputEditText editQuery = findViewById(R.id.productsearch_edit_query);
        String query = editQuery.getText().toString().toLowerCase();

        List <String> filteredProductList = new ArrayList<>();
        Iterator it = products.iterator();
        while(it.hasNext()){
            Product product = ((Product) it.next());
            if (product.getName().toLowerCase().contains(query) || product.getDescription().toLowerCase().  contains(query)) {
                filteredProductList.add(product.getName());
            }
        }
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                filteredProductList));
    }

}
