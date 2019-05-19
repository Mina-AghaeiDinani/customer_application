package com.example.customer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerView_Ordered_Config {

    private Context mContext;
    private FoodsAdapter mFoodsAdapter;
    //Last Update
    public void setConfig (RecyclerView recyclerView, Context context,List<OrderdFood> orderdFoods, List <String> keys){
        mContext=context;
        mFoodsAdapter = new FoodsAdapter(orderdFoods,keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mFoodsAdapter);
    }

    class OrderdFoodItemView extends RecyclerView.ViewHolder {
        private TextView mFoodname;
        private TextView mNumber;
        private TextView mPrice;
        private TextView mTotal;
        private TextView mComment;

        private String key;


        public OrderdFoodItemView (ViewGroup parent){
            super(LayoutInflater.from(mContext).
                    inflate(R.layout.cart_items,parent,false));
            mFoodname=  itemView.findViewById(R.id.tvOrderedFood);
            mNumber=itemView.findViewById(R.id.tvOrderedItem);
            mPrice=  itemView.findViewById(R.id.tvOrderedPrice);
            mTotal=  itemView.findViewById(R.id.tvTotalPrice);
            mComment=  itemView.findViewById(R.id.tvComment);

            //Item set click on it for delete or edit

           // itemView.setOnClickListener(new View.OnClickListener() {
             //   @Override
             //   public void onClick(View v) {
            //         //do whatever you like to happen by pressing item
              //      Intent intent = new Intent(mContext,FoodDetailsActivity.class);
              //      intent.putExtra("key",key);
              //      mContext.startActivity(intent);
             //   }
         //   });

        }
        public void bind(OrderdFood orderdFood, String key) {

            mFoodname.setText(orderdFood.getFoodName());
            mPrice.setText(orderdFood.getFoodPrice()+" € •");
            mNumber.setText(orderdFood.getNumber()+" Item(s) • ");
            mTotal.setText("Total price: "+orderdFood.getTotalPrice()+" € •");
            mComment.setText(orderdFood.getComment());
            this.key = key;
        }
    }
    class FoodsAdapter extends RecyclerView.Adapter<OrderdFoodItemView >{
        private List<OrderdFood> mOrderdFoodList ;
        private List <String> mKeys;

        public FoodsAdapter(List <OrderdFood> mOrderdFoodList , List <String> mKeys){
            this.mOrderdFoodList  = mOrderdFoodList ;
            this.mKeys = mKeys;
        }


        @Override
        public OrderdFoodItemView  onCreateViewHolder(ViewGroup parent, int viewType) {
            return new OrderdFoodItemView (parent);
        }

        @Override
        public void onBindViewHolder(OrderdFoodItemView  holder, int position) {
            holder.bind(mOrderdFoodList .get(position),mKeys.get(position));
        }


        @Override
        public int getItemCount() {

            return mOrderdFoodList .size();
        }
    }

}

