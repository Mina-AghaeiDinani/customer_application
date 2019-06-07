package com.example.customer;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseFilter {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceFoods;
    private DatabaseReference mReferenceRestaurant;
    private List<DailyOffer> DailyFoods = new ArrayList<>();

    public interface DataStatus {
        void DataIsLoaded(List<DailyOffer> dailyOffers, List<String> keys);

        void DataIsInserted();

        void DataIsUpdated();

        void DataIsDeleted();
    }

    public FirebaseDatabaseFilter() {
        //Initialize database objects
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceFoods = mDatabase.getReference("DailyFoods");
    }

    public void readFoods(final DataStatus dataStatus, final String foodName, final Integer lowPrice, final Integer highPrice, final Integer lowDiscount, final Integer highDiscount, final String restId) {
        mReferenceFoods.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DailyFoods.clear();
                List<String> keys = new ArrayList<>();

                Double IntPrice;
                Integer IntDiscount;


                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    DailyOffer dailyOffer = keyNode.getValue(DailyOffer.class);
                    IntPrice = Double.valueOf(dailyOffer.getPrice());
                    IntDiscount = Integer.valueOf(dailyOffer.getDiscount());


                    if (dailyOffer.getName().toLowerCase().contains(foodName))
                        if ((IntPrice >= lowPrice) && (IntPrice <= highPrice))
                            if ((IntDiscount >= lowDiscount) && (IntDiscount <= highDiscount))
                                /*

                                 */
                                    DailyFoods.add(dailyOffer);

                }
                dataStatus.DataIsLoaded(DailyFoods, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
