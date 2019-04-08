package com.ihandilnath.mykitchenapp.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class RecipeListActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        listView = findViewById(R.id.recipelist_list_view);

        List<String> products = getIntent().getExtras().getStringArrayList("productNames");
        initializeView(products);

    }

    public void initializeView(List<String> products){

        RequestQueue queue = Volley.newRequestQueue(this);
        Uri uri = new Uri.Builder().scheme("https")
                .authority("www.food2fork.com")
                .appendPath("api")
                .appendPath("search")
                .appendQueryParameter("key", "d4d6d1af0d6d52dcf90a83a3fb37b513")
                .appendQueryParameter("q", TextUtils.join(",",products).toLowerCase())
                .appendQueryParameter("page","1")
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

    public List<Recipe> parseResponseAsRecipes(JSONObject response){
        final List<Recipe> recipes = new ArrayList<>();
        try {
            JSONArray jsonArray = response.getJSONArray("recipes");
            for(int i =0; i< jsonArray.length(); i++){
                JSONObject object = jsonArray.getJSONObject(i);
                String name = object.getString("title");
                String url = object.getString("source_url");
                Recipe recipe = new Recipe(name, url);
                Log.d("MY_TAG", recipe.toString());
                recipes.add(new Recipe(name, url));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recipes;
    }

    public void populateRecipeList(final List<Recipe> recipes){
        if(recipes.size() == 0){
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("No recipes found")
                    .setMessage("Please try selecting other products")
                    .setNeutralButton("Go back", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .show();
        }else{
            listView.setAdapter(new ArrayAdapter<>(RecipeListActivity.this, android.R.layout.simple_list_item_1, recipes));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(recipes.get(i).getUrl()));
                    startActivity(intent);
                }
            });
        }
    }

}
