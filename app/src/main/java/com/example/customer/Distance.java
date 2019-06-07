package com.example.customer;


public class Distance {
    //Property name must be the same as what we defined in real time database
    private String restaurantId;

    private Double distance;

    public Distance() {
        //Constructor , it is needed
    }

    public Distance(Double distance, String restaurantId
        ) {


        this.distance=distance;
        this.restaurantId = restaurantId;

    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String customerId) {
        this.restaurantId = customerId;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}