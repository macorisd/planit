package com.example.planit.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.planit.R;
import com.example.planit.entity.SingletonEntity;
import com.example.planit.entity.subject.SubjectManager;
import com.example.planit.fragment.SubjectFragment;

import java.util.HashMap;
import java.util.Map;

public class SubjectEditActivity extends AppCompatActivity {
    private EditText editTextSubjectName;
    private Spinner spinnerSubjectColor;
    private Button buttonSaveSubject, buttonDeleteSubject;
    private SubjectManager subjectManager;
    private int subjectId = -1; // Subject ID, -1 if no subject is selected
    private int selectedColorValue = 0; // Default color value

    private Map<String, Integer> colorMap; // Color map to store color names and values

    // Initializes the SubjectManager from the SingletonEntity or creates a new one
    private void initSubjectManager() {
        subjectManager = (SubjectManager) SingletonEntity.getInstance().get(SubjectFragment.SHARED_SUBJECT_LIST);
        if (subjectManager == null) {
            subjectManager = new SubjectManager(this);
            SingletonEntity.getInstance().put(SubjectFragment.SHARED_SUBJECT_LIST, subjectManager);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_edit);

        // Initialize the UI components
        editTextSubjectName = findViewById(R.id.editTextSubjectName);
        spinnerSubjectColor = findViewById(R.id.spinnerSubjectColor);
        buttonSaveSubject = findViewById(R.id.buttonSaveSubject);
        buttonDeleteSubject = findViewById(R.id.buttonDeleteSubject);

        initSubjectManager(); // Initialize the SubjectManager
        initializeColorMap(); // Initialize color options for the subject

        // Create and set up an adapter for the color spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, colorMap.keySet().toArray(new String[0]));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubjectColor.setAdapter(adapter);

        // Handle selection changes in the color spinner
        spinnerSubjectColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, android.view.View selectedItemView, int position, long id) {
                selectedColorValue = colorMap.get(parentView.getItemAtPosition(position)); // Set the selected color
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        // Retrieve subject details passed from the previous activity
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("SUBJECT_ID")) {
            subjectId = intent.getIntExtra("SUBJECT_ID", -1); // Get the subject ID
            String subjectName = intent.getStringExtra("SUBJECT_NAME");
            int subjectColor = intent.getIntExtra("SUBJECT_COLOR", 0);

            // Set the subject name and color in the UI
            editTextSubjectName.setText(subjectName);

            if (subjectColor != 0 && colorMap.containsValue(subjectColor)) {
                for (Map.Entry<String, Integer> entry : colorMap.entrySet()) {
                    if (entry.getValue().equals(subjectColor)) {
                        spinnerSubjectColor.setSelection(getKeyIndex(entry.getKey())); // Set the correct color in the spinner
                        break;
                    }
                }
            }
        }

        // Set up the click listeners for saving or deleting the subject
        buttonSaveSubject.setOnClickListener(v -> updateSubject());
        buttonDeleteSubject.setOnClickListener(v -> deleteSubject());
    }

    // Update the subject with the new name and color
    private void updateSubject() {
        String name = editTextSubjectName.getText().toString();

        // Validate the subject name
        if (name.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.dialog_error_title))
                    .setMessage(getString(R.string.dialog_fill_out_all_fields))
                    .setPositiveButton(getString(R.string.dialog_positive_button), null)
                    .show();
            return;
        }

        // Validate the length of the subject name
        if (name.length() < 3 || name.length() > 50) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.dialog_error_title))
                    .setMessage(getString(R.string.dialog_subject_name_validation_error))
                    .setPositiveButton(getString(R.string.dialog_positive_button), null)
                    .show();
            return;
        }

        // Get the selected color value
        int color = selectedColorValue;

        // Update the subject in the SubjectManager
        if (subjectId != -1) {
            subjectManager.editSubject(subjectId, name, color);
            Toast.makeText(this, getString(R.string.toast_subject_updated), Toast.LENGTH_SHORT).show();
            finish(); // Close the activity after saving
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.dialog_error_title))
                    .setMessage(getString(R.string.dialog_subject_update_error))
                    .setPositiveButton(getString(R.string.dialog_positive_button), null)
                    .show();
        }
    }

    // Delete the subject from the SubjectManager
    private void deleteSubject() {
        if (subjectId != -1) {
            subjectManager.deleteSubject(subjectId);
            Toast.makeText(this, getString(R.string.toast_subject_deleted), Toast.LENGTH_SHORT).show();
            finish(); // Close the activity after deletion
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.dialog_error_title))
                    .setMessage(getString(R.string.dialog_subject_delete_error))
                    .setPositiveButton(getString(R.string.dialog_positive_button), null)
                    .show();
        }
    }

    // Initialize the color map with predefined color options
    private void initializeColorMap() {
        colorMap = new HashMap<>();
        colorMap.put(getString(R.string.color_red), Color.parseColor("#FF0000"));
        colorMap.put(getString(R.string.color_green), Color.parseColor("#00FF00"));
        colorMap.put(getString(R.string.color_blue), Color.parseColor("#0000FF"));
        colorMap.put(getString(R.string.color_yellow), Color.parseColor("#FFFF00"));
        colorMap.put(getString(R.string.color_purple), Color.parseColor("#800080"));
        colorMap.put(getString(R.string.color_cyan), Color.parseColor("#00FFFF"));
        colorMap.put(getString(R.string.color_orange), Color.parseColor("#FFA500"));
        colorMap.put(getString(R.string.color_pink), Color.parseColor("#FFC0CB"));
        colorMap.put(getString(R.string.color_black), Color.parseColor("#000000"));
        colorMap.put(getString(R.string.color_white), Color.parseColor("#FFFFFF"));
    }

    // Helper method to find the index of a color key in the map
    private int getKeyIndex(String key) {
        String[] keys = colorMap.keySet().toArray(new String[0]);
        for (int i = 0; i < keys.length; i++) {
            if (keys[i].equals(key)) {
                return i;
            }
        }
        return 0; // Default to the first item if no match is found
    }
}
