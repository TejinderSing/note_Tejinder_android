package com.example.note_tejinder_android.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.note_tejinder_android.model.Category;
import com.example.note_tejinder_android.model.Note;

import java.util.List;

@Dao
public abstract class NoteDao {

    @Insert
    public abstract void insertNote(Note note);

    @Insert
    public abstract void insertCategory(Category category);

    @Query("SELECT * FROM category")
    public abstract LiveData<List<Category>> getAllCategories();

    @Query("SELECT * FROM note")
    public abstract LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM category WHERE categoryID <> :categoryID")
    public abstract LiveData<List<Category>> getAllCategoriesBut(int categoryID);


    @Query("SELECT * FROM note WHERE noteCategoryId = :categoryID AND noteTitle LIKE  '%' || :searchKey || '%'  ORDER BY  " +
            "CASE  WHEN :isAsc = 1 THEN noteTitle END ASC, " +
            "CASE WHEN :isDesc = 1 THEN noteTitle END DESC, " +
            "CASE WHEN :byDate = 1 THEN noteDate END DESC"
    )
    public abstract LiveData<List<Note>> getNotesForCategory(int categoryID, boolean isAsc, boolean isDesc, String searchKey, boolean byDate);

    @Query("SELECT * FROM note WHERE noteCategoryId = :categoryID AND noteTitle LIKE :searchKey")
    public abstract List<Note> getNotesBySearch(int categoryID, String searchKey);


    @Query("SELECT * FROM note WHERE noteCategoryId = :categoryID")
    public abstract LiveData<List<Note>> getNotesForCategory(int categoryID);

    @Query("DELETE FROM note")
    public abstract void deleteAll();

    @Query("DELETE FROM note")
    public abstract void deleteAllNotes();

    @Query("DELETE FROM category WHERE categoryID = :categoryID")
    public abstract void deleteCategory(int categoryID);

    @Delete
    public abstract void deleteNote(Note note);

    @Update
    public abstract void update(Note note);

    @Update
    public abstract void updateCategory(Category category);

    @Query("SELECT * FROM note WHERE noteID = :id LIMIT 1")
    abstract public LiveData<Note> getNoteById(int id);

    @Query("SELECT * FROM category WHERE categoryID = :id LIMIT 1")
    public abstract LiveData<Category> getCategoryById(int id);




}
