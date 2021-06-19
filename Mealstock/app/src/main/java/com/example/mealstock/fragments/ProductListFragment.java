package com.example.mealstock.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealstock.R;
import com.example.mealstock.adapters.ProductListForStorageRecyclerViewAdapter;
import com.example.mealstock.database.FireBaseRepository;
import com.example.mealstock.databinding.FragmentProductListBinding;
import com.example.mealstock.models.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductListFragment extends Fragment implements ProductListForStorageRecyclerViewAdapter.ProductItemClickListener, ProductListForStorageRecyclerViewAdapter.ProductItemLongClickListener {
    private final String TAG = ProductListFragment.class.getSimpleName();
    private ArrayList<Product> currentProducts;
    private FragmentProductListBinding binding;
    private FireBaseRepository fireBaseRepository;
    private ProductListForStorageRecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private Spinner storageSpinner;
    private String storageOfProducts;
    private SearchView searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentProducts = new ArrayList<>();
        storageOfProducts = requireArguments().getString("storage");
        fireBaseRepository = new FireBaseRepository();
        recyclerViewAdapter = new ProductListForStorageRecyclerViewAdapter(this,this);


        Log.d(TAG, "onViewCreated: " + storageOfProducts);

        initializeViews();
        setUpSpinner();
        initRecyclerView();
        setUpFireBase();
        setUpSearchBar();


    }

    private void initializeViews() {
        storageSpinner = binding.filterSpinner;
        recyclerView = binding.recyclerView;
        searchView = binding.SearchViewProductList;

    }

    private void setUpSearchBar() {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerViewAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void setUpSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.product_storage, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        storageSpinner.setAdapter(adapter);
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void setUpFireBase() {

        fireBaseRepository.getProductReferenceForStorage(storageOfProducts).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentProducts.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Log.d(TAG, "onDataChange: " + dataSnapshot.getValue(Product.class));

                    currentProducts.add(dataSnapshot.getValue(Product.class));
                }
                recyclerViewAdapter.updateProducts(currentProducts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(null, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onProductItemClick(int position) {
        Log.d(TAG, "Position: " + position);


    }

    @Override
    public void onProductItemLongClick(int position) {
        Log.d(TAG, "Position LongClick: " + position);
        AlertDialog.Builder dialog=new AlertDialog.Builder(this.getContext());
        dialog.setMessage("Wollen Sie das Prdodukt wirklich löschen?");
        dialog.setTitle("Löschen");
        dialog.setPositiveButton("Ja",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        fireBaseRepository.deleteProduct(currentProducts.get(position).getProductName(),storageOfProducts);
                        recyclerViewAdapter.updateProducts(currentProducts);
                    }
                });
        dialog.setNegativeButton("Nein",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }

}