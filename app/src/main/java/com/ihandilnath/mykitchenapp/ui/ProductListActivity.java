package com.ihandilnath.mykitchenapp.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ProductListActivity extends AppCompatActivity {

    private ProductAction action;
    private ProductListViewModel viewmodel;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        action = ((ProductAction) getIntent().getExtras().get("action"));

        if (viewmodel == null) {
            viewmodel = ViewModelProviders.of(this).get(ProductListViewModel.class);
        }

        listView = findViewById(R.id.productlist_list_view);

        LiveData<List<Product>> data;
        switch (action){
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

                switch (action){

                    case ADD_TO_KITCHEN:
                        listView.setAdapter(new CheckedProductAdapter(products,true));
                        break;

                    case EDIT_AVAILABILITY:
                        button.setText("Save");

                        listView.setAdapter(new CheckedProductAdapter(products, false));
                        break;

                    case EDIT_PRODUCT:
                        ((ViewGroup)(button.getParent())).removeView(button);
                        listView.setAdapter(new SimpleProductAdapter(products));
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent = new Intent(ProductListActivity.this, ProductFormActivity.class);
                                intent.putExtra("action", ProductAction.EDIT_PRODUCT);
                                intent.putExtra("product", products.get(i));
                                startActivity(intent);
                                finish();
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
                        listView.setAdapter(new CheckedProductAdapter(products, false));
                        ((CheckedProductAdapter) listView.getAdapter()).uncheckAll();
                        break;

                }

            }

        });

    }

    private void onListAction() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ProductListActivity.this.finish();
                    }
                });

        switch (action){

            case ADD_TO_KITCHEN:
                alertDialogBuilder.setTitle("Added Items to Kitchen")
                        .setMessage("Added items to kitchen");
                break;

            case EDIT_AVAILABILITY:
                alertDialogBuilder.setTitle("Saved Availability")
                        .setMessage("Saved availability of products in database.");
                break;

        }

        alertDialogBuilder.show();

    }

    public void saveToDb(View view) {
        viewmodel.save();
        onListAction();
    }

    public void findRecipes(){
        ArrayList<String> productNames = new ArrayList<>();
        for(Iterator it = ((CheckedProductAdapter) listView.getAdapter()).getCheckedProducts().iterator();
            it.hasNext();){
            Product product = ((Product) it.next());
            productNames.add(product.getName());
        }
        Intent intent = new Intent(this, RecipeListActivity.class);
        intent.putStringArrayListExtra("productNames", productNames);
        startActivity(intent);
    }

    private class SimpleProductAdapter extends BaseAdapter{

        protected List<Product> products;

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

        private boolean disableCheckedItems;

        public CheckedProductAdapter(List<Product> products, boolean disableCheckedItems) {
            super(products);
            this.disableCheckedItems = disableCheckedItems;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            if (view == null) {
                view = getLayoutInflater().inflate(android.R.layout.simple_list_item_multiple_choice,
                        viewGroup, false);
            }

            final Product product = ((Product)getItem(i));
            final CheckedTextView ctv = ((CheckedTextView) view);
            ctv.setText(product.getName());
            ctv.setChecked(product.isAvailable());

            if(disableCheckedItems && ctv.isChecked()){
                ctv.setClickable(false);
            }else{
                ctv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ctv.toggle();
                        product.setAvailable(ctv.isChecked());
                    }
                });
            }

            return view;
        }

        public List<Product> getCheckedProducts(){
            List<Product> checkedProducts = new ArrayList<>();
            for(int i=0; i<getCount(); i++){
                if(((CheckedTextView) listView.getChildAt(i)).isChecked())
                    checkedProducts.add(products.get(i));
            }
            return checkedProducts;
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
