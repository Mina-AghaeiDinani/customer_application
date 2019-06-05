package com.example.customer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ShowFiltersActivity extends AppCompatActivity {
    //Navigation
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.sign_out:
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(ShowFiltersActivity.this,LoginActivity.class));
                    return true;
                case R.id.cartInfo:
                    startActivity(new Intent(ShowFiltersActivity.this,UserCartActivity.class));

                    return true;
                case R.id.menu:
                    Intent  intent = new Intent(ShowFiltersActivity.this, Home.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };
    //................
    //recycler view
    private RecyclerView mRecyclerView;
    private String lowPrice, highPrice, lowDiscount, highDiscount, restId, searchFood;
    Integer lowPriceint;
    Integer lowDiscountint;
    Integer highDiscountint;
    Integer highPriceint;
    //Define variables for filters
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_filters);
        //Navigation
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_filter);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        firebaseAuth=FirebaseAuth.getInstance();
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
