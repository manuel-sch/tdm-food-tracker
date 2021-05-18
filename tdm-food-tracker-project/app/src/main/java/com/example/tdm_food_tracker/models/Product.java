package com.example.tdm_food_tracker.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity
public class Product implements Serializable {
    @PrimaryKey(autoGenerate = true)
    protected int pId;

    @ColumnInfo(name = "barcode")
    private String barcode = "";

    @ColumnInfo(name = "product_name")
    private String productName = "";

    @ColumnInfo(name = "generic_name")
    private String genericName = "";

    @ColumnInfo(name = "brand")
    private String brand = "";

    @ColumnInfo(name = "imageUrl")
    private String imageUrl = "";

    @ColumnInfo(name = "allergens")
    private String allergens = "";

    @ColumnInfo(name = "categories")
    private String categories = "";

    @ColumnInfo(name = "ingredients")
    private String ingredients = "";

    @ColumnInfo(name = "expiry-date")
    private Date expiryDate = new Date();

    @ColumnInfo(name = "storage")
    private String storage = "Gefrierfach";

    @ColumnInfo(name = "nutrientLevel")
    private String nutrientLevel = "";

    @ColumnInfo(name = "novaGroup")
    private String novaGroup = "";

    @ColumnInfo(name = "ecoScore")
    private String ecoScore = "";

    @ColumnInfo(name = "quantity")
    private String quantity = "";

    @ColumnInfo(name = "price")
    private double price = 0;

    @ColumnInfo(name = "unit")
    private int unit = 0;


/*
    public ProductEntity(String barcode, @NonNull String productName, String genericName,
                         String brand, String imageUrl, String allergens, String categories,
                         String ingredients, String nutrientLevel, String novaGroup, String ecoScore,
                         String quantity, double price) {
        this.barcode = barcode;
        this.productName = productName;
        this.genericName = genericName;
        this.brand = brand;
        this.imageUrl = imageUrl;
        this.allergens = allergens;
        this.categories = categories;
        this.ingredients = ingredients;
        this.nutrientLevel = nutrientLevel;
        this.novaGroup = novaGroup;
        this.ecoScore = ecoScore;
        this.quantity = quantity;
        this.price = price;
    }
    */

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    @NonNull
    public String getProductName() {
        return productName;
    }

    public void setProductName(@NonNull String productName) {
        this.productName = productName;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAllergens() {
        return allergens;
    }

    public void setAllergens(String allergens) {
        this.allergens = allergens;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getNutrientLevel() {
        return nutrientLevel;
    }

    public void setNutrientLevel(String nutrientLevel) {
        this.nutrientLevel = nutrientLevel;
    }

    public String getNovaGroup() {
        return novaGroup;
    }

    public void setNovaGroup(String novaGroup) {
        this.novaGroup = novaGroup;
    }

    public String getEcoScore() {
        return ecoScore;
    }

    public void setEcoScore(String ecoScore) {
        this.ecoScore = ecoScore;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    @Override
    public String toString() {
        return "ProductEntity{" +
                "pId=" + pId +
                ", barcode='" + barcode + '\'' +
                ", productName='" + productName + '\'' +
                ", genericName='" + genericName + '\'' +
                ", brand='" + brand + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", allergens='" + allergens + '\'' +
                ", categories='" + categories + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", expiryDate=" + expiryDate +
                ", storage='" + storage + '\'' +
                ", nutrientLevel='" + nutrientLevel + '\'' +
                ", novaGroup='" + novaGroup + '\'' +
                ", ecoScore='" + ecoScore + '\'' +
                ", quantity='" + quantity + '\'' +
                ", price=" + price +
                ", unit=" + unit +
                '}';
    }
}
