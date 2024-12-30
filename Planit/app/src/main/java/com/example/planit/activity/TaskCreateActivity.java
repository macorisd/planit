package com.example.planit.activity;

import android.app.DatePickerDialog;
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

public class TaskCreateActivity extends AppCompatActivity {

    private EditText taskNameEditText, taskDescriptionEditText, taskDueDateEditText, taskDueTimeEditText;
    private Spinner taskImportanceSpinner, taskTypeSpinner, taskSubjectSpinner;
    private Button saveButton;
    private TaskManager taskManager;
    private SubjectManager subjectManager;
    private List<Subject> subjects;

    // Initialize the TaskManager, checking if it's already in Singleton, if not, create a new one
    private void initTaskManager() {
        taskManager = (TaskManager) SingletonEntity.getInstance().get(TaskFragment.SHARED_TASK_LIST);
        if (taskManager == null) {
            taskManager = new TaskManager(this);
            SingletonEntity.getInstance().put(TaskFragment.SHARED_TASK_LIST, taskManager);
        }
    }

    // Initialize the SubjectManager, checking if it's already in Singleton, if not, create a new one
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
        setContentView(R.layout.activity_task_create);

        // Find views for task name, description, due date, due time, importance, type, and subject
        taskNameEditText = findViewById(R.id.editTextTaskName);
        taskDescriptionEditText = findViewById(R.id.editTextTaskDescription);
        taskDueDateEditText = findViewById(R.id.editTextDueDate);
        taskDueTimeEditText = findViewById(R.id.editTextDueTime);
        taskImportanceSpinner = findViewById(R.id.spinnerImportance);
        taskTypeSpinner = findViewById(R.id.spinnerType);
        taskSubjectSpinner = findViewById(R.id.spinnerSubject);
        saveButton = findViewById(R.id.buttonSaveTask);

        // Initialize the managers
        initTaskManager();
        initSubjectManager();

        // Set up the importance spinner with values from resources
        ArrayAdapter<CharSequence> importanceAdapter = ArrayAdapter.createFromResource(this,
                R.array.importance_array, android.R.layout.simple_spinner_item);
        importanceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taskImportanceSpinner.setAdapter(importanceAdapter);

        // Initialize subject spinner
        subjects = subjectManager.getSubjects();
        List<String> subjectNames = new ArrayList<>();
        subjectNames.add(getString(R.string.no_subject));  // Add a default option
        for (Subject subject : subjects) {
            subjectNames.add(subject.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjectNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taskSubjectSpinner.setAdapter(adapter);

        // Set a listener for the due date field to show a date picker dialog
        taskDueDateEditText.setOnClickListener(v -> showDatePickerDialog());

        // Set a listener for the save button to save the task
        saveButton.setOnClickListener(v -> {
            saveTask();
        });
    }

    // Show a date picker dialog for the user to select a date
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

                    taskDueDateEditText.setText(formattedDate);
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    // Save the task after validating the inputs
    private void saveTask() {
        String name = taskNameEditText.getText().toString().trim();
        String description = taskDescriptionEditText.getText().toString().trim();
        String dueDate = Task.formatDateReversed(taskDueDateEditText.getText().toString().trim());
        String dueTime = taskDueTimeEditText.getText().toString().trim();
        String importanceString = taskImportanceSpinner.getSelectedItem().toString();
        String type = taskTypeSpinner.getSelectedItem().toString();

        int taskItemPosition = taskSubjectSpinner.getSelectedItemPosition();
        int subjectId = taskItemPosition == 0 ? -1 : subjects.get(taskItemPosition - 1).getId();

        // Check if essential fields are filled
        if (name.isEmpty() || dueDate.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.dialog_error_title))
                    .setMessage(getString(R.string.dialog_fill_out_all_fields))
                    .setPositiveButton(getString(R.string.dialog_positive_button), null)
                    .show();

            return;
        }

        // Validate the task name length
        if (name.length() < 3 || name.length() > 50) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.dialog_error_title))
                    .setMessage(getString(R.string.dialog_task_name_validation_error))
                    .setPositiveButton(getString(R.string.dialog_positive_button), null)
                    .show();
            return;
        }

        // Validate the description length
        if (description.length() > 400) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.dialog_error_title))
                    .setMessage(getString(R.string.dialog_task_description_validation_error))
                    .setPositiveButton(getString(R.string.dialog_positive_button), null)
                    .show();
            return;
        }

        // Validate the due time format
        if (!dueTime.isEmpty() && !dueTime.matches("^([01]?\\d|2[0-3]):[0-5]\\d$")) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.dialog_error_title))
                    .setMessage(getString(R.string.dialog_task_time_validation_error))
                    .setPositiveButton(getString(R.string.dialog_positive_button), null)
                    .show();
            return;
        }

        // Set importance level based on the selected option
        int importance;
        String[] importanceArray = getResources().getStringArray(R.array.importance_array);

        if (importanceString.equals(importanceArray[0])) {
            importance = 1;
        }
        else if (importanceString.equals(importanceArray[1])) {
            importance = 2;
        }
        else if (importanceString.equals(importanceArray[2])) {
            importance = 3;
        }
        else {
            importance = 1;
        }

        // Try to create the task and handle possible errors
        try {
            taskManager.createTask(name, description, importance, type, subjectId, dueDate, dueTime);
            Toast.makeText(TaskCreateActivity.this, getString(R.string.toast_task_created), Toast.LENGTH_SHORT).show();
            finish();
        } catch (RuntimeException e) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.dialog_error_title))
                    .setMessage(getString(R.string.dialog_task_create_error) + ": " + e.getMessage())
                    .setPositiveButton(getString(R.string.dialog_positive_button), null)
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.dialog_error_title))
                    .setMessage(getString(R.string.dialog_task_create_error) + ": " + e.getMessage())
                    .setPositiveButton(getString(R.string.dialog_positive_button), null)
                    .show();
        }
    }
}
