package com.example.customer.recyclers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import com.example.customer.CartInfo;
import com.example.customer.FoodDetailsActivity;
import com.example.customer.OrderdFood;
import com.example.customer.R;
import com.example.customer.UserCartActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerView_Carts {

    private Context mContext;
    private CartsAdapter mCartsAdapter;

    public void setConfig(RecyclerView recyclerView, Context context, List<CartInfo> cartInfos, List<String> keys) {
        mContext = context;
        mCartsAdapter = new CartsAdapter(cartInfos, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mCartsAdapter);
    }

    class CartsItemView extends RecyclerView.ViewHolder {
        private ImageView mImage;
        private TextView mName;
        private TextView mComment;
        private TextView mCart;
        private String key;
        private String restaurantId;
        private TextView mTotalPrice;
        private TextView mTotalItems;


        public CartsItemView(ViewGroup parent) {
            super(LayoutInflater.from(mContext).
                    inflate(R.layout.restaurant_cart, parent, false));
            mName = itemView.findViewById(R.id.tvRestaurantName);
            mComment = itemView.findViewById(R.id.tvRestaurantComment);
            mCart = itemView.findViewById(R.id.tvCart);
            mImage = itemView.findViewById(R.id.imgRestaurant);
            mTotalPrice = itemView.findViewById(R.id.tvTotalPrc);
            mTotalItems = itemView.findViewById(R.id.tvTotalItems);
            //Item set click on it for open list
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, UserCartActivity.class);
                    intent.putExtra("key", key);
                    intent.putExtra("restaurantId", restaurantId);
                    mContext.startActivity(intent);
                }
            });

        }

        public void bind(CartInfo cartInfo, String key) {

            mName.setText(cartInfo.getRestaurantName());
            mComment.setText(cartInfo.getRestaurantComment());
            restaurantId=cartInfo.getRestaurantId();
            mTotalPrice.setText(cartInfo.getTotalPrice()+" €");
            mTotalItems.setText(cartInfo.getTotalItems());
            this.key = key;
            Picasso.get()
                    .load(cartInfo.getRestaurantImage())
                    .placeholder(R.drawable.default_food)
                    .fit()
                    .centerCrop()
                    .into(mImage);
        }
    }

    class CartsAdapter extends RecyclerView.Adapter<CartsItemView> {
        private List<CartInfo> mCartList;
        private List<String> mKeys;

        public CartsAdapter(List<CartInfo> mCartList, List<String> mKeys) {
            this.mCartList = mCartList;
            this.mKeys = mKeys;
        }


        @Override
        public CartsItemView onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CartsItemView(parent);
        }

        @Override
        public void onBindViewHolder(CartsItemView holder, int position) {
            holder.bind(mCartList.get(position), mKeys.get(position));
        }

        //........

        @Override
        public int getItemCount() {
            return mCartList.size();
        }
    }

}

