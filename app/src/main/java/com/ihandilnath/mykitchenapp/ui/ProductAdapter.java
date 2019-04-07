package com.ihandilnath.mykitchenapp.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.ihandilnath.mykitchenapp.db.Product;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> products;
    private ProductAction action;

    public ProductAdapter(List<Product> products, ProductAction action) {
        this.products = products;
        this.action = action;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CheckedTextView ctv = (CheckedTextView) LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_multiple_choice, parent, false);
        ProductViewHolder pvh = new ProductViewHolder(ctv);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder holder, final int position) {

        final Product product = products.get(position);
        holder.ctv.setText(product.getName());

        switch (action){

            case LIST_PRODUCTS:
                holder.ctv.setCheckMarkDrawable(null);
                holder.ctv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Context context = view.getContext();
                        Intent intent = new Intent(context, ProductFormActivity.class);
                        intent.putExtra("product", products.get(position));
                        context.startActivity(intent);
                    }
                });
                break;

            default:
                holder.ctv.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v)
                    {
                        CheckedTextView ctv = ((CheckedTextView) v);
                        ctv.toggle();
                        if(ctv.isChecked()){
                            product.setAvailable(true);
                        }else{
                            product.setAvailable(false);
                        }
                    }
                });

                if(product.isAvailable()){
                    holder.ctv.setChecked(true);
                    if(action == ProductAction.ADD_TO_KITCHEN){
                        holder.ctv.setEnabled(false);
                    }
                }

                break;

        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder{
        public CheckedTextView ctv;

        public ProductViewHolder(CheckedTextView ctv) {
            super(ctv);
            this.ctv = ctv;
        }
    }

}
