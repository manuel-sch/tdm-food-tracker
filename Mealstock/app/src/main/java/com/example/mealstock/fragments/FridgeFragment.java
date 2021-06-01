package com.example.mealstock.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.mealstock.R;
import com.example.mealstock.activities.ProductListActivity;

public class FridgeFragment extends Fragment {

    private ImageView gefrierFach;
    private ImageView kuehlFach;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return (ViewGroup) inflater.inflate(
                R.layout.fragment_fridge, container, false);

    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        gefrierFach = (ImageView) getView().findViewById(R.id.gefrierfach);

        gefrierFach.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), ProductListActivity.class)));

    }
}