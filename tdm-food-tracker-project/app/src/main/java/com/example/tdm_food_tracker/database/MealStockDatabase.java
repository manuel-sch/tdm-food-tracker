package com.example.tdm_food_tracker.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.tdm_food_tracker.daos.ProductDao;
import com.example.tdm_food_tracker.models.Product;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@TypeConverters({Converters.class})
@Database(entities = {Product.class}, version = 1)
public abstract class MealStockDatabase extends RoomDatabase {

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static volatile MealStockDatabase INSTANCE;
    private static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };

    static MealStockDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MealStockDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MealStockDatabase.class, "word_database")
                            .addCallback(roomDatabaseCallback).fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract ProductDao productDao();

}
