package com.example.planit.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.planit.R;
import com.example.planit.entity.subject.Subject;
import com.example.planit.entity.subject.SubjectManager;
import com.example.planit.entity.task.TaskManager;

import java.util.ArrayList;
import java.util.List;

public class TaskCreateActivity extends AppCompatActivity {

    private EditText taskNameEditText, taskDescriptionEditText, taskDueDateEditText, taskDueTimeEditText;
    private Spinner taskImportanceSpinner, taskTypeSpinner, taskSubjectSpinner;
    private Button saveButton;

    private TaskManager taskManager;
    private SubjectManager subjectManager;
    private List<Subject> subjects;

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
        taskManager = new TaskManager(this);
        subjectManager = new SubjectManager(this);

        // Obtener los subjects y llenar el spinner
        subjects = subjectManager.getSubjects();
        List<String> subjectNames = new ArrayList<>();
        for (Subject subject : subjects) {
            subjectNames.add(subject.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjectNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taskSubjectSpinner.setAdapter(adapter);

        // Configuración del botón de guardar
        saveButton.setOnClickListener(v -> {
            saveTask();
            Toast.makeText(TaskCreateActivity.this, "Tarea creada con éxito", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    // Método para guardar la tarea en la base de datos
    private void saveTask() {
        String name = taskNameEditText.getText().toString();
        String description = taskDescriptionEditText.getText().toString();
        String dueDate = taskDueDateEditText.getText().toString();
        String dueTime = taskDueTimeEditText.getText().toString();
        int importance = Integer.parseInt(taskImportanceSpinner.getSelectedItem().toString());
        String type = taskTypeSpinner.getSelectedItem().toString();
        int subjectId = subjects.get(taskSubjectSpinner.getSelectedItemPosition()).getId();

        if (name.isEmpty() || description.isEmpty() || dueDate.isEmpty() || dueTime.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Llamar al método createTask para insertar la nueva tarea
            taskManager.createTask(name, description, importance, type, subjectId, dueDate, dueTime);
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