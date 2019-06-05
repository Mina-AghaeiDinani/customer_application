package com.example.customer;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RestaurantLocation {
    private String key;
    private LatLng latLng;
    private DatabaseReference databaseRestaurant;
    public String restName;

    public interface NameCallBack {
        void onCallback(String value);
    }

    public RestaurantLocation(String key, LatLng latLng){
        this.key = key;
        this.latLng = latLng;
    }

    public void setKey(String key){
        this.key = key;
    }

    public void setRestName(String name){
        this.restName = name;
    }

    public String getRestName(){
         readRestaurantName(new NameCallBack() {
                @Override
                public void onCallback(String value) {
                    restName = value;
                }
            });
        return restName;
    }



    public void setLatLng(LatLng latLng){
        this.latLng = latLng;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void readRestaurantName(final NameCallBack nameCallBack){
        databaseRestaurant = FirebaseDatabase.getInstance().getReference()
                .child("Restaurants");
        databaseRestaurant.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot restaurantSnap : dataSnapshot.getChildren()){

                    RestaurantsProfile thisRestaurant = restaurantSnap.getValue(RestaurantsProfile.class);
                    if (key.equals(restaurantSnap.getKey())){ //If this corresponds to our restaurant
                        restName = thisRestaurant.getName();
                        setRestName(restName);
                        Log.d("RESTNAME", "name: "+restName);
                    }

                }
                nameCallBack.onCallback(restName);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public String getKey() {
        return key;
    }
}
