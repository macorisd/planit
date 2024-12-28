package com.example.planit.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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

    private void initTaskManager() {
        taskManager = (TaskManager) SingletonEntity.getInstance().get(TaskFragment.SHARED_TASK_LIST);
        if (taskManager == null) {
            taskManager = new TaskManager(this);
            SingletonEntity.getInstance().put(TaskFragment.SHARED_TASK_LIST, taskManager);
        }
    }

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

        // Inicialización de vistas
        taskNameEditText = findViewById(R.id.editTextTaskName);
        taskDescriptionEditText = findViewById(R.id.editTextTaskDescription);
        taskDueDateEditText = findViewById(R.id.editTextDueDate);
        taskDueTimeEditText = findViewById(R.id.editTextDueTime);
        taskImportanceSpinner = findViewById(R.id.spinnerImportance);
        taskTypeSpinner = findViewById(R.id.spinnerType);
        taskSubjectSpinner = findViewById(R.id.spinnerSubject);
        saveButton = findViewById(R.id.buttonSaveTask);

        // Inicializar el TaskManager y SubjectManager
        initTaskManager();
        initSubjectManager();

        // Configurar el spinner de importancia
        ArrayAdapter<CharSequence> importanceAdapter = ArrayAdapter.createFromResource(this,
                R.array.importance_array, android.R.layout.simple_spinner_item);
        importanceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taskImportanceSpinner.setAdapter(importanceAdapter);

        // Obtener los subjects y llenar el spinner
        subjects = subjectManager.getSubjects();
        List<String> subjectNames = new ArrayList<>();
        subjectNames.add("(Sin asignatura)");
        for (Subject subject : subjects) {
            subjectNames.add(subject.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjectNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taskSubjectSpinner.setAdapter(adapter);

        taskDueDateEditText.setOnClickListener(v -> showDatePickerDialog());

        // Configuración del botón de guardar
        saveButton.setOnClickListener(v -> {
            saveTask();
        });
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Crear y mostrar el DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(selectedYear, selectedMonth, selectedDay);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    String formattedDate = dateFormat.format(selectedDate.getTime());

                    // Establecer la fecha en el EditText
                    taskDueDateEditText.setText(formattedDate);
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    // Método para guardar la tarea en la base de datos
    private void saveTask() {
        // Obtener los valores ingresados por el usuario
        String name = taskNameEditText.getText().toString().trim();
        String description = taskDescriptionEditText.getText().toString().trim();
        String dueDate = Task.formatDateReversed(taskDueDateEditText.getText().toString().trim());
        String dueTime = taskDueTimeEditText.getText().toString().trim();
        String importanceString = taskImportanceSpinner.getSelectedItem().toString();
        String type = taskTypeSpinner.getSelectedItem().toString();

        int taskItemPosition = taskSubjectSpinner.getSelectedItemPosition();
        int subjectId = taskItemPosition == 0 ? -1 : subjects.get(taskItemPosition - 1).getId();

        // Validar los campos obligatorios
        if (name.isEmpty() || dueDate.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar la longitud del nombre
        if (name.length() < 3 || name.length() > 40) {
            Toast.makeText(TaskCreateActivity.this, "El nombre debe tener entre 3 y 40 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar la longitud de la descripción
        if (description.length() > 400) {
            Toast.makeText(TaskCreateActivity.this, "La descripción no puede tener más de 400 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar el formato de la hora (si no está vacía)
        if (!dueTime.isEmpty() && !dueTime.matches("^([01]?\\d|2[0-3]):[0-5]\\d$")) {
            Toast.makeText(TaskCreateActivity.this, "La hora debe tener el formato HH:MM", Toast.LENGTH_SHORT).show();
            return;
        }

        int importance;
        switch (importanceString) {
            case "Baja":
                importance = 1;
                break;
            case "Media":
                importance = 2;
                break;
            case "Alta":
                importance = 3;
                break;
            default:
                importance = 1;
                break;
        }

        try {
            // Llamar al método createTask para insertar la nueva tarea
            taskManager.createTask(name, description, importance, type, subjectId, dueDate, dueTime);
            Toast.makeText(TaskCreateActivity.this, "Tarea creada con éxito", Toast.LENGTH_SHORT).show();
            finish();
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
        } catch (RuntimeException e) {
            Toast.makeText(this, "Error al guardar la tarea", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error inesperado: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}