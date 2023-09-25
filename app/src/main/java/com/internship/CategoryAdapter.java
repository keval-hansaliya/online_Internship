package com.internship;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyHolder> {
    Context context;
    String[] Category_name_array;
    int[] Category_image_array;

    public CategoryAdapter(FragmentActivity activity, String[] Category_name_array, int[] Category_image_array) {
        this.context = activity;
        this.Category_name_array = Category_name_array;
        this.Category_image_array = Category_image_array;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_category, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.name.setText(Category_name_array[position]);
        holder.imageView.setImageResource(Category_image_array[position]);
    }

    @Override
    public int getItemCount() {
        return Category_image_array.length;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView name;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.custom_category_image);
            name = itemView.findViewById(R.id.custom_category_name);
        }
    }
}
