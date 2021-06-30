package com.example.mealstock.models;

public class Recipe {

    private String recipeName = "";

    private String cookingTime = "";

    private String ingredients;

    private String cookingDirections;

    private String imageUrl = "";


    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getCookingTime(){
        return cookingTime;
    }

    public void setCookingTime(String cookingTime){
        this.cookingTime = cookingTime;
    }

    public String getIngredients(){
        return ingredients;
    }

    public void setIngredients(String ingredients){
        this.ingredients = ingredients;
    }

    public String getCookingDirections(){
        return  cookingDirections;
    }

    public void setCookingDirections(String cookingDirections){
        this.cookingDirections = cookingDirections;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
