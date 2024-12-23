package com.example.planit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planit.R;
import com.example.planit.entity.SingletonEntity;
import com.example.planit.entity.task.TaskAdapter;
import com.example.planit.entity.task.TaskManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    public static final String SHARED_ACTIVITY_LIST = "SHARED_ACTIVITY_LIST";

    // Resto de atributos
    private TaskManager taskManager;

    private void initTaskManager() {
        taskManager = (TaskManager) SingletonEntity.getInstance().get(MainActivity.SHARED_ACTIVITY_LIST);
        if (taskManager == null) {
            taskManager = new TaskManager(getApplicationContext());
            SingletonEntity.getInstance().put(MainActivity.SHARED_ACTIVITY_LIST, taskManager);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTaskManager();
        taskManager.initTaskManager();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        // Configurar el RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        TaskAdapter taskAdapter = new TaskAdapter(taskManager.getTasks());
        recyclerView.setAdapter(taskAdapter);

        // Configurar el botón para añadir tarea
        Button addButton = findViewById(R.id.buttonAddTask);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la actividad para añadir una nueva tarea
                Intent intent = new Intent(MainActivity.this, TaskCreateActivity.class);
                startActivity(intent);
            }
        });

        // Configurar la barra de navegación inferior
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_tasks) {
                // Navegar a la actividad de tareas
                Intent taskIntent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(taskIntent);
                return true;
            } else if (itemId == R.id.nav_notes) {
                // Navegar a la actividad de notas
                Intent noteIntent = new Intent(MainActivity.this, NoteListActivity.class);
                startActivity(noteIntent);
                return true;
            } else {
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Actualizar la lista de tareas
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        TaskAdapter taskAdapter = new TaskAdapter(taskManager.getTasks());
        recyclerView.setAdapter(taskAdapter);
    }
}
