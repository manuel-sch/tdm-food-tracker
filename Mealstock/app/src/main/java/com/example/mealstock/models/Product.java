package com.example.mealstock.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Product implements Serializable {

    private String barcode = "";


    private String productName;


    private String genericName = "";


    private String brands = "";


    private String imageUrl = "";


    private String allergens = "";


    private String categories = "";

    private String nutritionFacts = "";

    private String ingredients;

    private Date boughtDate = new Date();

    private Date expiryDate = new Date();



    private String storage = "Gefrierfach";


    private String nutrientLevel = "";


    private String novaGroup = "";


    private String ecoScore = "";


    private double quantity = 0;


    private double price;


    private int unit = 1;


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


    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public String getBrands() {
        return brands;
    }

    public void setBrands(String brands) {
        this.brands = brands;
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

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getBoughtDate() {
        return boughtDate;
    }

    public void setBoughtDate(Date boughtDate) {
        this.boughtDate = boughtDate;
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


    public String getNutritionFacts() {
        return nutritionFacts;
    }

    public void setNutritionFacts(String nutritionFacts) {
        this.nutritionFacts = nutritionFacts;
    }

    public List<String> getAllergensAsList(){
        String[] allergenTokens = allergens.split(", ");
        return new ArrayList<String>(Arrays.asList(allergenTokens));
    }

    public List<String> getIngredientsAsList(){
        String[] ingredientsTokens = ingredients.split(", ");
        return new ArrayList<String>(Arrays.asList(ingredientsTokens));
    }

    @Override
    public String toString() {
        return "Product{" +
                "barcode='" + barcode + '\'' +
                ", productName='" + productName + '\'' +
                ", genericName='" + genericName + '\'' +
                ", brands='" + brands + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", allergens='" + allergens + '\'' +
                ", categories='" + categories + '\'' +
                ", nutritionFacts='" + nutritionFacts + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", boughtDate=" + boughtDate +
                ", expiryDate=" + expiryDate +
                ", storage='" + storage + '\'' +
                ", nutrientLevel='" + nutrientLevel + '\'' +
                ", novaGroup='" + novaGroup + '\'' +
                ", ecoScore='" + ecoScore + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", unit=" + unit +
                '}';
    }
}
