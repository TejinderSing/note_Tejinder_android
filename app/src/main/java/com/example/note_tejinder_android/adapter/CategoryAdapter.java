package com.example.note_tejinder_android.adapter;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.note_tejinder_android.MainActivity;
import com.example.note_tejinder_android.R;
import com.example.note_tejinder_android.model.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {


    private static final String TAG = "MainActivity";
    public List<Category> categoryList;
    public Context context;
    OnCategoryClickListener onCategoryClickListener;
    String ss = "";

    public CategoryAdapter(List<Category> categoryList, Context context, OnCategoryClickListener onCategoryClickListener) {
        this.categoryList = categoryList;
        this.context = context;
        this.onCategoryClickListener = onCategoryClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categoryrow,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(categoryList.isEmpty()){
            return;
        }
        else {
            Log.i(TAG, "Adapter: ");
            System.out.println(position);

            Category cat = categoryList.get(position);
            ss = cat.getCategoryName();
            System.out.println(ss);
            holder.categoryName.setText(ss);

            holder.categoryName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("categoryID", cat.getCategoryID());
                    intent.putExtra("categoryName", cat.getCategoryName());
                    context.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements OnCategoryClickListener {
        TextView categoryName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.rowCategoryName);

        }

        @Override
        public void onCategoryClick(int position) {
            onCategoryClickListener.onCategoryClick(getAdapterPosition());
        }
    }

    public interface OnCategoryClickListener{
        void onCategoryClick(int position);
    }
}
