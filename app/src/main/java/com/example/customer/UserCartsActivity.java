package com.example.customer;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.customer.classes.FirebaseDatabaseCarts;
import com.example.customer.recyclers.RecyclerView_Carts;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserCartsActivity extends AppCompatActivity {
    private TextView tvTotalCarts;
    private Integer totalCart;
    private RecyclerView mRecyclerView;
    private int totalPrice;
    private int totalItems;
    private DatabaseReference databaseCart;
    private String customerId;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_carts);


        //get reference
        customerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseCart = FirebaseDatabase.getInstance().getReference("CartFoods")
                .child(customerId);

        //count total restaurants that u ordered food
        totalCart=0;
        tvTotalCarts=findViewById(R.id.tvTotalCarts);
        databaseCart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot keyNode : dataSnapshot.getChildren()){
                    totalCart++;
                }
                if (totalCart==0) tvTotalCarts.setText("Your cart is empty");
                else tvTotalCarts.setText("ordered from "+ totalCart+" restaurant(s)•");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//.....................
        //Initiate Recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerCarts);
        new FirebaseDatabaseCarts().readCarts(new FirebaseDatabaseCarts.DataStatus() {
            @Override
            public void DataIsLoaded(List<CartInfo> cartInfos, List<String> keys) {
                findViewById(R.id.loading_pb1).setVisibility(View.GONE);
                new RecyclerView_Carts().setConfig(mRecyclerView, UserCartsActivity.this, cartInfos, keys);
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
        });
        //



    }
}
