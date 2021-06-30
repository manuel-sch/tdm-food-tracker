package com.example.mealstock.models;

public class Recipe {

    private String recipeName = "";

    private String time = "";

    private String ingredients;

    private String cookingDirections;

    private String imageUrl = "";


    public Recipe (String recipeName, String time, String ingredients, String cookingDirections, String imageUrl){
        this.recipeName = recipeName;
        this.time = time;
        this.ingredients = ingredients;
        this.cookingDirections = cookingDirections;
        this.imageUrl = imageUrl;
    }


    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getTime(){
        return time;
    }

    public void setTime(String time){
        this.time = time;
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
