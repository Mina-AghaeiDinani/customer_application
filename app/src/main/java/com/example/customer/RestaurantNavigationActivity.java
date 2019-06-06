package com.example.customer;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.customer.classes.FirebaseDatabaseCarts;
import com.example.customer.recyclers.RecyclerView_Carts;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RestaurantNavigationActivity extends AppCompatActivity implements OnMapReadyCallback {
    private DatabaseReference mReferenceLocations, mReferenceLocations2;
    private DatabaseReference mReference;
    private ArrayList<RestaurantLocation> restaurantLocations;
    TextView textView;
    String mystring;
    List<LocationReader> locationReaders = new ArrayList<>();
    List<String> keys = new ArrayList<>();
    List<Distance> distances = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private FirebaseAuth firebaseAuth;
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap mMap;
    private ArrayList<MarkerOptions> places;
    MarkerOptions place1, place2;
    Marker myPositionMarker;
    private Button getdirection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_navigation);
        restaurantLocations = new ArrayList<>();
        places = new ArrayList<>();
        mReferenceLocations = FirebaseDatabase.getInstance().getReference().child("RestaurantsLocation");

        //We get the location of the customer
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        mReferenceLocations.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                restaurantLocations.clear();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    LocationReader locationReader = keyNode.getValue(LocationReader.class);
                    locationReaders.add(locationReader);

                    // We store the value read inside an array of RestaurantLocation
                    LatLng thisLatLng = new LatLng(locationReader.getLat(), locationReader.getLng());
                    RestaurantLocation thisRestLocation = new RestaurantLocation(keyNode.getKey(), thisLatLng);
                    restaurantLocations.add(thisRestLocation);
                }
                int i;
                double initiallng, intiallong, distance;
                initiallng = myPositionMarker.getPosition().latitude;
                intiallong = myPositionMarker.getPosition().longitude;
                for (i = 0; i < restaurantLocations.size(); i++) {
                    mMap.addMarker(new MarkerOptions().position(restaurantLocations.get(i).getLatLng()).title(restaurantLocations.get(i).getRestName()));
                    distance = CalculationByDistance(initiallng, intiallong, restaurantLocations.get(i).getLatLng().latitude, restaurantLocations.get(i).getLatLng().longitude);
                    String restId = (restaurantLocations.get(i).getKey());
                    //................
                    //....
                    Distance distance1 = new Distance();
                    distance1.setDistance(distance);
                    distance1.setRestaurantId(restId);

                    FirebaseDatabase.getInstance().getReference("RestaurantDistance")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(restId).setValue(distance1);
                    //..............
                    //distances.add( new Distance(distance,restId) );
                    //sortdistance();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Reading Distance from database
        //Initiate Recycler view
        mReference = FirebaseDatabase.getInstance().getReference("RestaurantDistance")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerNearest1);
        new FirebaseDatabaseSortDistance().readDistance(new FirebaseDatabaseSortDistance.DataStatus() {
            @Override
            public void DataIsLoaded(List<Distance> orderdFoods, List<String> keys) {
                new RecyclerView_Distance().setConfig(mRecyclerView, RestaurantNavigationActivity.this, orderdFoods, keys);

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
        },mReference);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapNearBy);
        mapFragment.getMapAsync(this);


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Log.d("LOCATION", "loc:" + location);
                        if (location != null) {
                            myPositionMarker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
                        }
                    }
                });


    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("mylog", "Added Markers");

        // mMap.addMarker(place1);

        //mMap.addMarker(place2);
        MarkerOptions a = new MarkerOptions().position(new LatLng(50, 6)).title("My position");
        myPositionMarker = mMap.addMarker(a);

    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.map_api_key);
        return url;
    }

    public double CalculationByDistance(double initialLat, double initialLong, double finalLat, double finalLong) {

        double latDiff = finalLat - initialLat;
        double longDiff = finalLong - initialLong;
        double earthRadius = 6371; //In Km if you want the distance in km

        double distance = 2 * earthRadius * Math.asin(Math.sqrt(Math.pow(Math.sin(latDiff / 2.0), 2) + Math.cos(initialLat) * Math.cos(finalLat) * Math.pow(Math.sin(longDiff / 2), 2)));


        return distance;
    }

    public void sortdistance() {
        Collections.sort(distances, new Comparator<Distance>() {
            @Override
            public int compare(Distance o1, Distance o2) {
                return o1.getDistance().compareTo(o2.getDistance());
            }

        });
    }
   /* Circle circle = mMap.addCircle(new CircleOptions()
            .center(new LatLng(myPositionMarker.getPosition().latitude,myPositionMarker.getPosition().longitude))
            .radius(10000)
            .strokeColor(Color.parseColor("#2271cce7"))
            .fillColor(Color.parseColor("#2271cce7")));*/
}
