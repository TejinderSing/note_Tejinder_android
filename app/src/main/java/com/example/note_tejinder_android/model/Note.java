package com.example.note_tejinder_android.model;


import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "note",foreignKeys = @ForeignKey(entity = Category.class,parentColumns = "categoryID",childColumns = "noteCategoryID",onDelete = CASCADE))
public class Note {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "noteID")
    public int noteID;

    @ColumnInfo(name = "noteCategoryID")
    public int noteCategoryID;

    @NonNull
    @ColumnInfo(name = "noteTitle")
    public String noteTitle;

    @NonNull
    @ColumnInfo(name = "noteDetail")
    public String noteDetail;

    @NonNull
    @ColumnInfo(name = "noteDate")
    public String noteDate;

    @ColumnInfo(name = "noteImage")
    public byte[] noteImage;

    @ColumnInfo(name = "longitude")
    public double longitude;

    @ColumnInfo(name = "latitude")
    public double latitude;

    public Note() {
    }

//    public Note(@NonNull String noteTitle, @NonNull String noteDetail, @NonNull String noteDate, byte[] noteImage) {
//        this.noteTitle = noteTitle;
//        this.noteDetail = noteDetail;
//        this.noteDate = noteDate;
//        this.noteImage = noteImage;
//    }

    public Note(int noteCategoryID, @NonNull String noteTitle, @NonNull String noteDetail, @NonNull String noteDate, byte[] noteImage, double longitude, double latitude) {
        this.noteCategoryID = noteCategoryID;
        this.noteTitle = noteTitle;
        this.noteDetail = noteDetail;
        this.noteDate = noteDate;
        this.noteImage = noteImage;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public int getNoteCategoryID() {
        return noteCategoryID;
    }

    public void setNoteCategoryID(int noteCategoryID) {
        this.noteCategoryID = noteCategoryID;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getNoteID() {
        return noteID;
    }

    public void setNoteID(int noteID) {
        this.noteID = noteID;
    }

    @NonNull
    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(@NonNull String noteTitle) {
        this.noteTitle = noteTitle;
    }

    @NonNull
    public String getNoteDetail() {
        return noteDetail;
    }

    public void setNoteDetail(@NonNull String noteDetail) {
        this.noteDetail = noteDetail;
    }

    @NonNull
    public String getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(@NonNull String noteDate) {
        this.noteDate = noteDate;
    }

    public byte[] getNoteImage() {
        return noteImage;
    }

    public void setNoteImage(byte[] noteImage) {
        this.noteImage = noteImage;
    }
}
