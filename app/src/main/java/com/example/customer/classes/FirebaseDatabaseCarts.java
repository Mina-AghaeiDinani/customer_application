package com.example.customer.classes;

import android.support.annotation.NonNull;

import com.example.customer.CartInfo;
import com.example.customer.OrderdFood;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseCarts {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceCarts;
    private List<CartInfo> cartInfos = new ArrayList<>();

    public interface DataStatus {
        void DataIsLoaded(List<CartInfo> cartInfos, List<String> keys);

        void DataIsInserted();

        void DataIsUpdated();

        void DataIsDeleted();
    }

    public FirebaseDatabaseCarts() {
        //Initialize database objects
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceCarts = mDatabase.getReference("CartInfo")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    }

    public void readCarts(final DataStatus dataStatus) {
        mReferenceCarts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cartInfos.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    CartInfo cartInfo = keyNode.getValue(CartInfo.class);

                    cartInfos.add(cartInfo);
                }
                dataStatus.DataIsLoaded(cartInfos, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
