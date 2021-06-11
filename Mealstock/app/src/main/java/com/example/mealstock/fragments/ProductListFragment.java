package com.example.mealstock.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealstock.R;
import com.example.mealstock.adapters.ProductListAdapter;
import com.example.mealstock.database.FirebaseAdapter;
import com.example.mealstock.models.DataModel;
import com.example.mealstock.models.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductListFragment extends Fragment implements ProductListAdapter.ItemClickListener {
    private ArrayList<Product> list = new ArrayList<>();
    public ProductListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductListFragment newInstance() {
        ProductListFragment fragment = new ProductListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_product_list, container, false);

        FirebaseAdapter fire = new FirebaseAdapter();

        fire.getProductReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // this method is call to get the realtime
                // updates in the data.
                // this method is called when the data is
                // changed in our Firebase console.
                // below line is for getting the data from
                // snapshot of our database.
                //list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    list.add(dataSnapshot.getValue(Product.class));
                }
                initRecyclerView(view);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(null, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

        initRecyclerView(view);
        return view;
    }

    private void initRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);
        ProductListAdapter adapter = new ProductListAdapter(list, this);
        recyclerView.setAdapter(adapter);
    }

    private void buildListData() {
        

    }

    @Override
    public void onItemClick(Product dataModel) {
        Fragment fragment = ProductDetailFragment.newInstance(dataModel.getGenericName());


        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        // transaction.replace(R.id.frame_container, fragment, "detail_fragment");

        transaction.hide(getActivity().getSupportFragmentManager().findFragmentByTag("ProductCardView_fragment"));
        transaction.add(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}