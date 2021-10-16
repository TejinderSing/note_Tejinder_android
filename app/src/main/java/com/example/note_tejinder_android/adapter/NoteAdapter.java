package com.example.note_tejinder_android.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.note_tejinder_android.AddNote;
import com.example.note_tejinder_android.MainActivity;
import com.example.note_tejinder_android.R;
import com.example.note_tejinder_android.model.Note;

import java.util.List;

public class NoteAdapter<T> extends RecyclerView.Adapter<NoteAdapter<T>.ViewHolder> {

    public List<T> noteList;
    public Context context;
    OnNoteClickListener onNoteClickListener;
    String categoryName;


    public NoteAdapter(List<T> noteList, Context context, OnNoteClickListener onNoteClickListener, String categoryName) {
        this.noteList = noteList;
        this.context = context;
        this.onNoteClickListener = onNoteClickListener;
        this.categoryName = categoryName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        T t = noteList.get(position);
        Note note = (Note) t;
        holder.noteTitle.setText(note.getNoteTitle());
        holder.noteDate.setText(note.getNoteDate());
        holder.noteCategory.setText(categoryName);
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddNote.class);
                intent.putExtra("noteID",note.getNoteID());
                intent.putExtra("noteCategoryID",note.getNoteCategoryID());
                intent.putExtra("noteTitle",note.getNoteTitle());
                intent.putExtra("noteDetail",note.getNoteDetail());
                intent.putExtra("noteImage",note.getNoteImage());
                intent.putExtra("longitude",note.getLongitude());
                intent.putExtra("latitude",note.getLatitude());
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView noteTitle;
        TextView noteDate,noteCategory;
        LinearLayout edit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.rowNoteTitle);
            noteDate = itemView.findViewById(R.id.rowNoteDate);
            noteCategory = itemView.findViewById(R.id.noteCategory);
            edit = itemView.findViewById(R.id.edit);
        }


        @Override
        public void onClick(View v) {
            onNoteClickListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteClickListener {
            void onNoteClick(int position);
    }
}
