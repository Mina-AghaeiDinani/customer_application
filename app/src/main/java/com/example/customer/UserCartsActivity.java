package com.example.customer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
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
    //Navigation
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.sign_out:
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(UserCartsActivity.this,LoginActivity.class));
                    return true;
                case R.id.profile:
                    startActivity(new Intent(UserCartsActivity.this,ProfileActivity.class));
                    finish();
                    return true;
                case R.id.menu:
                    Intent  intent = new Intent(UserCartsActivity.this, Home.class);
                    startActivity(intent);
                    finish();
                    return true;
            }
            return false;
        }
    };
    //................
    //..................

    private RecyclerView mRecyclerView;
    FirebaseAuth firebaseAuth;
    private DatabaseReference databaseCart;
    private String customerId;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_carts);
        firebaseAuth=FirebaseAuth.getInstance();
        //Navigation
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_Carts);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //get reference
        customerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseCart = FirebaseDatabase.getInstance().getReference("CartFoods")
                .child(customerId);
//.....................
        //Initiate Recycler view for readind data
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
