package com.example.prescribe.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prescribe.Interface.ItemClickListener;
import com.example.prescribe.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView Order_user_name;
    public TextView Order_phone_number;
    public TextView Order_total_price;
    public TextView Order_address;
    public TextView Order_city;
    public TextView Order_date;
    public TextView Order_time;
    public Button Show_all_orders;
    private ItemClickListener itemClickListener;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
        Order_user_name=itemView.findViewById(R.id.order_user_name);
        Order_phone_number=itemView.findViewById(R.id.order_phone_number);
        Order_total_price=itemView.findViewById(R.id.order_total_price);
        Order_address=itemView.findViewById(R.id.order_address);
        Order_city=itemView.findViewById(R.id.order_city);
        Order_date=itemView.findViewById(R.id.order_date);
        Order_time=itemView.findViewById(R.id.order_time);
        Show_all_orders=itemView.findViewById(R.id.show_all_orders);
    }

    @Override
    public void onClick(View v)
    {
        itemClickListener.onClick(v, getAdapterPosition(),false);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
