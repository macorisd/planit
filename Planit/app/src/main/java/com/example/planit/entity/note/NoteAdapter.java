package com.example.planit.entity.note;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planit.R;
import com.example.planit.activity.NoteEditActivity;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private List<Note> notes;

    // Constructor that accepts a list of notes
    public NoteAdapter(List<Note> notes) {
        this.notes = notes;
    }

    // Create a new view holder for each item in the RecyclerView
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    // Bind data to the view holder at the specified position
    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);

        // Set the title and content of the note to the respective TextViews
        holder.titleTextView.setText(note.getTitle());
        String contentText = (note.getContent().length() > 100)
                ? note.getContent().substring(0, 100) + " [...]"
                : note.getContent();
        holder.contentTextView.setText(contentText);

        // Set an OnClickListener to navigate to NoteEditActivity when the note item is clicked
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), NoteEditActivity.class);
            // Pass the note's data to the edit activity
            intent.putExtra("NOTE_ID", note.getId());
            intent.putExtra("NOTE_TITLE", note.getTitle());
            intent.putExtra("NOTE_CONTENT", note.getContent());
            v.getContext().startActivity(intent);
        });
    }

    // Return the total number of notes in the list
    @Override
    public int getItemCount() {
        return notes.size();
    }

    // ViewHolder class to hold the views for each item in the RecyclerView
    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView contentTextView;

        // Constructor to initialize the TextViews
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.noteTitle);
            contentTextView = itemView.findViewById(R.id.noteContent);
        }
    }
}
