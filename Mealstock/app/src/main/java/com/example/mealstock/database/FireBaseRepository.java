package com.example.mealstock.database;

import android.util.Log;
import android.widget.Toast;

import com.example.mealstock.constants.ProductConstants;
import com.example.mealstock.models.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FireBaseRepository {

    private final String TAG = FireBaseRepository.class.getSimpleName();

    private final FirebaseUser user;
    private final String uID;

    private final DatabaseReference databaseReference;
    private final DatabaseReference userReference;
    private final DatabaseReference productReference;
    private final DatabaseReference userIDReference;
    private final DatabaseReference productFreezerReference;
    private final DatabaseReference productFridgeReference;
    private final DatabaseReference productDrinksReference;
    private final DatabaseReference productShelfReference;


    public FireBaseRepository() {

        databaseReference = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        uID = user.getUid();
        userReference = FirebaseDatabase.getInstance().getReference("Users");
        userIDReference = userReference.child(uID);
        productReference = userIDReference.child("Products");
        productFreezerReference = productReference.child(ProductConstants.FREEZER);
        productDrinksReference = productReference.child(ProductConstants.DRINKS);
        productFridgeReference = productReference.child(ProductConstants.FRIDGE);
        productShelfReference = productReference.child(ProductConstants.SHELF);



        ChildEventListener childEventListener = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                //Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
                Product product = dataSnapshot.getValue(Product.class);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                //Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());
                Product newProduct = dataSnapshot.getValue(Product.class);
                String productKey = dataSnapshot.getKey();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());
                String commentKey = dataSnapshot.getKey();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                //Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());
                Product movedComment = dataSnapshot.getValue(Product.class);
                String commentKey = dataSnapshot.getKey();
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                Toast.makeText(null, "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }
        };

        productReference.addChildEventListener(childEventListener);

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

    public FirebaseUser getUser() {
        return user;
    }


    public String getuID() {
        return uID;
    }

    public DatabaseReference getProductReference() {
        return productReference;
    }

    public DatabaseReference getProductReferenceForStorage(String productStorage) {
        return productReference.child(productStorage);
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

}
