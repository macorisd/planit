package com.example.planit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.planit.R;
import com.example.planit.entity.SingletonEntity;
import com.example.planit.entity.note.NoteManager;
import com.example.planit.fragment.NoteFragment;

public class NoteEditActivity extends AppCompatActivity {

    private EditText editTextNoteTitle, editTextNoteContent;
    private Button buttonSaveNote, buttonDeleteNote;
    private NoteManager noteManager;
    private int noteId = -1;

    // Initialize NoteManager
    private void initNoteManager() {
        noteManager = (NoteManager) SingletonEntity.getInstance().get(NoteFragment.SHARED_NOTE_LIST);
        if (noteManager == null) {
            noteManager = new NoteManager(this);
            SingletonEntity.getInstance().put(NoteFragment.SHARED_NOTE_LIST, noteManager);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);

        // Initialize UI components
        editTextNoteTitle = findViewById(R.id.editTextNoteTitle);
        editTextNoteContent = findViewById(R.id.editTextNoteContent);
        buttonSaveNote = findViewById(R.id.buttonSaveNote);
        buttonDeleteNote = findViewById(R.id.buttonDeleteNote);

        initNoteManager();

        // Get the note data from the Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("NOTE_ID")) {
            noteId = intent.getIntExtra("NOTE_ID", -1);
            String noteTitle = intent.getStringExtra("NOTE_TITLE");
            String noteContent = intent.getStringExtra("NOTE_CONTENT");

            editTextNoteTitle.setText(noteTitle);
            editTextNoteContent.setText(noteContent);
        }

        // Set button click listeners
        buttonSaveNote.setOnClickListener(v -> updateNote());
        buttonDeleteNote.setOnClickListener(v -> deleteNote());
    }

    // Update the note
    private void updateNote() {
        String title = editTextNoteTitle.getText().toString();
        String content = editTextNoteContent.getText().toString();

        // Validate input fields
        if (title.isEmpty() || content.isEmpty()) {
            showAlertDialog(R.string.dialog_fill_out_all_fields);
            return;
        }

        if (title.length() > 50) {
            showAlertDialog(R.string.dialog_note_title_validation_error);
            return;
        }

        if (content.length() > 30000) {
            showAlertDialog(R.string.dialog_note_content_validation_error);
            return;
        }

        // Proceed with note update
        if (noteId != -1) {
            noteManager.editNote(noteId, title, content);
            Toast.makeText(this, getString(R.string.toast_note_updated), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            showAlertDialog(R.string.dialog_note_update_error);
        }
    }

    // Delete the note
    private void deleteNote() {
        if (noteId != -1) {
            noteManager.deleteNote(noteId);
            Toast.makeText(this, getString(R.string.toast_note_deleted), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            showAlertDialog(R.string.dialog_note_delete_error);
        }
    }

    // Show an alert dialog with the provided message resource ID
    private void showAlertDialog(int messageResId) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_error_title))
                .setMessage(getString(messageResId))
                .setPositiveButton(getString(R.string.dialog_positive_button), null)
                .show();
    }
}
