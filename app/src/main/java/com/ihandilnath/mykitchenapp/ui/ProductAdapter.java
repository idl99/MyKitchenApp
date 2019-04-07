package com.ihandilnath.mykitchenapp.ui;

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
    private List<Product> checked;

    public ProductAdapter(List<Product> products, List<Product> checked) {
        this.products = products;
        this.checked = checked;
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
    public void onBindViewHolder(@NonNull ProductViewHolder holder, final int position) {

        holder.ctv.setText(products.get(position).getName());
        final Product currentProduct = products.get(position);

        if(checked.contains(currentProduct)){
            holder.ctv.setChecked(true);
        }

        holder.ctv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                CheckedTextView ctv = ((CheckedTextView) v);
                ctv.toggle();
                if(ctv.isChecked()){
                    checked.add(products.get(position));
                }else{
                    checked.remove(products.get(position));
                }
            }
        });
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
