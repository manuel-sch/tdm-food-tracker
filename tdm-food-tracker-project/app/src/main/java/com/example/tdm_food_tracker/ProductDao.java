package com.example.tdm_food_tracker;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM productentity")
    List<ProductEntity> getAll();

    @Query("SELECT * FROM productentity WHERE pId IN (:productIds)")
    List<ProductEntity> loadAllByIds(int[] productIds);

    @Insert
    void insert(ProductEntity product);

    @Insert
    void insertAll(ProductEntity... products);

    @Delete
    void delete(ProductEntity product);
}
