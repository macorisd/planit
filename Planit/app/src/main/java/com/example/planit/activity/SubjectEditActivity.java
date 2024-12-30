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
    private int subjectId = -1;
    private int selectedColorValue = 0;  // Default color

    private Map<String, Integer> colorMap;

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
                selectedColorValue = colorMap.get(parentView.getItemAtPosition(position));
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
            int subjectColor = intent.getIntExtra("SUBJECT_COLOR", 0);

            editTextSubjectName.setText(subjectName);
            // Set the color in the spinner
            if (subjectColor != 0 && colorMap.containsValue(subjectColor)) {
                for (Map.Entry<String, Integer> entry : colorMap.entrySet()) {
                    if (entry.getValue().equals(subjectColor)) {
                        spinnerSubjectColor.setSelection(getKeyIndex(entry.getKey()));
                        break;
                    }
                }
            }
        }

        buttonSaveSubject.setOnClickListener(v -> updateSubject());
        buttonDeleteSubject.setOnClickListener(v -> deleteSubject());
    }

    private void updateSubject() {
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

        int color = selectedColorValue;

        if (subjectId != -1) {
            subjectManager.editSubject(subjectId, name, color);
            Toast.makeText(this, getString(R.string.toast_subject_updated), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.dialog_error_title))
                    .setMessage(getString(R.string.dialog_subject_update_error))
                    .setPositiveButton(getString(R.string.dialog_positive_button), null)
                    .show();
        }
    }

    private void deleteSubject() {
        if (subjectId != -1) {
            subjectManager.deleteSubject(subjectId);
            Toast.makeText(this, getString(R.string.toast_subject_deleted), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.dialog_error_title))
                    .setMessage(getString(R.string.dialog_subject_delete_error))
                    .setPositiveButton(getString(R.string.dialog_positive_button), null)
                    .show();
        }
    }

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
