package com.example.planit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.planit.R;
import com.example.planit.entity.task.TaskManager;

public class TaskEditActivity extends AppCompatActivity {

    private EditText etTaskName, etDescription, etImportance, etType, etSubjectId, etDueDate, etDueTime, etCompleted;
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
        String name = intent.getStringExtra("TASK_NAME");
        String description = intent.getStringExtra("TASK_DESCRIPTION");
        int importance = intent.getIntExtra("TASK_IMPORTANCE", 1);
        String type = intent.getStringExtra("TASK_TYPE");
        int subject = intent.getIntExtra("TASK_SUBJECT_ID", -1);
        String dueDate = intent.getStringExtra("TASK_DUE_DATE");
        String dueTime = intent.getStringExtra("TASK_DUE_TIME");
        int completed = intent.getIntExtra("TASK_COMPLETED", 0);

        // Vincular los EditText y los botones
        etTaskName = findViewById(R.id.etTaskName);
        etDescription = findViewById(R.id.etDescription);
        etImportance = findViewById(R.id.etImportance);
        etType = findViewById(R.id.etType);
        etSubjectId = findViewById(R.id.etSubjectId);
        etDueDate = findViewById(R.id.etDueDate);
        etDueTime = findViewById(R.id.etDueTime);
        etCompleted = findViewById(R.id.etCompleted);

        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        // Llenar los campos con los datos actuales de la tarea
        etTaskName.setText(name);
        etDescription.setText(description);
        etImportance.setText(String.valueOf(importance));
        etType.setText(type);
        etSubjectId.setText(String.valueOf(subject));
        etDueDate.setText(dueDate);
        etDueTime.setText(dueTime);
        etCompleted.setText(String.valueOf(completed));

        // Configurar el botón de guardar
        btnSave.setOnClickListener(v -> {
            // Obtener los nuevos valores de los campos
            String name1 = etTaskName.getText().toString();
            String description1 = etDescription.getText().toString();
            int importance1 = Integer.parseInt(etImportance.getText().toString());
            String type1 = etType.getText().toString();
            int subject1 = Integer.parseInt(etSubjectId.getText().toString());
            String dueDate1 = etDueDate.getText().toString();
            String dueTime1 = etDueTime.getText().toString();
            int completed1 = Integer.parseInt(etCompleted.getText().toString());

            // Llamar al método editTask para guardar los cambios
            try {
                taskManager.editTask(taskId, name1, description1, importance1, type1, subject1, dueDate1, dueTime1, completed1);
                Toast.makeText(TaskEditActivity.this, "Tarea actualizada correctamente", Toast.LENGTH_SHORT).show();

                Intent intent2 = new Intent(this, TaskDetailActivity.class);

                intent2.putExtra("TASK_ID", taskId);
                intent2.putExtra("TASK_NAME", name1);
                intent2.putExtra("TASK_DESCRIPTION", description1);
                intent2.putExtra("TASK_COMPLETED", completed1);
                intent2.putExtra("TASK_IMPORTANCE", importance1);
                intent2.putExtra("TASK_TYPE", type1);
                intent2.putExtra("TASK_SUBJECT_ID", subject1);
                intent2.putExtra("TASK_DUE_DATE", dueDate1);
                intent2.putExtra("TASK_DUE_TIME", dueTime1);

                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);

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
