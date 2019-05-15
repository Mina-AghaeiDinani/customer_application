package com.example.customer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

public class ShowFiltersActivity extends AppCompatActivity {
    //recycler view
    private RecyclerView mRecyclerView;
    private String lowPrice, highPrice, lowDiscount, highDiscount, restId, searchFood;
    Integer lowPriceint;
    Integer lowDiscountint;
    Integer highDiscountint;
    Integer highPriceint;
    //Define variables for filters

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_filters);
        //Get intent

        lowPrice = getIntent().getStringExtra("lowPrice");
        highPrice = getIntent().getStringExtra("highPrice");
        lowDiscount = getIntent().getStringExtra("lowDiscount");
        highDiscount = getIntent().getStringExtra("highDiscount");
        restId = getIntent().getStringExtra("restId");
        searchFood = getIntent().getStringExtra("searchFood");

        lowPriceint = Integer.valueOf(lowPrice);
        highPriceint = Integer.valueOf(highPrice);
        lowDiscountint = Integer.valueOf(lowDiscount);
        highDiscountint = Integer.valueOf(highDiscount);
        //Initiate Recycler view

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_show_filter);
        new FirebaseDatabaseFilter().readFoods(new FirebaseDatabaseFilter.DataStatus() {
            @Override
            public void DataIsLoaded(List<DailyOffer> dailyOffers, List<String> keys) {

                new RecyclerView_Config().setConfig(mRecyclerView, ShowFiltersActivity.this, dailyOffers, keys);
            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        },searchFood, lowPriceint, highPriceint, lowDiscountint, highDiscountint, restId);

    }
}
