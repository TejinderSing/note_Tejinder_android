package com.example.note_tejinder_android.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.note_tejinder_android.data.NoteRepository;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    public NoteRepository noteRepository;
    public LiveData<List<Note>> allNotes;
    public final LiveData<List<Category>> allCategories;
    public NoteViewModel(@NonNull Application application) {
        super(application);
        noteRepository = new NoteRepository(application);
        allNotes = noteRepository.allNotes;
        allCategories = noteRepository.allCategories;
    }
    public LiveData<List<Note>> getAllNotes(){
        return allNotes;
    }
    public void insertNote(Note note){
        noteRepository.insertNote(note);
    }
    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }

    public LiveData<List<Category>> getAllCategoriesBut(int categoryID) {
        return noteRepository.getAllCategoriesBut(categoryID);
    }



    public LiveData<List<Note>> getNotesByCategory(int categoryID, boolean isAsc, boolean isDesc, String searchKey, boolean byDate) {
        allNotes = noteRepository.getNotesByCategory(categoryID, isAsc, isDesc, searchKey, byDate);
        return allNotes;
    }

    public LiveData<List<Note>> getNotesByCategory(int categoryID) {
        allNotes = noteRepository.getNotesByCategory(categoryID);
        return allNotes;
    }


    public void insertCategory(Category category) {
        noteRepository.insertCategory(category);
    }


    public void delete(Note note) {
        noteRepository.delete(note);
    }
    public void deleteCategory(int categoryID) {
        noteRepository.deleteCategory(categoryID);
    }
    public void update(Note note) {
        noteRepository.update(note);
    }

    public void updateCategory(Category category) {
        noteRepository.updateCategory(category);
    }

    public LiveData<Note> getNoteById(int id) {
        return noteRepository.getNoteById(id);
    }
    public LiveData<Category> getCategoryById(int id) {
        return noteRepository.getCategoryById(id);
    }
}