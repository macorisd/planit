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
    private String selectedColorValue = "#FFFFFF";  // Default color

    private Map<String, String> colorMap;

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

        editTextSubjectName = findViewById(R.id.editTextSubjectName);
        spinnerSubjectColor = findViewById(R.id.spinnerSubjectColor);
        buttonSaveSubject = findViewById(R.id.buttonSaveSubject);

        initSubjectManager();

        // Initialize color map
        initializeColorMap();

        // Set up Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, colorMap.keySet().toArray(new String[0]));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubjectColor.setAdapter(adapter);

        // Set up spinner item selection listener
        spinnerSubjectColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, android.view.View selectedItemView, int position, long id) {
                selectedColorValue = colorMap.get(parentView.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // No action needed
            }
        });

        buttonSaveSubject.setOnClickListener(v -> saveSubject());
    }

    private void saveSubject() {
        String name = editTextSubjectName.getText().toString();

        if (name.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.dialog_error_title))
                    .setMessage(getString(R.string.dialog_fill_out_all_fields))
                    .setPositiveButton(getString(R.string.dialog_positive_button), null)
                    .show();

            return;
        }

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
            color = Color.parseColor(selectedColorValue);
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, getString(R.string.dialog_subject_color_validation_error), Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            subjectManager.createSubject(name, color);
            Toast.makeText(this, getString(R.string.toast_subject_created), Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.dialog_error_title))
                    .setMessage(getString(R.string.dialog_subject_create_error) + ": " + e.getMessage())
                    .setPositiveButton(getString(R.string.dialog_positive_button), null)
                    .show();
        }
    }

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