package com.example.customer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserCartActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;

    private String customerId;
    private String OrderId;
    private TextView tvTotal;
    private Integer Total;
    private Integer TotalItems;
    private String key, restaurantId;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceFoods;
    private DatabaseReference mReferenceInfo;
    private DatabaseReference mRefOrderFoods;
    private DatabaseReference mRefOrderInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cart);
        //get Ids from previous activity
        key = getIntent().getStringExtra("key");
        restaurantId = getIntent().getStringExtra("restaurantId");
        // Toast.makeText(UserCartActivity.this,restaurantId,Toast.LENGTH_LONG).show();
        //get reference
        customerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceFoods = mDatabase.getReference("CartFoods")
                .child(customerId).child(restaurantId).child("Foods");
        mReferenceInfo = mDatabase.getReference("CartInfo")
                .child(customerId).child(restaurantId);
        mRefOrderFoods = mDatabase.getReference("OrderFoods");
        mRefOrderInfo = mDatabase.getReference("OrderInfo");
        OrderId = mRefOrderFoods.push().getKey();
        //Initiate Recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_ordersFood);
        new FirebaseDatabaseOrdered().readFoods(new FirebaseDatabaseOrdered.DataStatus() {
            @Override
            public void DataIsLoaded(List<OrderdFood> orderdFoods, List<String> keys) {
                findViewById(R.id.loading_pb).setVisibility(View.GONE);
                new RecyclerView_Ordered_Config().setConfig(mRecyclerView, UserCartActivity.this, orderdFoods, keys);
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
        }, mReferenceFoods);
//.......................................................
        Button btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            mReferenceFoods.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot keyNode : dataSnapshot.getChildren()){
                        OrderdFood orderdFood=keyNode.getValue(OrderdFood.class);
                       mRefOrderFoods.child(OrderId).child(keyNode.getKey()).setValue(orderdFood);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            mReferenceInfo.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    CartInfo cartInfo = dataSnapshot.getValue(CartInfo.class);
                    mRefOrderInfo.child(OrderId).setValue(cartInfo);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            }
        });
        //.................................
        Button btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserCartActivity.this, "Cart has been removed", Toast.LENGTH_LONG).show();
               // mReferenceFoods.setValue(null);
              //  mReferenceInfo.setValue(null);
            }
        });

    }


}
