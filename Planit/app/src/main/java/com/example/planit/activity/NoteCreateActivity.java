package com.example.planit.activity;

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

public class NoteCreateActivity extends AppCompatActivity {

    private EditText editTextNoteTitle, editTextNoteContent;
    private Button buttonSaveNote;
    private NoteManager noteManager;

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
        setContentView(R.layout.activity_note_create);

        // Initialize UI components
        editTextNoteTitle = findViewById(R.id.editTextNoteTitle);
        editTextNoteContent = findViewById(R.id.editTextNoteContent);
        buttonSaveNote = findViewById(R.id.buttonSaveNote);

        initNoteManager();

        // Set click listener for saving the note
        buttonSaveNote.setOnClickListener(v -> saveNote());
    }

    // Save the note after validation
    private void saveNote() {
        String title = editTextNoteTitle.getText().toString();
        String content = editTextNoteContent.getText().toString();

        // Validate title and content
        if (title.isEmpty() || content.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.dialog_error_title))
                    .setMessage(getString(R.string.dialog_fill_out_all_fields))
                    .setPositiveButton(getString(R.string.dialog_positive_button), null)
                    .show();
            return;
        }

        if (title.length() > 50) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.dialog_error_title))
                    .setMessage(getString(R.string.dialog_note_title_validation_error))
                    .setPositiveButton(getString(R.string.dialog_positive_button), null)
                    .show();
            return;
        }

        if (content.length() > 30000) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.dialog_error_title))
                    .setMessage(getString(R.string.dialog_note_content_validation_error))
                    .setPositiveButton(getString(R.string.dialog_positive_button), null)
                    .show();
            return;
        }

        try {
            noteManager.createNote(title, content);
            Toast.makeText(this, getString(R.string.toast_note_created), Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.dialog_error_title))
                    .setMessage(getString(R.string.dialog_note_create_error) + ": " + e.getMessage())
                    .setPositiveButton(getString(R.string.dialog_positive_button), null)
                    .show();
        }
    }
}
