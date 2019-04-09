package com.ihandilnath.mykitchenapp.db;

import android.content.Context;

import com.ihandilnath.mykitchenapp.model.Product;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Product.class}, version = 1)
public abstract class ProductDatabase extends RoomDatabase {

    private static volatile ProductDatabase INSTANCE;

    static ProductDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ProductDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ProductDatabase.class, "product_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract ProductDao productDao();
}
