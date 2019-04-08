package com.ihandilnath.mykitchenapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.ihandilnath.mykitchenapp.R;
import com.ihandilnath.mykitchenapp.model.Product;
import com.ihandilnath.mykitchenapp.viewmodel.ProductListViewModel;

import java.util.List;


public class ProductListActivity extends AppCompatActivity {

    private ListView listView;
    private ProductListViewModel viewmodel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        if (viewmodel == null) {
            viewmodel = ViewModelProviders.of(this).get(ProductListViewModel.class);
        }

        listView = findViewById(R.id.productlist_list_view);

        LiveData<List<Product>> data;
        switch (((ProductAction) getIntent().getExtras().get("action"))){
            case EDIT_AVAILABILITY:
            case FIND_RECIPES:
                data = viewmodel.getAvailableProducts();
                break;

            default:
                data = viewmodel.getProducts();
                break;
        }

        final Button button = findViewById(R.id.productlist_action);
        data.observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(final List<Product> products) {

                switch (((ProductAction) getIntent().getExtras().get("action"))){

                    case ADD_TO_KITCHEN:
                        listView.setAdapter(new CheckedProductAdapter(products));
                        break;

                    case EDIT_AVAILABILITY:
                        button.setText("Save");
                        listView.setAdapter(new CheckedProductAdapter(products));
                        break;

                    case EDIT_PRODUCT:
                        ((ViewGroup)(button.getParent())).removeView(button);
                        listView.setAdapter(new SimpleProductAdapter(products));
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent = new Intent(ProductListActivity.this, ProductFormActivity.class);
                                intent.putExtra("product", products.get(i));
                                startActivity(intent);
                            }
                        });
                        break;

                    case FIND_RECIPES:
                        button.setText("Find Recipes");
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ProductListActivity.this.findRecipes();
                            }
                        });
                        listView.setAdapter(new CheckedProductAdapter(products));
                        ((CheckedProductAdapter) listView.getAdapter()).uncheckAll();
                        break;

                }

            }

        });

    }

    public void saveToDb(View view) {
        viewmodel.save();
        finish();
    }

    public void findRecipes(){
        // code to find recipes
    }

    private class SimpleProductAdapter extends BaseAdapter{

        private List<Product> products;

        public SimpleProductAdapter(List<Product> products) {
            super();
            this.products = products;
        }

        @Override
        public int getCount() {
            return products.size();
        }

        @Override
        public Object getItem(int i) {
            return products.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = getLayoutInflater().inflate(android.R.layout.simple_list_item_1,
                        viewGroup, false);
            }

            TextView tv = view.findViewById(android.R.id.text1);
            tv.setText(((Product)getItem(i)).getName());

            return view;
        }
    }

    private class CheckedProductAdapter extends SimpleProductAdapter{

        public CheckedProductAdapter(List<Product> products) {
            super(products);
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            if (view == null) {
                view = getLayoutInflater().inflate(android.R.layout.simple_list_item_multiple_choice,
                        viewGroup, false);
            }

            final Product product = ((Product)getItem(i));
            final CheckedTextView ctv = view.findViewById(android.R.id.text1);
            ctv.setText(product.getName());
            ctv.setChecked(product.isAvailable());

            ctv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ctv.toggle();
                    product.setAvailable(ctv.isChecked());
                }
            });

            return view;
        }

        public void uncheckAll() {
            if(listView.getChoiceMode() != AbsListView.CHOICE_MODE_MULTIPLE){
                listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
            }
            for(int i=0; i<getCount(); i++){
                listView.setItemChecked(i, false);
            }
        }

    }

}
