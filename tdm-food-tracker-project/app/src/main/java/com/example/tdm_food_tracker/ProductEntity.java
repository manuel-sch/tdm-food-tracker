package com.example.tdm_food_tracker;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ProductEntity {
    @PrimaryKey(autoGenerate = true)
    public int pId;

    @ColumnInfo(name = "barcode")
    public String barcode;

    @ColumnInfo(name = "product_name")
    @NonNull
    public String productName;

    @ColumnInfo(name = "generic_name")
    public String genericName;

    @ColumnInfo(name = "brand")
    public String brand;

    @ColumnInfo(name = "imageUrl")
    public String imageUrl;

    @ColumnInfo(name = "allergens")
    public String allergens;

    @ColumnInfo(name = "categories")
    public String categories;

    @ColumnInfo(name = "ingredients")
    public String ingredients;

    @ColumnInfo(name = "nutrientLevel")
    public String nutrientLevel;

    @ColumnInfo(name = "novaGroup")
    public String novaGroup;

    @ColumnInfo(name = "ecoScore")
    public String ecoScore;

    @ColumnInfo(name = "quantity")
    public String quantity;

    @ColumnInfo(name = "price")
    public double price;

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

    public int getpId() {
        return pId;
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
                ", nutrientLevel='" + nutrientLevel + '\'' +
                ", novaGroup='" + novaGroup + '\'' +
                ", ecoScore='" + ecoScore + '\'' +
                ", quantity='" + quantity + '\'' +
                ", price=" + price +
                '}';
    }
}
