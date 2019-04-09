package com.ihandilnath.mykitchenapp.ui;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.ihandilnath.mykitchenapp.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void registerProduct(View view) {
        Intent intent = new Intent(this, ProductFormActivity.class);
        intent.putExtra("action", "register");
        startActivity(intent);
    }

    public void displayProducts(View view) {
        Intent intent = new Intent(this, ProductListActivity.class);
        intent.putExtra("action", ProductAction.ADD_TO_KITCHEN);
        startActivity(intent);
    }

    public void checkAvailability(View view) {
        Intent intent = new Intent(this, ProductListActivity.class);
        intent.putExtra("action", ProductAction.EDIT_AVAILABILITY);
        startActivity(intent);
    }

    public void editProduct(View view) {
        Intent intent = new Intent(this, ProductListActivity.class);
        intent.putExtra("action", ProductAction.EDIT_PRODUCT);
        startActivity(intent);
    }

    public void search(View view) {
        Intent intent = new Intent(this, ProductSearchActivity.class);
        startActivity(intent);
    }


    public void findRecipes(View view) {
        Intent intent = new Intent(this, ProductListActivity.class);
        intent.putExtra("action", ProductAction.FIND_RECIPES);
        startActivity(intent);
    }

}
