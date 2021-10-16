package com.example.note_tejinder_android.utilities;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.note_tejinder_android.data.NoteDao;
import com.example.note_tejinder_android.model.Category;
import com.example.note_tejinder_android.model.Note;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Note.class, Category.class}, version = 1,exportSchema = true)
public abstract class NoteDataBase extends RoomDatabase {

    public abstract NoteDao noteDao();

    public static volatile NoteDataBase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;
    // executor service helps to do tasks in background thread
    public static final ExecutorService databaseWriteExecutor
            = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static NoteDataBase getInstance(final Context context){
        if(INSTANCE == null){
            synchronized(NoteDataBase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),NoteDataBase.class,"notedatabase").addCallback(sRoomDatabaseCallback).build();
                }
            }
        }
        return INSTANCE;
    }

    public static final RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    databaseWriteExecutor.execute(() ->{
                        NoteDao dao = INSTANCE.noteDao();
                            }

                    );
                }
            };

}
