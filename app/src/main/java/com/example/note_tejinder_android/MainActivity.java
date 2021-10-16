package com.example.note_tejinder_android;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.note_tejinder_android.adapter.NoteAdapter;
import com.example.note_tejinder_android.model.Category;
import com.example.note_tejinder_android.model.Note;
import com.example.note_tejinder_android.model.NoteViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NoteAdapter.OnNoteClickListener {

    RecyclerView noteList;
    public NoteViewModel noteViewModel;
    NoteAdapter noteAdapter;
    List<Note> allNotesList;
    ImageButton newNote,back;
    int categoryID1;
    String categoryName1;
    TextView noteListTitle;
    android.widget.SearchView searchView;
    String searchKey = "";
    Button aces,dec,date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteList = findViewById(R.id.notesRecycler);
        newNote = findViewById(R.id.addNote);
        noteListTitle = findViewById(R.id.catName);
        searchView = findViewById(R.id.searchView);
        aces = findViewById(R.id.aces);
        dec = findViewById(R.id.des);
        date = findViewById(R.id.date);

        aces.setOnClickListener(v -> getNoteLists(true,false,false));
        dec.setOnClickListener(v -> getNoteLists(false,true,false));
        date.setOnClickListener(v -> getNoteLists(false,false,true));


        categoryID1 = getIntent().getIntExtra("categoryID",0);
        categoryName1 = getIntent().getStringExtra("categoryName");

        noteListTitle.setText(categoryName1);


//        noteList.setHasFixedSize(true);
        noteList.setLayoutManager(new LinearLayoutManager(this));

        noteViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication()).create(NoteViewModel.class);

        allNotesList = new ArrayList<>();

        noteViewModel.getNotesByCategory(categoryID1).observe(this,allNotes->{
            allNotesList.clear();
            allNotesList = allNotes;
            noteAdapter = new NoteAdapter(allNotesList,this,this,categoryName1);
            noteList.setAdapter(noteAdapter);
        });

        back = findViewById(R.id.backToCat);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        newNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddNote.class);
                intent.putExtra("noteCategoryID",categoryID1);
                startActivity(intent);
//                launcher.launch(intent);
            }
        });


    }

//    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(), result -> {
//                if(result.getResultCode() == Activity.RESULT_OK){
//                    Intent data = result.getData();
//                    String title = data.getStringExtra(AddNote.NOTE_TITLE);
//                    String detail = data.getStringExtra(AddNote.NOTE_DETAIL);
//
//                    // getting the current date
//                    Calendar cal = Calendar.getInstance();
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
//                    String date = sdf.format(cal.getTime());
//
//                    Note note = new Note(title,detail,date);
//
//                    noteViewModel.insertNote(note);
//
//                }
//            }
//    );

    @Override
    protected void onResume() {
        super.onResume();

        noteAdapter = new NoteAdapter(allNotesList,this,this,categoryName1);
        noteList.setAdapter(noteAdapter);

        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchKey = newText;
                getNoteLists(true, false, false);
                return false;
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(noteList);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();

            switch (direction) {
                case ItemTouchHelper.LEFT:
                    // confirmation dialog to ask user before delete contact
                    AlertDialog.Builder builderL = new AlertDialog.Builder(MainActivity.this);
                    builderL.setTitle("Are you sure you want to delete this note?");
                    builderL.setPositiveButton("Yes", (dialog, which) -> {
                        noteViewModel.delete(allNotesList.get(position));
                    });
                    builderL.setNegativeButton("No", (dialog, which) -> noteAdapter.notifyDataSetChanged());
                    AlertDialog alertDialogL = builderL.create();
                    alertDialogL.show();
                    break;
                case ItemTouchHelper.RIGHT:

                    noteViewModel.getAllCategories().observe(MainActivity.this, categories -> {
                        if (categories.size() > 1) {
                            AlertDialog.Builder builderR = new AlertDialog.Builder(MainActivity.this);
                            LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
                            View view = layoutInflater.inflate(R.layout.move_note_dialog, null);
                            builderR.setView(view);

                            final AlertDialog alertDialogR = builderR.create();
                            alertDialogR.show();

                            Spinner otherCategoriesSp = view.findViewById(R.id.otherCategoriesSp);
                            List<Category> otherCats = categories;
                            List<String> catNames= new ArrayList<>();
                            for (Category category :categories){
                                catNames.add(category.getCategoryName());
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, catNames);
                            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

                            otherCategoriesSp.setAdapter(adapter);
                            Button btnChange = view.findViewById(R.id.changeCategory);

                            btnChange.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final Note note = allNotesList.get(position);
                                    final Category category = otherCats.get(otherCategoriesSp.getSelectedItemPosition());
                                    note.setNoteCategoryID(category.getCategoryID());
                                    noteViewModel.update(note);
                                }
                            });
                        } else {
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("Alert");
                            builder.setMessage("This is the only category");

                            builder.setCancelable(false);
                            builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            builder.create().show();
                        }
                    });
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView
                recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
        }
    };

    public void getNoteLists(boolean isAsc, boolean isDesc, boolean byDate) {

        noteViewModel.getNotesByCategory(categoryID1, isAsc, isDesc, searchKey, byDate).observe(this, notes -> {
            allNotesList.clear();
            allNotesList.addAll(notes);



            noteAdapter.notifyDataSetChanged();

        });

    }


    @Override
    public void onNoteClick(int position) {

    }
}