package com.example.planit.activity;

import android.app.DatePickerDialog;
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
        this.completed = intent.getBooleanExtra("TASK_COMPLETED", false);

        // Vincular los EditText, Spinner y botones
        etTaskName = findViewById(R.id.etTaskName);
        etDescription = findViewById(R.id.etDescription);
        etDueDate = findViewById(R.id.etDueDate);
        etDueTime = findViewById(R.id.etDueTime);
        spinnerImportance = findViewById(R.id.spinnerImportance);
        spinnerType = findViewById(R.id.spinnerType);
        spinnerSubject = findViewById(R.id.spinnerSubject);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        // Llenar los campos con los datos actuales de la tarea
        etTaskName.setText(name);
        etDescription.setText(description);
        etDueDate.setText(Task.formatDate(dueDate));
        etDueTime.setText(dueTime);

        // Configurar el spinner de importancia
        ArrayAdapter<CharSequence> importanceAdapter = ArrayAdapter.createFromResource(this,
                R.array.importance_array, android.R.layout.simple_spinner_item);
        importanceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerImportance.setAdapter(importanceAdapter);
        spinnerImportance.setSelection(importance - 1);

        // Configurar el spinner de tipo
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,
                R.array.type_array, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);
        spinnerType.setSelection(typeAdapter.getPosition(type));

        // Obtener los subjects y llenar el spinner
        subjects = subjectManager.getSubjects();
        List<String> subjectNames = new ArrayList<>();
        subjectNames.add("(Sin asignatura)");
        for (Subject subject : subjects) {
            subjectNames.add(subject.getName());
        }
        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjectNames);
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubject.setAdapter(subjectAdapter);

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

        // Configurar el campo de fecha para mostrar el DatePickerDialog
        etDueDate.setOnClickListener(v -> showDatePickerDialog());

        // Configurar el botón de guardar con validaciones
        btnSave.setOnClickListener(v -> {
            updateTask();
        });

        // Configurar el botón de cancelar
        btnCancel.setOnClickListener(v -> finish());
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
                    etDueDate.setText(formattedDate);
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    // Método para actualizar una tarea existente
    private void updateTask() {
        // Obtener los nuevos valores de los campos
        String newName = etTaskName.getText().toString().trim();
        String newDescription = etDescription.getText().toString().trim();
        String newDueDate = Task.formatDateReversed(etDueDate.getText().toString());
        String newDueTime = etDueTime.getText().toString().trim();
        String newImportanceString = spinnerImportance.getSelectedItem().toString();
        String newType = spinnerType.getSelectedItem().toString();

        // Validar los campos obligatorios
        if (newName.isEmpty() || newDueDate.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar la longitud del nombre
        if (newName.length() < 3 || newName.length() > 40) {
            Toast.makeText(TaskEditActivity.this, "El nombre debe tener entre 3 y 40 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar la longitud de la descripción
        if (newDescription.length() > 400) {
            Toast.makeText(TaskEditActivity.this, "La descripción no puede tener más de 400 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar el formato de la hora (si no está vacía)
        if (!newDueTime.isEmpty() && !newDueTime.matches("^([01]?\\d|2[0-3]):[0-5]\\d$")) {
            Toast.makeText(TaskEditActivity.this, "La hora debe tener el formato HH:MM", Toast.LENGTH_SHORT).show();
            return;
        }

        int newSubjectId = spinnerSubject.getSelectedItemPosition() == 0
                ? -1
                : subjects.get(spinnerSubject.getSelectedItemPosition() - 1).getId();

        int newImportance;
        switch (newImportanceString) {
            case "Baja":
                newImportance = 1;
                break;
            case "Media":
                newImportance = 2;
                break;
            case "Alta":
                newImportance = 3;
                break;
            default:
                newImportance = 1;
                break;
        }

        // Llamar al método editTask del TaskManager
        try {
            taskManager.editTask(taskId, newName, newDescription, newImportance, newType, newSubjectId, newDueDate, newDueTime, 0);
            Toast.makeText(this, "Tarea actualizada correctamente", Toast.LENGTH_SHORT).show();

            // Navegar de regreso a TaskDetailActivity con los nuevos datos
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
            Toast.makeText(this, "Error al actualizar la tarea: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}