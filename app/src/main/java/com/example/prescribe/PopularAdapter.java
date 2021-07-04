package com.example.prescribe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prescribe.Interface.ItemClickListner;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class PopularAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView pname, description, price;
    public ImageView image;
    public ItemClickListner listner;

    public PopularAdapter(@NonNull View itemView) {
        super(itemView);
            pname=itemView.findViewById(R.id.drugName);
            description=itemView.findViewById(R.id.drugDescription);
            price=itemView.findViewById(R.id.drugPrice);
            image=itemView.findViewById(R.id.drugImage);
    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
//        Popular popularCur = mPopulars.get(position);
//        holder.pname.setText(popularCur.getPname());
//        holder.description.setText(popularCur.getDescription());
//        holder.price.setText(popularCur.getPrice());
//        Picasso.get().load(popularCur.getImage()).into((Target) holder.image);
//


//    }


    @Override
    public void onClick(View v) {
        listner.onClick(v, getAdapterPosition(), false);
    }
    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }


//    public class ImageViewHolder extends RecyclerView.ViewHolder {
//
//        public TextView pname, description, price, image;
//        public ImageViewHolder(@NonNull View itemView) {
//            super(itemView);
//            pname=itemView.findViewById(R.id.drugName);
//            description=itemView.findViewById(R.id.drugDescription);
//            price=itemView.findViewById(R.id.drugPrice);
//            image=itemView.findViewById(R.id.drugImage);
//
//        }
//    }
}
