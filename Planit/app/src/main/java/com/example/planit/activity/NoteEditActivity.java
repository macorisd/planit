package com.example.planit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.planit.R;
import com.example.planit.entity.note.NoteManager;

public class NoteEditActivity extends AppCompatActivity {

    private EditText editTextNoteTitle, editTextNoteContent;
    private Button buttonSaveNote, buttonDeleteNote;
    private NoteManager noteManager;
    private int noteId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);

        editTextNoteTitle = findViewById(R.id.editTextNoteTitle);
        editTextNoteContent = findViewById(R.id.editTextNoteContent);
        buttonSaveNote = findViewById(R.id.buttonSaveNote);
        buttonDeleteNote = findViewById(R.id.buttonDeleteNote);

        noteManager = new NoteManager(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("NOTE_ID")) {
            noteId = intent.getIntExtra("NOTE_ID", -1);
            String noteTitle = intent.getStringExtra("NOTE_TITLE");
            String noteContent = intent.getStringExtra("NOTE_CONTENT");

            editTextNoteTitle.setText(noteTitle);
            editTextNoteContent.setText(noteContent);
        }

        buttonSaveNote.setOnClickListener(v -> saveNote());
        buttonDeleteNote.setOnClickListener(v -> deleteNote());
    }

    private void saveNote() {
        String title = editTextNoteTitle.getText().toString();
        String content = editTextNoteContent.getText().toString();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (noteId == -1) {
            noteManager.createNote(title, content);
            Toast.makeText(this, "Note created successfully", Toast.LENGTH_SHORT).show();
        } else {
            noteManager.editNote(noteId, title, content);
            Toast.makeText(this, "Note updated successfully", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    private void deleteNote() {
        if (noteId != -1) {
            noteManager.deleteNote(noteId);
            Toast.makeText(this, "Note deleted successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error deleting note", Toast.LENGTH_SHORT).show();
        }
    }
}