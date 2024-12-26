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
    private int subjectId = -1;
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
        setContentView(R.layout.activity_subject_edit);

        editTextSubjectName = findViewById(R.id.editTextSubjectName);
        spinnerSubjectColor = findViewById(R.id.spinnerSubjectColor);
        buttonSaveSubject = findViewById(R.id.buttonSaveSubject);
        buttonDeleteSubject = findViewById(R.id.buttonDeleteSubject);

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

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("SUBJECT_ID")) {
            subjectId = intent.getIntExtra("SUBJECT_ID", -1);
            String subjectName = intent.getStringExtra("SUBJECT_NAME");
            String subjectColor = intent.getStringExtra("SUBJECT_COLOR");

            editTextSubjectName.setText(subjectName);
            // Set the color in the spinner
            if (subjectColor != null && colorMap.containsValue(subjectColor)) {
                for (Map.Entry<String, String> entry : colorMap.entrySet()) {
                    if (entry.getValue().equals(subjectColor)) {
                        spinnerSubjectColor.setSelection(getKeyIndex(entry.getKey()));
                        break;
                    }
                }
            }
        }

        buttonSaveSubject.setOnClickListener(v -> saveSubject());
        buttonDeleteSubject.setOnClickListener(v -> deleteSubject());
    }

    private void saveSubject() {
        String name = editTextSubjectName.getText().toString();

        if (name.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int color;
        try {
            color = Color.parseColor(selectedColorValue);
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, "Please select a valid color", Toast.LENGTH_SHORT).show();
            return;
        }

        if (subjectId == -1) {
            subjectManager.createSubject(name, color);
            Toast.makeText(this, "Subject created successfully", Toast.LENGTH_SHORT).show();
        } else {
            subjectManager.editSubject(subjectId, name, color);
            Toast.makeText(this, "Subject updated successfully", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    private void deleteSubject() {
        if (subjectId != -1) {
            subjectManager.deleteSubject(subjectId);
            Toast.makeText(this, "Subject deleted successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error deleting subject", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeColorMap() {
        colorMap = new HashMap<>();
        colorMap.put("Red", "#FF0000");
        colorMap.put("Green", "#00FF00");
        colorMap.put("Blue", "#0000FF");
        colorMap.put("Yellow", "#FFFF00");
        colorMap.put("Purple", "#800080");
        colorMap.put("Cyan", "#00FFFF");
        colorMap.put("Orange", "#FFA500");
        colorMap.put("Pink", "#FFC0CB");
        colorMap.put("Black", "#000000");
        colorMap.put("White", "#FFFFFF");
    }

    private int getKeyIndex(String key) {
        String[] keys = colorMap.keySet().toArray(new String[0]);
        for (int i = 0; i < keys.length; i++) {
            if (keys[i].equals(key)) {
                return i;
            }
        }
        return 0;  // Default to the first item
    }
}
