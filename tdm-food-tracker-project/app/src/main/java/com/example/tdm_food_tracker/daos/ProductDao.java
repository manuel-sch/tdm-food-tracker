package com.example.tdm_food_tracker.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.tdm_food_tracker.models.ProductEntity;

import java.util.List;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM productentity")
    LiveData<List<ProductEntity>> getAll();

    @Query("SELECT * FROM productentity ORDER BY product_name ASC")
    List<ProductEntity> getAlphabetizedWords();

    @Query("SELECT * FROM productentity WHERE pId IN (:productIds)")
    List<ProductEntity> loadAllByIds(int[] productIds);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(ProductEntity product);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(ProductEntity... products);

    @Update
    void update(ProductEntity product);

    @Delete
    void delete(ProductEntity product);

    @Query("DELETE FROM productentity")
    void deleteAll();
}
