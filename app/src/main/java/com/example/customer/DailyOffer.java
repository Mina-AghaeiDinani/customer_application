package com.example.customer;


public class DailyOffer {
    //Property name must be the same as what we defined in real time database
    private String name, price, discount, shortdescription,restaurantUid,foodId;
    private String imageUrl;
    public DailyOffer() {
        //Constructor , it is needed
    }

    public DailyOffer(String name, String price, String discount, String shortdescription,String restaurantUid, String imageUrl,String foodId) {
        if (discount.trim().equals("")) {
            this.discount = "0";
        } else this.discount = discount;
        this.imageUrl = imageUrl;
        this.name = name;
        this.restaurantUid=restaurantUid;
        this.price = price;
        this.foodId=foodId;

        if (shortdescription.trim().equals("")) {
            this.shortdescription = "Information is not provided";
        } else this.shortdescription = shortdescription;
    }

    public String getName() {
        return name;
    }

    public String getRestaurantUid() {
        return restaurantUid;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getPrice() {
        return price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public void setShortdescription(String shortdescription) {
        this.shortdescription = shortdescription;
    }

    public void setRestaurantUid(String restaurantUid) {
        this.restaurantUid = restaurantUid;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getShortdescription() {
        return shortdescription;
    }

    public String getImageUrl() {
        return imageUrl;
   }

}