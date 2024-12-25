package com.example.planit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.planit.R;
import com.example.planit.entity.SingletonEntity;
import com.example.planit.entity.task.TaskManager;
import com.example.planit.fragment.TaskFragment;

public class TaskDetailActivity extends AppCompatActivity {

    private TaskManager taskManager;
    private int taskId; // ID de la tarea para eliminarla
    private String taskName;
    private String taskDescription;
    private boolean taskCompleted;
    private int taskImportance;
    private String taskType;
    private int taskSubjectId;
    private String taskDueDate;
    private String taskDueTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        // Obtener datos de la tarea
        taskId = getIntent().getIntExtra("TASK_ID", -1);
        taskName = getIntent().getStringExtra("TASK_NAME");
        taskDescription = getIntent().getStringExtra("TASK_DESCRIPTION");
        taskCompleted = getIntent().getBooleanExtra("TASK_COMPLETED", false);
        taskImportance = getIntent().getIntExtra("TASK_IMPORTANCE", 0);
        taskType = getIntent().getStringExtra("TASK_TYPE");
        taskSubjectId = getIntent().getIntExtra("TASK_SUBJECT_ID", -1);
        taskDueDate = getIntent().getStringExtra("TASK_DUE_DATE");
        taskDueTime = getIntent().getStringExtra("TASK_DUE_TIME");

        // Inicializar TaskManager
        taskManager = (TaskManager) SingletonEntity.getInstance().get(TaskFragment.SHARED_TASK_LIST);

        // Configurar vistas
        TextView taskNameView = findViewById(R.id.taskDetailName);
        TextView taskDescriptionView = findViewById(R.id.taskDetailDescription);
        TextView taskCompletedView = findViewById(R.id.taskDetailCompleted);
        TextView taskImportanceView = findViewById(R.id.taskDetailImportance);
        TextView taskTypeView = findViewById(R.id.taskDetailType);
        TextView taskSubjectView = findViewById(R.id.taskDetailSubject);
        TextView taskDueDateView = findViewById(R.id.taskDetailDueDate);
        TextView taskDueTimeView = findViewById(R.id.taskDetailDueTime);
        Button deleteButton = findViewById(R.id.deleteTaskButton);
        Button editButton = findViewById(R.id.editTaskButton);

        taskNameView.setText(taskName);
        taskDescriptionView.setText(taskDescription);
        taskCompletedView.setText(taskCompleted ? "Completada" : "Pendiente");
        taskImportanceView.setText("Importancia: " + taskImportance);
        taskTypeView.setText("Tipo: " + taskType);
        taskSubjectView.setText("Asignatura: " + taskSubjectId);
        taskDueDateView.setText("Fecha de vencimiento: " + taskDueDate);
        taskDueTimeView.setText("Hora de vencimiento: " + taskDueTime);

        // Manejar el clic del botón "Eliminar"
        deleteButton.setOnClickListener(v -> {
            if (taskId != -1) {
                taskManager.deleteTask(taskId);
                Toast.makeText(this, "Tarea eliminada", Toast.LENGTH_SHORT).show();

                // Volver a MainActivity y actualizar la lista
//                Intent intent = new Intent(this, TaskListActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Error al eliminar la tarea", Toast.LENGTH_SHORT).show();
            }
        });

        // Manejar el clic del botón "Editar"
        editButton.setOnClickListener(v -> {
            if (taskId != -1) {
                // Pasar los detalles de la tarea a la actividad de edición
                Intent intent = new Intent(this, TaskEditActivity.class);
                intent.putExtra("TASK_ID", taskId);
                intent.putExtra("TASK_NAME", taskName);
                intent.putExtra("TASK_DESCRIPTION", taskDescription);
                intent.putExtra("TASK_COMPLETED", taskCompleted);
                intent.putExtra("TASK_IMPORTANCE", taskImportance);
                intent.putExtra("TASK_TYPE", taskType);
                intent.putExtra("TASK_SUBJECT_ID", taskSubjectId);
                intent.putExtra("TASK_DUE_DATE", taskDueDate);
                intent.putExtra("TASK_DUE_TIME", taskDueTime);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Error al editar la tarea", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
