package com.ihandilnath.mykitchenapp.db;

import android.app.Application;
import android.os.AsyncTask;

import com.ihandilnath.mykitchenapp.model.Product;

import java.util.List;

import androidx.lifecycle.LiveData;

public class ProductRepository {

    private final ProductDao mProductDao;
    private final LiveData<List<Product>> mProducts;

    public ProductRepository(Application application) {
        ProductDatabase db = ProductDatabase.getDatabase(application);
        mProductDao = db.productDao();
        mProducts = mProductDao.getAllProducts();
    }

    public LiveData<List<Product>> getAllProducts() {
        return mProducts;
    }

    public LiveData<List<Product>> getAvailableProducts() {
        return mProductDao.getAvailableProducts();
    }

    public void insert(Product product) {
        new insertAsyncTask(mProductDao).execute(product);
    }

    public void update(Product product) {
        new updateAsyncTask(mProductDao).execute(product);
    }


    private static class insertAsyncTask extends AsyncTask<Product, Void, Void> {
        private final ProductDao mAsyncTaskDao;

        insertAsyncTask(ProductDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Product... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }

    }

    private static class updateAsyncTask extends AsyncTask<Product, Void, Void> {
        private final ProductDao mAsyncTaskDao;

        updateAsyncTask(ProductDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Product... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }

    }

}
