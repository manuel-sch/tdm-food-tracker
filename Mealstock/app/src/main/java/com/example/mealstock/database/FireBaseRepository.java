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

public class FireBaseRepository {


    private FirebaseUser user;
    private String uID;
    private final DatabaseReference databaseReference;
    private final DatabaseReference userReference;
    private final DatabaseReference productReference;
    private final DatabaseReference userIDReference;

    private final String TAG = FireBaseRepository.class.getSimpleName();



    public FireBaseRepository() {

        databaseReference = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        uID = user.getUid();
        userReference = FirebaseDatabase.getInstance().getReference("Users");
        userIDReference = userReference.child(uID);
        productReference = userIDReference.child("Products");



        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new comment has been added, add it to the displayed list
                Product product = dataSnapshot.getValue(Product.class);

                // ...
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                Product newProduct = dataSnapshot.getValue(Product.class);
                String productKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                String commentKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
                Product movedComment = dataSnapshot.getValue(Product.class);
                String commentKey = dataSnapshot.getKey();

                // ...
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());

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

}
