package com.example.mealstock.database;

import android.util.Log;

import com.example.mealstock.constants.ProductConstants;
import com.example.mealstock.models.Product;
import com.example.mealstock.models.Recipe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FireBaseRepository {

    private final String TAG = FireBaseRepository.class.getSimpleName();

    private final FirebaseUser user;
    private final String uID;

    private final DatabaseReference databaseReference;

    private final DatabaseReference userReference;
    private final DatabaseReference userIDReference;
    private final DatabaseReference productReference;
    private final DatabaseReference recipeReference;

    private final DatabaseReference freezerProductsReference;
    private final DatabaseReference fridgeProductsReference;
    private final DatabaseReference drinksProductsReference;
    private final DatabaseReference shelfProductsReference;

    private final FirebaseDatabase firebaseInstance;


    public FireBaseRepository() {

        firebaseInstance = FirebaseDatabase.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        user = FirebaseAuth.getInstance().getCurrentUser();
        uID = user.getUid();
        userReference = FirebaseDatabase.getInstance().getReference("Users");
        userIDReference = userReference.child(uID);

        recipeReference = userIDReference.child("Recipes");
        productReference = userIDReference.child("Products");

        freezerProductsReference = productReference.child(ProductConstants.FREEZER);
        drinksProductsReference = productReference.child(ProductConstants.DRINKS);
        fridgeProductsReference = productReference.child(ProductConstants.FRIDGE);
        shelfProductsReference = productReference.child(ProductConstants.SHELF);

    }

    public void insertRecipe(Recipe recipe){
        Log.d(TAG, "insertRecipe: " + recipe);
        recipeReference.push().setValue(recipe);
    }

    public void deleteRecipe(String recipeName){
        Query query = recipeReference.orderByChild("name").equalTo(recipeName);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot toDeletedSnapshot: dataSnapshot.getChildren()) {
                    toDeletedSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
    }

    public void insertProduct(Product product){
        String convertedStorage;
        switch(product.getStorage()){
            case "Kühlfach":
                convertedStorage = ProductConstants.FRIDGE;
                break;
            case "Gefrierfach":
                convertedStorage = ProductConstants.FREEZER;
                break;
            case "Getränke":
                convertedStorage = ProductConstants.DRINKS;
                break;
            case "Regal":
                convertedStorage = ProductConstants.SHELF;
                break;
            default:
                return;
        }
        Log.d(TAG, "insertProduct: " + product);
        productReference.child(convertedStorage).push().setValue(product);
    }

    public void deleteProduct (String productName, String storageReference){

        Query query = productReference.child(storageReference).orderByChild("productName").equalTo(productName);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot toDeletedSnapshot: dataSnapshot.getChildren()) {
                    toDeletedSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
    }

    public FirebaseUser getUser() {
        return user;
    }

    public String getuID() {
        return uID;
    }

    public DatabaseReference getRecipeReference() {
        return recipeReference;
    }

    public DatabaseReference getProductReference() {
        return productReference;
    }

    public DatabaseReference getProductReferenceForStorage(String productStorage) {
        return productReference.child(productStorage);
    }


}
