package com.internship;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class HomeFragment extends Fragment {

    RecyclerView recyclerView, Category_recyclerView;
    String[] idArray = {"1", "2", "3"};
    String[] nameArray = {"Table lamp", "Shirt", "Butter"}, priceArray = {"1000", "450", "20"};
    int[] ImageArray = {R.drawable.table_lamp, R.drawable.shirt, R.drawable.butter};
    String[] Category_name_array = {"Travel", "Bakery", "Mobile", "Health", "Food-drink", "Electronics", "Appliances"};
    int[] Category_image_array = {R.drawable.travel, R.drawable.bakery,R.drawable.mobile, R.drawable.health, R.drawable.food_drink, R.drawable.electronics, R.drawable.applications};
    String[] descArray = {"A table lamp is a source of light that stands on a table or any piece of furniture. In the family of lamps, table lamps serve as the easiest lighting solutions.",
    "a long- or short-sleeved garment for the upper part of the body, usually lightweight and having a collar and a front opening. an undergarment of cotton, or other material, for the upper part of the body.",
    "Butter is a dairy product made from separating whole milk or cream into fat and buttermilk."};

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

//        category releted from here
        Category_recyclerView = view.findViewById(R.id.home_recyclerview_category);
        Category_recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        Category_recyclerView.setItemAnimator(new DefaultItemAnimator());

        CategoryAdapter categoryAdapter = new CategoryAdapter(getActivity(), Category_name_array, Category_image_array);

        Category_recyclerView.setAdapter(categoryAdapter);


//        Product related from here
        ProductAdapter adapter = new ProductAdapter(getActivity(), nameArray, ImageArray, priceArray, descArray, idArray);

        recyclerView = view.findViewById(R.id.home_recyclerview);

//        vertical view
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

//        Horixontal view
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        return view;
    }
}