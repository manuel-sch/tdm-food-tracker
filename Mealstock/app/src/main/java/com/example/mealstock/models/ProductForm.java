package com.example.mealstock.models;

public class ProductForm {

    private String productPrice;
    private String productCount;
    private String productIngredient;
    private String productUnit;
    private String productName;

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductCount() {
        return productCount;
    }

    public void setProductCount(String productCount) {
        this.productCount = productCount;
    }

    public String getProductIngredient() {
        return productIngredient;
    }

    public void setProductIngredient(String productIngredient) {
        this.productIngredient = productIngredient;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }


    @Override
    public String toString() {
        return "FormModel{" +
                "productPrice='" + productPrice + '\'' +
                ", productCount='" + productCount + '\'' +
                ", productIngredient='" + productIngredient + '\'' +
                ", productUnit='" + productUnit + '\'' +
                ", productName='" + productName + '\'' +
                '}';
    }

}
