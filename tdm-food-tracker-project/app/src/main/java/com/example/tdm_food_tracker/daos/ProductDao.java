package com.example.tdm_food_tracker.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.tdm_food_tracker.models.Product;

import java.util.Date;
import java.util.List;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM Product")
    LiveData<List<Product>> getAll();

    @Query("SELECT * FROM Product WHERE `expiry-date` <= :targetDate")
    LiveData<List<Product>> findProductsThatExpireBeforeOrEqualToTargetDate(Date targetDate);

    @Query("SELECT * FROM Product ORDER BY product_name ASC")
    List<Product> getAlphabetizedWords();

    @Query("SELECT * FROM Product WHERE pId IN (:productIds)")
    List<Product> loadAllByIds(int[] productIds);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Product product);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Product... products);

    @Update
    void update(Product product);

    @Update
    void updateProducts(Product... product);

    @Delete
    void delete(Product product);

    @Delete
    void deleteProducts(Product... product);
}
