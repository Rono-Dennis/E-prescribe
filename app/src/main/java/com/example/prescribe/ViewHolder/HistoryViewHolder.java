package com.example.prescribe.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prescribe.Interface.ItemClickListener;
import com.example.prescribe.R;


public class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView amountToBePaid,phones, dateAndTime,usersId,amountPaid, balanceRemain;
    public Button Show_all_order;
    private ItemClickListener itemClickListener;
    public HistoryViewHolder(@NonNull View itemView) {
        super(itemView);
        amountToBePaid=itemView.findViewById(R.id.AmountToBePaid);
        phones=itemView.findViewById(R.id.Phones);
        dateAndTime=itemView.findViewById(R.id.DateAndTime);
        usersId=itemView.findViewById(R.id.UsersId);
        amountPaid=itemView.findViewById(R.id.AmountPaid);
        balanceRemain=itemView.findViewById(R.id.BalanceRemain);
        Show_all_order=itemView.findViewById(R.id.show_all_orders);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
