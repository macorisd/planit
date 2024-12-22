package com.example.planit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planit.R;
import com.example.planit.entity.SingletonTaskList;
import com.example.planit.entity.TaskAdapter;
import com.example.planit.entity.TaskManager;

public class MainActivity extends AppCompatActivity {
    public static final String SHARED_AGENDA = "SHARED_AGENDA";

    // Resto de atributos
    private TaskManager taskManager;

    private void initTaskManager() {
        taskManager = (TaskManager) SingletonTaskList.getInstance().get(MainActivity.SHARED_AGENDA);
        if (taskManager == null) {
            taskManager = new TaskManager(getApplicationContext());
            SingletonTaskList.getInstance().put(MainActivity.SHARED_AGENDA, taskManager);
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
    }
}
