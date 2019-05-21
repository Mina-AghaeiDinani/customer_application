package com.example.customer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FoodDetailsActivity extends AppCompatActivity {
    private int i;
    private Integer Total;
    private Integer TotalItems;
    //
    private String key; //Key of food
    private String foodId;
    private String restaurantId; //Uid of restaurant
    private String restaurantName;
    private String restaurantImage;
    private String restaurantComment;
    private String customerId;
    private String customerName;
    private String customerImage;
    private String status;
    private String foodName;
    private String foodPrice;

    private DatabaseReference databaseFood;
    private DatabaseReference databaseCartFood;
    private DatabaseReference databaseCartInfo;
    private DatabaseReference databaseTestAdd;

    private DatabaseReference databaseCustomer; //we want to extract customer name
    private DatabaseReference databaseRestaurant; // we want to get restaurant name

    private TextView tvCounter;
    private int Counter;
    private int IntPrice;
    private Button btnAdd;
    private ImageButton btnDecrease;
    private TextView NameofFood, PriceOfFood, DiscountOfFood, DescriptionOfFood, CommentOfFood;
    private ImageView imgFoodDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        //Get refernces
        customerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseFood = FirebaseDatabase.getInstance().getReference("DailyFoods");
        databaseCustomer = FirebaseDatabase.getInstance().getReference("CustomersProfile").child(customerId);
        databaseRestaurant = FirebaseDatabase.getInstance().getReference("Restaurants");
        databaseCartFood = FirebaseDatabase.getInstance().getReference("CartFoods")
                .child(customerId);
        databaseCartInfo = FirebaseDatabase.getInstance().getReference("CartInfo")
                .child(customerId);
        //
        key = getIntent().getStringExtra("key");
        foodId = getIntent().getStringExtra("foodId");

        NameofFood = findViewById(R.id.NameOfFood);
        PriceOfFood = findViewById(R.id.PriceOfFood);
        DiscountOfFood = findViewById(R.id.DiscountOfFood);
        DescriptionOfFood = findViewById(R.id.DescriptionOfFood);
        imgFoodDetails = findViewById(R.id.imgFoodDetails);
        CommentOfFood = findViewById(R.id.CommentOfFood);
        btnAdd = findViewById(R.id.btnAdd);
        //read info and show in activity
        // readFoodInfo(key);
        readFoodInfo(foodId);
        //getRestaurantName(restaurantId);
        getCustomerName(customerId);
        //*********Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarBack);
        setSupportActionBar(toolbar);
        //end of toolbar
        //Counter
        i = 0;
        ImageButton btnIncrease = findViewById(R.id.btnIncrease);
        btnDecrease = findViewById(R.id.btnDecrease);
        tvCounter = findViewById(R.id.tvCounter);
        //convert number of item to Integer

        if (i == 0) btnDecrease.setEnabled(false);
        else btnDecrease.setEnabled(true);
        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i--;
                if (i == 0) btnDecrease.setEnabled(false);
                else btnDecrease.setEnabled(true);
                tvCounter.setText("" + i);
            }
        });

        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 0) btnDecrease.setEnabled(false);
                else btnDecrease.setEnabled(true);
                i++;
                tvCounter.setText("" + i);
            }
        });
        // End of counter

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //New added codes
                //Check if food has been selected food
                if (tvCounter.getText().equals("0")) {
                    Toast.makeText(FoodDetailsActivity.this, "Wrong quantity chosen!", Toast.LENGTH_LONG).show();
                } else {
                    //register ordered food
                    Counter = Integer.valueOf(tvCounter.getText().toString().trim());
                    Integer totalPrice = Counter * IntPrice;
                    OrderdFood orderedFood = new OrderdFood();
                    orderedFood.setFoodName(foodName);
                    orderedFood.setFoodPrice(foodPrice);
                    orderedFood.setTotalPrice(totalPrice.toString());
                    orderedFood.setComment(CommentOfFood.getText().toString().trim());
                    orderedFood.setFoodId(foodId);
                    orderedFood.setNumber(tvCounter.getText().toString().trim());
                    databaseCartFood.child(restaurantId).child("Foods").child(foodId).setValue(orderedFood);

                    CartInfo cartInfo = new CartInfo();
                    cartInfo.setStatus("pending");
                    cartInfo.setCustomerId(customerId);
                    cartInfo.setCustomerName(customerName);
                    cartInfo.setCustomerImage(customerImage);
                    cartInfo.setRestaurantId(restaurantId);
                    cartInfo.setRestaurantName(restaurantName);
                    cartInfo.setRestaurantImage(restaurantImage);
                    cartInfo.setRestaurantComment(restaurantComment);
                    databaseCartInfo.child(restaurantId).setValue(cartInfo);

                    //Compute and Show total Price
                    Total = 0;
                    TotalItems = 0;

                    databaseCartFood.child(restaurantId).child("Foods").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            /*  for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                                OrderdFood orderdFood = keyNode.getValue(OrderdFood.class);

                                Integer IntTotalPrice = Integer.valueOf(orderdFood.getTotalPrice());
                                Integer IntTotalItems = Integer.valueOf(orderdFood.getNumber());
                                TotalItems = TotalItems + IntTotalItems;
                                Total = Total + IntTotalPrice;
                            }
                            Toast.makeText(FoodDetailsActivity.this, TotalItems, Toast.LENGTH_LONG).show();


                            databaseCartInfo.child(restaurantId)
                                    .child("totalPrice").setValue(Total.toString());
                            databaseCartInfo.child(restaurantId)
                                    .child("totalItems").setValue(TotalItems.toString());*/
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    //...............................

                    Toast.makeText(FoodDetailsActivity.this, "Food is added to your cart", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(FoodDetailsActivity.this, Home.class);
                    startActivity(intent);
                    // finish();

                }
            }
        });
    }

    //********** what toolbar is doing
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.backmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.btn_back) {
            Intent intent = new Intent(this, Home.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    //End of code related to the toolbar

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("counter", i); // Save value of i which is integer into counter

    }

    //To restore the saved value
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        i = savedInstanceState.getInt("counter"); // Value that was saved will restore to variable
        tvCounter.setText("" + i); //tv.setText(Integer.toString(i));
    }

    //read selected photo
    private void readFoodInfo(String foodId) {
        //Read info of food
        databaseFood.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DailyOffer dailyOffer = dataSnapshot.getValue(DailyOffer.class);
                NameofFood.setText(dailyOffer.getName());
                DiscountOfFood.setText(dailyOffer.getDiscount() + "% (Off) • ");
                PriceOfFood.setText(dailyOffer.getPrice() + " € •");
                DescriptionOfFood.setText(dailyOffer.getShortdescription());
                restaurantId = dailyOffer.getRestaurantUid();
                foodPrice = dailyOffer.getPrice();
                //Convert foodPrice to Integer
                IntPrice = Integer.valueOf(dailyOffer.getPrice());
                foodName = dailyOffer.getName();
                Picasso.get()
                        .load(dailyOffer.getImageUrl())
                        .placeholder(R.drawable.personal)
                        .fit()
                        .centerCrop()
                        .into(imgFoodDetails);

                //get restaurant Name
                databaseRestaurant.child(restaurantId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        RestaurantsProfile restaurantsProfile = dataSnapshot.getValue(RestaurantsProfile.class);
                        restaurantName = restaurantsProfile.getNamerestaurant();
                        restaurantImage = restaurantsProfile.getImageUrl();
                        restaurantComment = restaurantsProfile.getDescription();
                        // Toast.makeText(FoodDetailsActivity.this,restaurantId,Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(FoodDetailsActivity.this, databaseError.getCode(), Toast.LENGTH_LONG).show();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FoodDetailsActivity.this, databaseError.getCode(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getCustomerName(String customerId) {
        //get customer address
        databaseCustomer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CustomersProfile customersProfile = dataSnapshot.getValue(CustomersProfile.class);
                customerName = customersProfile.getName();
                customerImage = customersProfile.getImageUrl();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FoodDetailsActivity.this, databaseError.getCode(), Toast.LENGTH_LONG).show();
            }
        });
    }


}
