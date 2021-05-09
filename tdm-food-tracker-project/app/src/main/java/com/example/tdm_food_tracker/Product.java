package com.example.tdm_food_tracker;

import java.util.Arrays;

public class Product {
    private String barcode;
    private String name;
    private String genericName;
    private String brand;
    private String imageUrl;
    private String[] allergens;
    private String[] categories;
    private String ingredients;
    private String nutrientLevel;
    private String novaGroup;
    private String ecoScore;
    private String quantity;
    private double price;

    public Product(String barcode, String name, String genericName, String brand,
                   String imageUrl, String[] allergens, String[] categories,
                   String ingredients, String nutrientLevel, String novaGroup, String ecoScore,
                   String quantity, double price) {
        this.barcode = barcode;
        this.name = name;
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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String[] getAllergens() {
        return allergens;
    }

    public void setAllergens(String[] allergens) {
        this.allergens = allergens;
    }

    public String[] getCategories() {
        return categories;
    }

    public void setCategories(String[] categories) {
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
        return "Product{" +
                "barcode='" + barcode + '\'' +
                ", name='" + name + '\'' +
                ", genericName='" + genericName + '\'' +
                ", brand='" + brand + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", allergens=" + Arrays.toString(allergens) +
                ", categories=" + Arrays.toString(categories) +
                ", ingredients='" + ingredients + '\'' +
                ", nutrientLevel='" + nutrientLevel + '\'' +
                ", novaGroup='" + novaGroup + '\'' +
                ", ecoScore='" + ecoScore + '\'' +
                ", quantity='" + quantity + '\'' +
                ", price=" + price +
                '}';
    }
}
