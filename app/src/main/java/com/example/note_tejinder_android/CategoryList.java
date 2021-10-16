package com.example.note_tejinder_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.note_tejinder_android.adapter.CategoryAdapter;
import com.example.note_tejinder_android.model.Category;
import com.example.note_tejinder_android.model.NoteViewModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryList extends AppCompatActivity implements CategoryAdapter.OnCategoryClickListener {

    RecyclerView categoryRecycler;
    CategoryAdapter categoryAdapter;
    ImageButton createCat;
    List<Category> categories;
    Activity activity;
    NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        categoryRecycler = findViewById(R.id.categoryRecycler);
        createCat = findViewById(R.id.createCat);

        categoryRecycler.setHasFixedSize(true);

        categoryRecycler.setLayoutManager(new LinearLayoutManager(this));

        noteViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication()).create(NoteViewModel.class);

        categories = new ArrayList<>();

        noteViewModel.getAllCategories().observe(this, categoryList -> {
            categories.clear();
            categories = categoryList;

            categoryAdapter = new CategoryAdapter(categories,this,this);

            categoryRecycler.setAdapter(categoryAdapter);
            //categoryAdapter = new CategoryAdapter(categories,this,this);

        });

        createCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CategoryList.this);
                LayoutInflater layoutInflater = LayoutInflater.from(CategoryList.this);
                View view = layoutInflater.inflate(R.layout.create_category_dialogbox, null);
                builder.setView(view);

                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                EditText newCategoryName = view.findViewById(R.id.newCategoryName);
                Button create = view.findViewById(R.id.create);

                create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String categoryName = newCategoryName.getText().toString();
                        if (categoryName.isEmpty()) {
                            dialogBox("Please enter value for Category name");
                            return;
                        }
                        if (categories.contains(categoryName)) {
                            dialogBox("Category name already exist!");
                            return;
                        }
                        Category cat = new Category(categoryName);
                        noteViewModel.insertCategory(cat);
                        Log.i("MAinActivity", "jkjkjk: ");
                        alertDialog.dismiss();
                    }
                });
            }
        });

    }
    public void dialogBox(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CategoryList.this);
        builder.setTitle("Alert");
        builder.setMessage(message);

        builder.setCancelable(false);
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        categoryAdapter = new CategoryAdapter(categories,this,this);

        categoryRecycler.setAdapter(categoryAdapter);
    }

    @Override
    public void onCategoryClick(int position) {

    }
}