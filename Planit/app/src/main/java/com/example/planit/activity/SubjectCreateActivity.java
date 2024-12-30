package com.example.planit.activity;

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

public class SubjectCreateActivity extends AppCompatActivity {

    private EditText editTextSubjectName;
    private Spinner spinnerSubjectColor;
    private Button buttonSaveSubject;
    private SubjectManager subjectManager;
    private String selectedColorValue = "#FFFFFF"; // Default white color

    private Map<String, String> colorMap;

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
        setContentView(R.layout.activity_subject_create);

        // Initialize the UI components
        editTextSubjectName = findViewById(R.id.editTextSubjectName);
        spinnerSubjectColor = findViewById(R.id.spinnerSubjectColor);
        buttonSaveSubject = findViewById(R.id.buttonSaveSubject);

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
                selectedColorValue = colorMap.get(parentView.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        // Save the subject when the button is clicked
        buttonSaveSubject.setOnClickListener(v -> saveSubject());
    }

    // Save the new subject to the SubjectManager
    private void saveSubject() {
        String name = editTextSubjectName.getText().toString();

        // Validate that the subject name is not empty
        if (name.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.dialog_error_title))
                    .setMessage(getString(R.string.dialog_fill_out_all_fields))
                    .setPositiveButton(getString(R.string.dialog_positive_button), null)
                    .show();
            return;
        }

        // Validate that the subject name has a valid length
        if (name.length() < 3 || name.length() > 50) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.dialog_error_title))
                    .setMessage(getString(R.string.dialog_subject_name_validation_error))
                    .setPositiveButton(getString(R.string.dialog_positive_button), null)
                    .show();
            return;
        }

        int color;
        try {
            color = Color.parseColor(selectedColorValue); // Parse the selected color
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, getString(R.string.dialog_subject_color_validation_error), Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            subjectManager.createSubject(name, color); // Create the subject
            Toast.makeText(this, getString(R.string.toast_subject_created), Toast.LENGTH_SHORT).show();
            finish(); // Close the activity after saving
        } catch (Exception e) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.dialog_error_title))
                    .setMessage(getString(R.string.dialog_subject_create_error) + ": " + e.getMessage())
                    .setPositiveButton(getString(R.string.dialog_positive_button), null)
                    .show();
        }
    }

    // Initialize the color map with predefined color options
    private void initializeColorMap() {
        colorMap = new HashMap<>();
        colorMap.put(getString(R.string.color_red), "#FF0000");
        colorMap.put(getString(R.string.color_green), "#00FF00");
        colorMap.put(getString(R.string.color_blue), "#0000FF");
        colorMap.put(getString(R.string.color_yellow), "#FFFF00");
        colorMap.put(getString(R.string.color_purple), "#800080");
        colorMap.put(getString(R.string.color_cyan), "#00FFFF");
        colorMap.put(getString(R.string.color_orange), "#FFA500");
        colorMap.put(getString(R.string.color_pink), "#FFC0CB");
        colorMap.put(getString(R.string.color_black), "#000000");
        colorMap.put(getString(R.string.color_white), "#FFFFFF");
    }
}
