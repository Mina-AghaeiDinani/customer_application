package com.example.customer;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FirebaseDatabaseSortPrice {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceFoods;
    private List<DailyOffer> DailyFoods = new ArrayList<>();

    public interface DataStatus{
        void  DataIsLoaded(List<DailyOffer> dailyOffers, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public FirebaseDatabaseSortPrice() {
       //Initialize database objects
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceFoods= mDatabase.getReference("DailyFoods");
    }
    public void readFoods (final DataStatus dataStatus){
        mReferenceFoods.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DailyFoods.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    DailyOffer dailyOffer=keyNode.getValue(DailyOffer.class);

                    DailyFoods.add(dailyOffer);


                }
                sortprice();
                dataStatus.DataIsLoaded(DailyFoods,keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void sortprice() {

        Collections.sort(DailyFoods, new Comparator<DailyOffer>() {
            @Override
            public int compare(DailyOffer o1, DailyOffer o2) {
                return o1.getPrice().compareTo(o2.getPrice());

            }

        });


    }


}
