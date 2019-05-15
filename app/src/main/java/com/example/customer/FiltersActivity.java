package com.example.customer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FiltersActivity extends AppCompatActivity {
    //................Swap between hours and info


    //
    EditText etSearchFood, etSearchRestaurant;
    RadioGroup radioPrice;
    RadioButton radioSubPrice;
    RadioGroup radioDiscount;
    RadioButton radioSubDiscount;

    //Define variables for filters
    Integer lowDiscount;
    Integer highDiscount;
    Integer lowPrice;
    Integer highPrice;
    //database
    private FirebaseDatabase mDatabaseRestaurant;
    String restId;
    private DatabaseReference mReferenceRestaurant;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        //Initialization
        lowPrice = 0;
        highPrice = 10000;
        lowDiscount = 0;
        highDiscount = 100;

        //Assign Ids

        etSearchFood = findViewById(R.id.etSearchFood);
        etSearchRestaurant = findViewById(R.id.etSearchRestaurant);

        radioDiscount = findViewById(R.id.radioDiscount);
        radioPrice = findViewById(R.id.radioPrice);


        Button btnApplysearch = findViewById(R.id.btnApplySearch);
        btnApplysearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchFood=etSearchFood.getText().toString().trim();
                Intent intent = new Intent(FiltersActivity.this,ShowFiltersActivity.class);
                intent.putExtra("lowPrice",lowPrice.toString());
                intent.putExtra("highPrice",highPrice.toString());
                intent.putExtra("lowDiscount",lowDiscount.toString());
                intent.putExtra("highDiscount",highDiscount.toString());
                intent.putExtra("restId",restId);
              //  Toast.makeText(FiltersActivity.this,lowPrice.toString()+lowDiscount.toString()+highPrice.toString()+highDiscount.toString(),Toast.LENGTH_LONG).show();

                intent.putExtra("searchFood",etSearchFood.getText().toString().trim());
                startActivity(intent);



            }
        });


///
    }

    public void rbClick(View view) {
        int radioPriceId = radioPrice.getCheckedRadioButtonId();
        radioSubPrice = findViewById(radioPriceId);
      //  Toast.makeText(FiltersActivity.this, radioSubPrice.getText(), Toast.LENGTH_SHORT).show();
        if (radioSubPrice.getText().toString().toLowerCase().contains("all")) {
            lowPrice = 0;
            highPrice = 10000;
        }
        if (radioSubPrice.getText().toString().toLowerCase().contains("10")) {
            lowPrice = 0;
            highPrice = 10;
        }
        if (radioSubPrice.getText().toString().toLowerCase().contains("20")) {
            lowPrice = 10;
            highPrice = 20;
        }
        if (radioSubPrice.getText().toString().toLowerCase().contains("above")) {
            lowPrice = 20;
            highPrice = 10000;
        }
    }

    public void rbPcClick(View view) {
        int radioDiscountId = radioDiscount.getCheckedRadioButtonId();
        radioSubDiscount = findViewById(radioDiscountId);
       // Toast.makeText(FiltersActivity.this, radioSubDiscount.getText(), Toast.LENGTH_SHORT).show();
        if (radioSubDiscount.getText().toString().toLowerCase().contains("all")) {
            lowDiscount = 0;
            highDiscount = 100;
        }
        if (radioSubDiscount.getText().toString().toLowerCase().contains("0")) {
            lowDiscount = 0;
            highDiscount = 30;
        }
        if (radioSubDiscount.getText().toString().toLowerCase().contains("60")) {
            lowDiscount = 30;
            highDiscount = 60;
        }
        if (radioSubDiscount.getText().toString().toLowerCase().contains("above")) {
            lowDiscount = 60;
            highDiscount = 100;
        }
    }

}
