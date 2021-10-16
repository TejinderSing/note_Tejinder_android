package com.example.note_tejinder_android.data;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.note_tejinder_android.model.Category;
import com.example.note_tejinder_android.model.Note;
import com.example.note_tejinder_android.utilities.NoteDataBase;

import java.util.List;

public class NoteRepository {

    public NoteDao noteDao;

    public LiveData<List<Note>> allNotes;
    public LiveData<List<Category>> allCategories;

    public NoteRepository(Application application) {
        NoteDataBase db = NoteDataBase.getInstance(application);
        noteDao = db.noteDao();
        allNotes = noteDao.getAllNotes();
        allCategories = noteDao.getAllCategories();
    }

    LiveData<List<Note>> getAllNotes(){
        return allNotes;
    }

    public void insertNote(Note note){
        NoteDataBase.databaseWriteExecutor.execute(()->noteDao.insertNote(note));
    }

    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }

    public LiveData<List<Category>> getAllCategoriesBut(int cateId) {
        return noteDao.getAllCategoriesBut(cateId);
    }



    public void insertCategory(Category category) {
        NoteDataBase.databaseWriteExecutor.execute(() -> noteDao.insertCategory(category));
        Log.i("Repo", "jkjkjk: ");
    }



    public void delete(Note note) {
        NoteDataBase.databaseWriteExecutor.execute(() -> noteDao.deleteNote(note));
    }
    public void deleteCategory(int cateId) {
        NoteDataBase.databaseWriteExecutor.execute(() -> noteDao.deleteCategory(cateId));
    }
    public LiveData<List<Note>> getNotesByCategory(int categoryID, boolean isAsc, boolean isDesc, String searchKey, boolean byDate) {
        return noteDao.getNotesForCategory(categoryID, isAsc, isDesc, searchKey, byDate);
    }

    // Get notes by Category
    public LiveData<List<Note>> getNotesByCategory(int categoryID) {
        return noteDao.getNotesForCategory(categoryID);
    }

    // updates a note
    public void update(Note note) {
        NoteDataBase.databaseWriteExecutor.execute(() -> noteDao.update(note));
    }

    // updates a note
    public void updateCategory(Category category) {
        NoteDataBase.databaseWriteExecutor.execute(() -> noteDao.updateCategory(category));
    }


    // Get a note by Id
    public LiveData<Note> getNoteById(int id) {
        return noteDao.getNoteById(id);
    }  public LiveData<Category> getCategoryById(int id) {
        return noteDao.getCategoryById(id);
    }
}
