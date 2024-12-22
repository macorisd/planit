package com.example.planit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.planit.R;
import com.example.planit.entity.TaskManager;

public class TaskEditActivity extends AppCompatActivity {

    private EditText etTaskName, etDescription, etImportance, etType, etSubject, etDueDate, etDueTime, etCompleted;
    private Button btnSave, btnCancel;
    private TaskManager taskManager;
    private int taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);

        // Inicializar TaskManager
        taskManager = new TaskManager(this);

        // Obtener los datos de la tarea desde el Intent
        Intent intent = getIntent();
        taskId = intent.getIntExtra("TASK_ID", -1);
        String taskName = intent.getStringExtra("TASK_NAME");
        String description = intent.getStringExtra("TASK_DESCRIPTION");
        int importance = intent.getIntExtra("TASK_IMPORTANCE", 1);
        String type = intent.getStringExtra("TASK_TYPE");
        String subject = intent.getStringExtra("TASK_SUBJECT");
        String dueDate = intent.getStringExtra("TASK_DUE_DATE");
        String dueTime = intent.getStringExtra("TASK_DUE_TIME");
        int completed = intent.getIntExtra("TASK_COMPLETED", 0);

        // Vincular los EditText y los botones
        etTaskName = findViewById(R.id.etTaskName);
        etDescription = findViewById(R.id.etDescription);
        etImportance = findViewById(R.id.etImportance);
        etType = findViewById(R.id.etType);
        etSubject = findViewById(R.id.etSubjectId);
        etDueDate = findViewById(R.id.etDueDate);
        etDueTime = findViewById(R.id.etDueTime);
        etCompleted = findViewById(R.id.etCompleted);

        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        // Llenar los campos con los datos actuales de la tarea
        etTaskName.setText(taskName);
        etDescription.setText(description);
        etImportance.setText(String.valueOf(importance));
        etType.setText(type);
        etSubject.setText(subject);
        etDueDate.setText(dueDate);
        etDueTime.setText(dueTime);
        etCompleted.setText(String.valueOf(completed));

        // Configurar el botón de guardar
        btnSave.setOnClickListener(v -> {
            // Obtener los nuevos valores de los campos
            String name = etTaskName.getText().toString();
            String description1 = etDescription.getText().toString();
            int importance1 = Integer.parseInt(etImportance.getText().toString());
            String type1 = etType.getText().toString();
            int subject1 = Integer.parseInt(etSubject.getText().toString());
            String dueDate1 = etDueDate.getText().toString();
            String dueTime1 = etDueTime.getText().toString();
            int completed1 = Integer.parseInt(etCompleted.getText().toString());

            // Llamar al método editTask para guardar los cambios
            try {
                taskManager.editTask(taskId, name, description1, importance1, type1, subject1, dueDate1, dueTime1, completed1);
                Toast.makeText(TaskEditActivity.this, "Tarea actualizada correctamente", Toast.LENGTH_SHORT).show();
                finish(); // Cerrar la actividad después de guardar
            } catch (Exception e) {
                Toast.makeText(TaskEditActivity.this, "Error al actualizar la tarea", Toast.LENGTH_SHORT).show();
            }
        });

        // Configurar el botón de cancelar
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Cerrar la actividad sin guardar cambios
            }
        });
    }
}
