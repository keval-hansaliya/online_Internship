package com.internship;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyHolder> {

    Context context;
    String[] nameArray, priceArray, descArray, idArray;
    int[] imageArray;
    SharedPreferences sp;

    public ProductAdapter(Context context, String[] nameArray, int[] imageArray, String[] priceArray, String[] descArray, String[] idArray) {
        this.context = context;
        this.nameArray = nameArray;
        this.imageArray = imageArray;
        this.priceArray = priceArray;
        this.descArray = descArray;
        this.idArray = idArray;
        sp = context.getSharedPreferences(ConstantSp.PREF, Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_product, parent, false);
        return new MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name, price, desc;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.custom_product_image);
            name = itemView.findViewById(R.id.custom_product_name);
            price = itemView.findViewById(R.id.custom_product_price);
//            desc = itemView.findViewById(R.id.product_detail_desc);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.imageView.setImageResource(imageArray[position]);
        holder.name.setText(nameArray[position]);
        holder.price.setText(ConstantSp.PRICE_SYMBOL+priceArray[position]);
//        holder.desc.setText(descArray[position]);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sp.edit().putString(ConstantSp.PRODUCT_NAME, nameArray[position]).commit();
                sp.edit().putInt(ConstantSp.PRODUCT_IMAGE, imageArray[position]).commit();
                sp.edit().putString(ConstantSp.PRODUCT_PRICE, priceArray[position]).commit();
                sp.edit().putString(ConstantSp.PRODUCT_DESC, descArray[position]).commit();
                sp.edit().putString(ConstantSp.PRODUCT_ID, idArray[position]).commit();

                new CommonMethod(context, ProductDetailActivity.class);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageArray.length;
    }
}
