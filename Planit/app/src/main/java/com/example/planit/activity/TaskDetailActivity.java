package com.example.planit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.planit.R;
import com.example.planit.entity.SingletonTaskList;
import com.example.planit.entity.TaskManager;

public class TaskDetailActivity extends AppCompatActivity {

    private TaskManager taskManager;
    private int taskId; // ID de la tarea para eliminarla

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        // Obtener datos de la tarea
        taskId = getIntent().getIntExtra("TASK_ID", -1);
        String name = getIntent().getStringExtra("TASK_NAME");
        String description = getIntent().getStringExtra("TASK_DESCRIPTION");
        boolean completed = getIntent().getBooleanExtra("TASK_COMPLETED", false);
        int importance = getIntent().getIntExtra("TASK_IMPORTANCE", 0);

        // Inicializar TaskManager
        taskManager = (TaskManager) SingletonTaskList.getInstance().get(MainActivity.SHARED_AGENDA);

        // Configurar vistas
        TextView taskName = findViewById(R.id.taskDetailName);
        TextView taskDescription = findViewById(R.id.taskDetailDescription);
        TextView taskCompleted = findViewById(R.id.taskDetailCompleted);
        TextView taskImportance = findViewById(R.id.taskDetailImportance);
        Button deleteButton = findViewById(R.id.deleteTaskButton);

        taskName.setText(name);
        taskDescription.setText(description);
        taskCompleted.setText(completed ? "Completada" : "Pendiente");
        taskImportance.setText("Importancia: " + importance);

        // Manejar el clic del botón "Eliminar"
        deleteButton.setOnClickListener(v -> {
            if (taskId != -1) {
                taskManager.deleteTask(taskId);
                Toast.makeText(this, "Tarea eliminada", Toast.LENGTH_SHORT).show();

                // Volver a MainActivity y actualizar la lista
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Error al eliminar la tarea", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
