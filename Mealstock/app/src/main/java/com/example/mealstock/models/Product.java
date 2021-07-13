package com.example.mealstock.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Product implements Serializable, Comparable<Product> {

    private String barcode = "";

    private String productName;

    private String genericName = "";

    private String brands = "";

    private String imageUrl = "";

    private String allergens = "";

    private String categories = "";

    private HashMap<String, String> nutritionFacts = new HashMap<>();

    private String ingredients;

    private Date boughtDate = new Date();

    private Date expiryDate = new Date();

    private String storage = "Gefrierfach";

    private String nutrientLevel = "Keine Daten.";

    private String novaGroup = "";

    private String ecoScore = "";

    private double quantity = 0;

    private double price;

    private int unit = 0;


    public List<String> getAllergensAsList(){
        String[] allergenTokens = allergens.split(", ");
        if(allergenTokens.length == 1)
            allergenTokens = allergens.split(",");
        return new ArrayList<String>(Arrays.asList(allergenTokens));
    }

    public List<String> getIngredientsAsList(){
        String[] ingredientsTokens = ingredients.split(", ");
        if(ingredientsTokens.length == 1)
            ingredientsTokens = allergens.split(",");
        return new ArrayList<String>(Arrays.asList(ingredientsTokens));
    }

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

    public HashMap<String, String> getNutritionFacts() {
        return nutritionFacts;
    }

    public void setNutritionFacts(HashMap<String, String> nutritionFacts) {
        this.nutritionFacts = nutritionFacts;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
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

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
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

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
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
                ", nutritionFacts=" + nutritionFacts +
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

    @Override
    public int compareTo(Product p) {
        return getExpiryDate().compareTo(p.getExpiryDate());
    }
}
