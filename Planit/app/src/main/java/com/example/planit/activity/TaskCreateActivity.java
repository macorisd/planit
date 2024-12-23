package com.example.planit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.planit.R;
import com.example.planit.entity.task.TaskManager;

public class TaskCreateActivity extends AppCompatActivity {

    private EditText taskNameEditText, taskDescriptionEditText, taskDueDateEditText, taskDueTimeEditText;
    private Spinner taskImportanceSpinner, taskTypeSpinner, taskSubjectSpinner;
    private Button saveButton;

    private TaskManager taskManager;

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

        // Inicializar el TaskManager
        taskManager = new TaskManager(this);

        // Configuración del botón de guardar
        saveButton.setOnClickListener(v -> {
            saveTask();
            Toast.makeText(TaskCreateActivity.this, "Tarea creada con éxito", Toast.LENGTH_SHORT).show();

            // Volver a MainActivity y actualizar la lista
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
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
        int subjectId = Integer.parseInt(taskSubjectSpinner.getSelectedItem().toString());  // Asumiendo que los IDs de los subjects son números.

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
