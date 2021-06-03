package com.example.mealstock.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.example.mealstock.R;
import com.example.mealstock.adapters.ProductRecyclerViewAdapter;
import com.example.mealstock.constants.UrlRequestConstants;
import com.example.mealstock.databinding.FragmentSearchRemoteBinding;
import com.example.mealstock.models.Product;
import com.example.mealstock.network.JsonRequest;
import com.example.mealstock.network.NetworkDataTransmitterSingleton;
import com.example.mealstock.utils.RequestMethod;
import com.example.mealstock.viewmodels.ProductListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ProductRemoteSearchFragment extends Fragment implements ProductRecyclerViewAdapter.ProductItemClickListener, View.OnClickListener {

    // Constants
    private static final String TAG = ProductRemoteSearchFragment.class.getSimpleName();

    // Main Variables
    private List<Product> currentProducts;
    private Product selectedProduct;

    // Views
    private ProgressBar progressBar;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;

    // Utils
    private FragmentSearchRemoteBinding binding;
    private NetworkDataTransmitterSingleton dataTransmitter;
    private FragmentManager parentFragmentManager;
    private ProductListViewModel productListViewModel;
    private ProductRecyclerViewAdapter recyclerViewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchRemoteBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        initializeUtils();
        initializeViews();
        setUpSearchBar();
        setUpViewObserving();
    }

    private void initializeUtils() {
        dataTransmitter = NetworkDataTransmitterSingleton.getInstance(requireActivity().getApplicationContext());
        parentFragmentManager = getParentFragmentManager();
    }

    private void initializeViews() {
        progressBar = binding.progressBar;
        searchView = binding.searchViewRemoteProduct;
        recyclerView = binding.recyclerViewRemoteProducts;
        LinearLayoutManager llm = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(llm);
        recyclerViewAdapter = new ProductRecyclerViewAdapter(this);
        recyclerView.setAdapter(recyclerViewAdapter);
        floatingActionButton = binding.fabProductSave;
        floatingActionButton.setOnClickListener(this);
    }

    private void setUpSearchBar() {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            String combinedUrl;
            JsonRequest jsonRequest;

            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit: " + query);
                combinedUrl = UrlRequestConstants.OPENFOODFACTS_SEARCH_PRODUCT_WTIH_PRODUCT_NAME + query.replace(" ", "+");
                jsonRequest = new JsonRequest(combinedUrl, Request.Method.GET, RequestMethod.PRODUCT_NAME, null);
                dataTransmitter.requestJsonObjectResponseForJsonRequestWithContext(jsonRequest, requireContext());
                setSearchViewActivationWithBool(false);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void setUpViewObserving() {
        productListViewModel = new ViewModelProvider(this).get(ProductListViewModel.class);
        productListViewModel.getProducts().observe(requireActivity(), products -> {
            currentProducts = products;
            recyclerViewAdapter.updateProducts(products);
        });
    }

    public void setCurrentProducts(List<Product> products){
        productListViewModel.setProducts(products);
    }

    public void setSearchViewActivationWithBool(boolean activateSearchView){
            searchView.setSubmitButtonEnabled(activateSearchView);
    }

    private void handleProductSaveFab() {
        productListViewModel.insertProduct(selectedProduct);
        Log.d(TAG, "handleProductSaveFab: Product inserted: " + selectedProduct);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.fab_product_save:
                handleProductSaveFab();
                break;
        }
    }

    @Override
    public void onProductItemClick(int position) {
        floatingActionButton.setEnabled(true);
        selectedProduct = currentProducts.get(position);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}