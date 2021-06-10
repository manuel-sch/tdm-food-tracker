package com.example.mealstock.database;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mealstock.activities.MainActivity;
import com.example.mealstock.models.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;

import java.util.ArrayList;

public class FirebaseAdapter {


    private FirebaseUser user;
    private String uID;
    private DatabaseReference databaseReference;
    private DatabaseReference userReference;
    private DatabaseReference productReference;
    private DatabaseReference userIDReference;
    private String TAG = "FirebaseAdapter";



    public FirebaseAdapter() {

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
                Product comment = dataSnapshot.getValue(Product.class);

                // ...
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                Product newComment = dataSnapshot.getValue(Product.class);
                String commentKey = dataSnapshot.getKey();

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
        productReference.push().setValue(product);
    }

    public FirebaseUser getUser() {
        return user;
    }

    public ArrayList<Product> getProductListFromDatabase(){

         ArrayList<Product> list = new ArrayList<>();

        productReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // this method is call to get the realtime
                // updates in the data.
                // this method is called when the data is
                // changed in our Firebase console.
                // below line is for getting the data from
                // snapshot of our database.
                //list.clear();
                Product value = snapshot.getValue(Product.class);
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    list.add(dataSnapshot.getValue(Product.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(null, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

        productReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
            }
        });

        return list;

    }


    public String getuID() {
        return uID;
    }

    public DatabaseReference getProductReference() {
        return productReference;
    }
}
