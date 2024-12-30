package com.example.planit.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.planit.R;
import com.example.planit.entity.SingletonEntity;
import com.example.planit.entity.subject.Subject;
import com.example.planit.entity.subject.SubjectManager;
import com.example.planit.entity.task.Task;
import com.example.planit.entity.task.TaskManager;
import com.example.planit.fragment.SubjectFragment;
import com.example.planit.fragment.TaskFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TaskEditActivity extends AppCompatActivity {

    private EditText etTaskName, etDescription, etDueDate, etDueTime;
    private Spinner spinnerImportance, spinnerType, spinnerSubject;
    private Button btnSave, btnCancel;
    private TaskManager taskManager;
    private SubjectManager subjectManager;
    private List<Subject> subjects;
    private int taskId;
    private boolean completed;

    // Initialize the TaskManager object
    private void initTaskManager() {
        taskManager = (TaskManager) SingletonEntity.getInstance().get(TaskFragment.SHARED_TASK_LIST);
        if (taskManager == null) {
            taskManager = new TaskManager(this);
            SingletonEntity.getInstance().put(TaskFragment.SHARED_TASK_LIST, taskManager);
        }
    }

    // Initialize the SubjectManager object
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
        setContentView(R.layout.activity_task_edit);

        // Initialize managers
        initTaskManager();
        initSubjectManager();

        // Get data from the intent
        Intent intent = getIntent();
        taskId = intent.getIntExtra("TASK_ID", -1);
        String name = intent.getStringExtra("TASK_NAME");
        String description = intent.getStringExtra("TASK_DESCRIPTION");
        int importance = intent.getIntExtra("TASK_IMPORTANCE", 1);
        String type = intent.getStringExtra("TASK_TYPE");
        int subjectId = intent.getIntExtra("TASK_SUBJECT_ID", -1);
        String dueDate = intent.getStringExtra("TASK_DUE_DATE");
        String dueTime = intent.getStringExtra("TASK_DUE_TIME");
        this.completed = intent.getBooleanExtra("TASK_COMPLETED", false);

        // Initialize UI elements
        etTaskName = findViewById(R.id.etTaskName);
        etDescription = findViewById(R.id.etDescription);
        etDueDate = findViewById(R.id.etDueDate);
        etDueTime = findViewById(R.id.etDueTime);
        spinnerImportance = findViewById(R.id.spinnerImportance);
        spinnerType = findViewById(R.id.spinnerType);
        spinnerSubject = findViewById(R.id.spinnerSubject);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        // Set the task data to the UI fields
        etTaskName.setText(name);
        etDescription.setText(description);
        etDueDate.setText(Task.formatDate(dueDate));
        etDueTime.setText(dueTime);

        // Set up the importance spinner
        ArrayAdapter<CharSequence> importanceAdapter = ArrayAdapter.createFromResource(this,
                R.array.importance_array, android.R.layout.simple_spinner_item);
        importanceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerImportance.setAdapter(importanceAdapter);
        spinnerImportance.setSelection(importance - 1);

        // Set up the type spinner
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,
                R.array.type_array, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);
        spinnerType.setSelection(typeAdapter.getPosition(type));

        // Get and display subjects in a spinner
        subjects = subjectManager.getSubjects();
        List<String> subjectNames = new ArrayList<>();
        subjectNames.add(getString(R.string.no_subject));
        for (Subject subject : subjects) {
            subjectNames.add(subject.getName());
        }
        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjectNames);
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubject.setAdapter(subjectAdapter);

        // Set the selected subject in the spinner
        if (subjectId == -1) {
            spinnerSubject.setSelection(0);
        } else {
            for (int i = 0; i < subjects.size(); i++) {
                if (subjects.get(i).getId() == subjectId) {
                    spinnerSubject.setSelection(i + 1);
                    break;
                }
            }
        }

        // Set listener for the due date field to show the date picker dialog
        etDueDate.setOnClickListener(v -> showDatePickerDialog());

        // Set listener to save the task
        btnSave.setOnClickListener(v -> {
            updateTask();
        });

        // Set listener to cancel editing and return to previous screen
        btnCancel.setOnClickListener(v -> finish());
    }

    // Display the date picker dialog
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(selectedYear, selectedMonth, selectedDay);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    String formattedDate = dateFormat.format(selectedDate.getTime());

                    // Set the selected date in the due date field
                    etDueDate.setText(formattedDate);
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    // Method to update the task with the data entered by the user
    private void updateTask() {
        String newName = etTaskName.getText().toString().trim();
        String newDescription = etDescription.getText().toString().trim();
        String newDueDate = Task.formatDateReversed(etDueDate.getText().toString());
        String newDueTime = etDueTime.getText().toString().trim();
        String newImportanceString = spinnerImportance.getSelectedItem().toString();
        String newType = spinnerType.getSelectedItem().toString();

        // Check if all required fields are filled
        if (newName.isEmpty() || newDueDate.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.dialog_error_title))
                    .setMessage(getString(R.string.dialog_fill_out_all_fields))
                    .setPositiveButton(getString(R.string.dialog_positive_button), null)
                    .show();
            return;
        }

        // Validate task name length
        if (newName.length() < 3 || newName.length() > 50) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.dialog_error_title))
                    .setMessage(getString(R.string.dialog_task_name_validation_error))
                    .setPositiveButton(getString(R.string.dialog_positive_button), null)
                    .show();
            return;
        }

        // Validate description length
        if (newDescription.length() > 400) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.dialog_error_title))
                    .setMessage(getString(R.string.dialog_task_description_validation_error))
                    .setPositiveButton(getString(R.string.dialog_positive_button), null)
                    .show();
            return;
        }

        // Validate due time format
        if (!newDueTime.isEmpty() && !newDueTime.matches("^([01]?\\d|2[0-3]):[0-5]\\d$")) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.dialog_error_title))
                    .setMessage(getString(R.string.dialog_task_time_validation_error))
                    .setPositiveButton(getString(R.string.dialog_positive_button), null)
                    .show();
            return;
        }

        // Determine the selected subject ID
        int newSubjectId = spinnerSubject.getSelectedItemPosition() == 0
                ? -1
                : subjects.get(spinnerSubject.getSelectedItemPosition() - 1).getId();

        // Determine the selected importance value
        int newImportance;
        String[] importanceArray = getResources().getStringArray(R.array.importance_array);
        if (newImportanceString.equals(importanceArray[0])) {
            newImportance = 1;
        }
        else if (newImportanceString.equals(importanceArray[1])) {
            newImportance = 2;
        }
        else if (newImportanceString.equals(importanceArray[2])) {
            newImportance = 3;
        }
        else {
            newImportance = 1;
        }

        try {
            // Update the task using the TaskManager
            taskManager.editTask(taskId, newName, newDescription, newImportance, newType, newSubjectId, newDueDate, newDueTime, 0);
            Toast.makeText(this, getString(R.string.toast_task_updated), Toast.LENGTH_SHORT).show();

            // Navigate to the task details screen
            Intent intent = new Intent(this, TaskDetailActivity.class);
            intent.putExtra("TASK_ID", taskId);
            intent.putExtra("TASK_NAME", newName);
            intent.putExtra("TASK_DESCRIPTION", newDescription);
            intent.putExtra("TASK_COMPLETED", this.completed);
            intent.putExtra("TASK_IMPORTANCE", newImportance);
            intent.putExtra("TASK_TYPE", newType);
            intent.putExtra("TASK_SUBJECT_ID", newSubjectId);
            intent.putExtra("TASK_DUE_DATE", newDueDate);
            intent.putExtra("TASK_DUE_TIME", newDueTime);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            finish();
        } catch (Exception e) {
            // Show error message if something goes wrong
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.dialog_error_title))
                    .setMessage(getString(R.string.dialog_task_update_error) + ": " + e.getMessage())
                    .setPositiveButton(getString(R.string.dialog_positive_button), null)
                    .show();
        }
    }

}
