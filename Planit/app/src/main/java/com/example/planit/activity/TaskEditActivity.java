package com.example.planit.activity;

import android.content.Intent;
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
import com.example.planit.entity.task.TaskManager;
import com.example.planit.fragment.SubjectFragment;
import com.example.planit.fragment.TaskFragment;

import java.util.ArrayList;
import java.util.List;

public class TaskEditActivity extends AppCompatActivity {

    private EditText etTaskName, etDescription, etImportance, etType, etDueDate, etDueTime, etCompleted;
    private Spinner spinnerSubject;
    private Button btnSave, btnCancel;
    private TaskManager taskManager;
    private SubjectManager subjectManager;
    private List<Subject> subjects;
    private int taskId;

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
        setContentView(R.layout.activity_task_edit);

        // Inicializar TaskManager y SubjectManager
        initTaskManager();
        initSubjectManager();

        // Obtener los datos de la tarea desde el Intent
        Intent intent = getIntent();
        taskId = intent.getIntExtra("TASK_ID", -1);
        String name = intent.getStringExtra("TASK_NAME");
        String description = intent.getStringExtra("TASK_DESCRIPTION");
        int importance = intent.getIntExtra("TASK_IMPORTANCE", 1);
        String type = intent.getStringExtra("TASK_TYPE");
        int subjectId = intent.getIntExtra("TASK_SUBJECT_ID", -1);
        String dueDate = intent.getStringExtra("TASK_DUE_DATE");
        String dueTime = intent.getStringExtra("TASK_DUE_TIME");
        int completed = intent.getIntExtra("TASK_COMPLETED", 0);

        // Vincular los EditText, Spinner y los botones
        etTaskName = findViewById(R.id.etTaskName);
        etDescription = findViewById(R.id.etDescription);
        etImportance = findViewById(R.id.etImportance);
        etType = findViewById(R.id.etType);
        spinnerSubject = findViewById(R.id.spinnerSubject);
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
        etDueDate.setText(dueDate);
        etDueTime.setText(dueTime);
        etCompleted.setText(String.valueOf(completed));

        // Obtener los subjects y llenar el spinner
        subjects = subjectManager.getSubjects();
        List<String> subjectNames = new ArrayList<>();
        for (Subject subject : subjects) {
            subjectNames.add(subject.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjectNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubject.setAdapter(adapter);

        // Seleccionar el subject actual en el spinner
        for (int i = 0; i < subjects.size(); i++) {
            if (subjects.get(i).getId() == subjectId) {
                spinnerSubject.setSelection(i);
                break;
            }
        }

        // Configurar el botón de guardar
        btnSave.setOnClickListener(v -> {
            // Obtener los nuevos valores de los campos
            String name1 = etTaskName.getText().toString();
            String description1 = etDescription.getText().toString();
            int importance1 = Integer.parseInt(etImportance.getText().toString());
            String type1 = etType.getText().toString();
            int subjectId1 = subjects.get(spinnerSubject.getSelectedItemPosition()).getId();
            String dueDate1 = etDueDate.getText().toString();
            String dueTime1 = etDueTime.getText().toString();
            int completed1 = Integer.parseInt(etCompleted.getText().toString());

            // Llamar al método editTask para guardar los cambios
            try {
                taskManager.editTask(taskId, name1, description1, importance1, type1, subjectId1, dueDate1, dueTime1, completed1);
                Toast.makeText(TaskEditActivity.this, "Tarea actualizada correctamente", Toast.LENGTH_SHORT).show();

                Intent intent2 = new Intent(this, TaskDetailActivity.class);
                intent2.putExtra("TASK_ID", taskId);
                intent2.putExtra("TASK_NAME", name1);
                intent2.putExtra("TASK_DESCRIPTION", description1);
                intent2.putExtra("TASK_COMPLETED", completed1);
                intent2.putExtra("TASK_IMPORTANCE", importance1);
                intent2.putExtra("TASK_TYPE", type1);
                intent2.putExtra("TASK_SUBJECT_ID", subjectId1);
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
        btnCancel.setOnClickListener(v -> finish()); // Cerrar la actividad sin guardar cambios
    }
}