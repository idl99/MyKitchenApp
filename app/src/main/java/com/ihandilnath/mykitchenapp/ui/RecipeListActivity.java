package com.ihandilnath.mykitchenapp.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ihandilnath.mykitchenapp.R;
import com.ihandilnath.mykitchenapp.model.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity which provides user with an interface to view list of recipes fetched from Food2Fork API
 * and navigate to browser to view recipe.
 */
public class RecipeListActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        listView = findViewById(R.id.recipelist_list_view);

        List<String> products = getIntent().getExtras().getStringArrayList("productNames");

        if (isConnectedToInternet()) {
            callFood2ForkService(products);
        } else {
            // Not connected to internet, show alert dialog to user
            new AlertDialog.Builder(this)
                    .setTitle("No Internet Connectivity")
                    .setMessage("Please connect to a WiFi or Mobile Data network and try again")
                    .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                            finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .show();
        }

    }

    /**
     * Method to check internet connectivity
     * @return true if connected to internet, false otherwise.
     */
    private boolean isConnectedToInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    /**
     * Method which initializes view
     * @param products
     */
    private void callFood2ForkService(List<String> products) {

        RequestQueue queue = Volley.newRequestQueue(this);
        Uri uri = new Uri.Builder().scheme("https")
                .authority("www.food2fork.com")
                .appendPath("api")
                .appendPath("search")
                .appendQueryParameter("key", "<INSERT FOOD2FORK API KEY HERE>")
                .appendQueryParameter("q", TextUtils.join(",", products).toLowerCase())
                .appendQueryParameter("page", "1")
                .build();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, uri.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ProgressBar progressBar = findViewById(R.id.recipelist_list_progressbar);
                        ((ViewGroup) progressBar.getParent()).removeView(progressBar);
                        List<Recipe> recipes = parseResponseAsRecipes(response);
                        populateRecipeList(recipes);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        queue.add(jsonObjectRequest);

    }

    /**
     * Method which parses response returned by Food2Fork API as list of Recipe objects
     * @param response response returned by Food2Fork API
     * @return list of Recipe objects
     */
    private List<Recipe> parseResponseAsRecipes(JSONObject response) {
        final List<Recipe> recipes = new ArrayList<>();
        try {
            JSONArray jsonArray = response.getJSONArray("recipes");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String name = object.getString("title");
                String url = object.getString("source_url");
                recipes.add(new Recipe(name, url));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recipes;
    }

    /**
     * Method which populates the recipe list with recipes fetched from the Food2Fork API
     * @param recipes
     */
    private void populateRecipeList(final List<Recipe> recipes) {
        if (recipes.size() == 0) {
            // No recipes found on Food2Fork for list of products selected by user
            new AlertDialog.Builder(this)
                    .setTitle("No recipes found")
                    .setMessage("No recipes available containing these ingredients, try different\n" +
                            "ingredients")
                    .setNegativeButton("Go back", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .show();
        } else {
            listView.setAdapter(new ArrayAdapter<>(RecipeListActivity.this, android.R.layout.simple_list_item_1, recipes));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    // Intent to visit source webpage of recipe on device web browser
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(recipes.get(i).getUrl()));
                    startActivity(intent);
                }
            });
        }
    }

}
