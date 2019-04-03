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
    private List<Product> selected;
    private OnProductCheckListener onProductCheckListener;

    public ProductAdapter(List<Product> products, List<Product> selected, OnProductCheckListener onProductCheckListener ) {
        this.products = products;
        this.selected = selected;
        this.onProductCheckListener = onProductCheckListener;
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
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        holder.ctv.setText(products.get(position).getName());
        final Product currentProduct = products.get(position);

        if(selected.contains(currentProduct)){
            holder.ctv.setChecked(true);
        }

        holder.ctv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                CheckedTextView ctv = ((CheckedTextView) v);
                ctv.toggle();
                if(ctv.isChecked()){
                    onProductCheckListener.onProductCheck(currentProduct);
                }else{
                    onProductCheckListener.onProductUncheck(currentProduct);
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

    public interface OnProductCheckListener {

        void onProductCheck(Product product);

        void onProductUncheck(Product product);

    }
}
