package com.ihandilnath.mykitchenapp.db;

import android.app.Application;
import android.os.AsyncTask;

import com.ihandilnath.mykitchenapp.model.Product;

import java.util.List;

import androidx.lifecycle.LiveData;

/**
 * Repository class that provides an abstraction for collection of Products in database
 */
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

    /**
     * insert method which creates an async task to insert Product into database
     * @param product - Product to be inserted to database
     */
    public void insert(Product product) {
        new insertAsyncTask(mProductDao).execute(product);
    }

    /**
     * update method which creates an async task to update Product in database
     * @param product - Product to be updated in database
     */
    public void update(Product product) {
        new updateAsyncTask(mProductDao).execute(product);
    }


    /**
     * Class that creates an async task object which executes the insert database operation
     */
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

    /**
     * Class that creates an async task object which executes the update database operation
     */
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
