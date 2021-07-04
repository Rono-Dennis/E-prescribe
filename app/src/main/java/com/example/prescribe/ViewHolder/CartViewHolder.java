package com.example.prescribe.ViewHolder;


import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prescribe.Interface.ItemClickListener;
import com.example.prescribe.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
       public  TextView txtProductName, txtProductPrice, txtProductQuantity;
       private ItemClickListener itemClickListener;
       public Button Show_all_order, Show_all_orders,Show_all_pay;

    public CartViewHolder(@NonNull View itemView)
    {
        super(itemView);

        txtProductName=itemView.findViewById(R.id.cart_product_name);
        txtProductPrice=itemView.findViewById(R.id.cart_product_price);
        txtProductQuantity=itemView.findViewById(R.id.cart_product_quantity);
        Show_all_order=itemView.findViewById(R.id.show_all_order);
        Show_all_orders=itemView.findViewById(R.id.show_all_orders);
        Show_all_pay=itemView.findViewById(R.id.show_all_payment);

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
