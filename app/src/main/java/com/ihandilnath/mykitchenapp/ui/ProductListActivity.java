package com.ihandilnath.mykitchenapp.ui;

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
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


/**
 * Activity which provides an interface for user to view a list of products and interact with it
 * before performing a batch operation on several products
 */
public class ProductListActivity extends AppCompatActivity {

    private ProductAction mAction;
    private ProductListViewModel mViewModel;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        mAction = ((ProductAction) getIntent().getExtras().get("action")); // Getting the action
                                                // which the user intends to perform on the product
                                                // in this activity

        if (mViewModel == null) {
            mViewModel = ViewModelProviders.of(this).get(ProductListViewModel.class);
        }

        mListView = findViewById(R.id.productlist_list_view);

        // Fetching required data from database
        LiveData<List<Product>> data;
        switch (mAction) {
            case EDIT_AVAILABILITY:
            case FIND_RECIPES:
                data = mViewModel.getAvailableProducts();
                break;

            default:
                data = mViewModel.getProducts();
                break;
        }

        // Initializing list adapter with fetched data and other view initializations
        final Button button = findViewById(R.id.productlist_action);
        data.observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(final List<Product> products) {

                switch (mAction) {

                    case ADD_TO_KITCHEN:
                        mListView.setAdapter(new CheckedProductAdapter(products, true));
                        break;

                    case EDIT_AVAILABILITY:
                        button.setText("Save");
                        mListView.setAdapter(new CheckedProductAdapter(products, false));
                        break;

                    case EDIT_PRODUCT:
                        // Removing action button
                        ((ViewGroup) (button.getParent())).removeView(button);
                        mListView.setAdapter(new SimpleProductAdapter(products));

                        // When user clicks on a product, start ProductFormActivity to edit details of the product
                        // passed as an extra to the ProductFormActivity.
                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                        mListView.setAdapter(new CheckedProductAdapter(products, false));
                        ((CheckedProductAdapter) mListView.getAdapter()).uncheckAll();
                        break;

                }

            }

        });

    }

    /**
     * Method called when user has performed an action. Displays an alert dialog to user indicating
     * completion or success of operation.
     */
    private void onListAction() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ProductListActivity.this.finish();
                    }
                });

        switch (mAction) {

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
        mViewModel.save();
        onListAction();
    }

    /**
     * Method which begins the RecipeListActivity by sending list of names of products for which the
     * user wishes to find recipes.
     */
    private void findRecipes() {
        ArrayList<String> productNames = new ArrayList<>();
        for (Product product : ((CheckedProductAdapter) mListView.getAdapter()).getCheckedProducts()) {
            productNames.add(product.getName());
        }
        Intent intent = new Intent(this, RecipeListActivity.class);
        intent.putStringArrayListExtra("productNames", productNames);
        startActivity(intent);
    }

    /**
     * Custom adapter class to show simple list of Products
     */
    private class SimpleProductAdapter extends BaseAdapter {

        final List<Product> products;

        SimpleProductAdapter(List<Product> products) {
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
            tv.setText(((Product) getItem(i)).getName());

            return view;
        }
    }

    /**
     * Custom adapter class to show a list of Products with checkbox actions
     */
    private class CheckedProductAdapter extends SimpleProductAdapter {

        private final boolean disableCheckedItems;

        CheckedProductAdapter(List<Product> products, boolean disableCheckedItems) {
            super(products);
            this.disableCheckedItems = disableCheckedItems;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            if (view == null) {
                view = getLayoutInflater().inflate(android.R.layout.simple_list_item_multiple_choice,
                        viewGroup, false);
            }

            final Product product = ((Product) getItem(i));
            final CheckedTextView ctv = ((CheckedTextView) view);
            ctv.setText(product.getName());
            ctv.setChecked(product.isAvailable());

            if (disableCheckedItems && ctv.isChecked()) {
                // Item is available and the availability shouldn't be modified
                ctv.setClickable(false);
            } else {
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

        /**
         * Method which returns the list of Products checked by the users
         * @return list of Products checked by the users
         */
        List<Product> getCheckedProducts() {
            List<Product> checkedProducts = new ArrayList<>();
            for (int i = 0; i < getCount(); i++) {
                if (((CheckedTextView) mListView.getChildAt(i)).isChecked())
                    checkedProducts.add(products.get(i));
            }
            return checkedProducts;
        }

        /**
         * Uncheck all checked boxes in the list view
         */
        void uncheckAll() {
            if (mListView.getChoiceMode() != AbsListView.CHOICE_MODE_MULTIPLE) {
                mListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
            }
            for (int i = 0; i < getCount(); i++) {
                mListView.setItemChecked(i, false);
            }
        }

    }

}
