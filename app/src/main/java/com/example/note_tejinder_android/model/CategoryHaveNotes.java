package com.example.note_tejinder_android.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class CategoryHaveNotes {

    @Embedded
    public Category category;

    @Relation(
            parentColumn = "categoryID",
            entityColumn = "noteCategoryID"
    )
    public List<Note> allNotes;

    public CategoryHaveNotes(Category category, List<Note> allNotes) {
        this.category = category;
        this.allNotes = allNotes;
    }

    public Category getCategory() {
        return category;
    }

    public List<Note> getAllNotes() {
        return allNotes;
    }
}
