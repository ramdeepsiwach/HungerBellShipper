package com.se_p2.hungerbellshipper.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.se_p2.hungerbellshipper.common.Common;
import com.se_p2.hungerbellshipper.model.ShippingOrderModel;
import com.se_p2.hungerbellshipper.R;
import com.se_p2.hungerbellshipper.ShippingActivity;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.paperdb.Paper;

public class MyShippingOrderAdapter extends RecyclerView.Adapter<MyShippingOrderAdapter.MyViewHolder> {

    Context context;
    List<ShippingOrderModel> shippingOrderModelList;
    SimpleDateFormat simpleDateFormat;

    public MyShippingOrderAdapter(Context context, List<ShippingOrderModel> shippingOrderModelList) {
        this.context = context;
        this.shippingOrderModelList = shippingOrderModelList;
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
        Paper.init(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_order_shipper, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(shippingOrderModelList.get(position).getOrderModel().getCartItemList()
                .get(0).getFoodImage()).into(holder.img_food);
        holder.txt_date.setText(new StringBuilder(simpleDateFormat.format(shippingOrderModelList.get(position)
                .getOrderModel().getCreateDate())));
        Common.setSpanStringColor("No. ",shippingOrderModelList.get(position).getOrderModel()
        .getKey(),holder.txt_order_number, Color.parseColor("#BA454A"));
        Common.setSpanStringColor("Address: ",shippingOrderModelList.get(position).getOrderModel()
                .getShippingAddress(),holder.txt_order_address, Color.parseColor("#BA454A"));
        Common.setSpanStringColor("Payment: ",shippingOrderModelList.get(position).getOrderModel()
                .getTransactionID(),holder.txt_payment, Color.parseColor("#BA454A"));

        if(shippingOrderModelList.get(position).isStartTrip()){
            holder.btn_ship_now.setEnabled(false);
        }

        //Event
        holder.btn_ship_now.setOnClickListener(view -> {
            Paper.book().write(Common.SHIPPING_ORDER_DATA,new Gson().toJson(shippingOrderModelList.get(position)));
            context.startActivity(new Intent(context, ShippingActivity.class));

        });

    }

    @Override
    public int getItemCount() {
        return shippingOrderModelList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final Unbinder unbinder;

        @BindView(R.id.txt_date)
        TextView txt_date;
        @BindView(R.id.txt_order_address)
        TextView txt_order_address;
        @BindView(R.id.txt_order_number)
        TextView txt_order_number;
        @BindView(R.id.txt_payment)
        TextView txt_payment;
        @BindView(R.id.img_food)
        ImageView img_food;
        @BindView(R.id.btn_ship_now)
        MaterialButton btn_ship_now;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
        }
    }
}
