package com.example.mealstock.models;

public class Recipe {

    private String name;
    private String image;
    private double energyInKcal;
    private String ingredients;
    private String url;
    private int totalTimeInMinutes;
    private String cuisineType;
    private String mealType;
    private String dishType;
    private int quantity;
    private boolean isFavorite;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getEnergyInKcal() {
        return energyInKcal;
    }

    public void setEnergyInKcal(double energyInKcal) {
        this.energyInKcal = energyInKcal;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTotalTimeInMinutes() {
        return totalTimeInMinutes;
    }

    public void setTotalTimeInMinutes(int totalTimeInMinutes) {
        this.totalTimeInMinutes = totalTimeInMinutes;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public String getDishType() {
        return dishType;
    }

    public void setDishType(String dishType) {
        this.dishType = dishType;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", energyInKcal=" + energyInKcal +
                ", ingredients='" + ingredients + '\'' +
                ", directions='" + url + '\'' +
                ", totalTimeInMinutes=" + totalTimeInMinutes +
                ", cuisineType='" + cuisineType + '\'' +
                ", mealType='" + mealType + '\'' +
                ", dishType='" + dishType + '\'' +
                ", quantity=" + quantity +
                ", isFavorite=" + isFavorite +
                '}';
    }
}
