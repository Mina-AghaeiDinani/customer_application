package com.example.customer;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import com.example.customer.R;

public class FirebaseDatabaseOrdered {

    private List<OrderdFood> orderdFoods = new ArrayList<>();
    private String restaurantId1;

    public interface DataStatus{
        void  DataIsLoaded(List<OrderdFood> orderdFoods, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }





    public  void readFoods (final DataStatus dataStatus,final DatabaseReference mReferenceFoods){
        mReferenceFoods.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderdFoods.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    OrderdFood orderdFood=keyNode.getValue(OrderdFood.class);

                   orderdFoods.add(orderdFood);
                }

                dataStatus.DataIsLoaded(orderdFoods,keys);
                }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



}
