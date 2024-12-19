package com.example.planit.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planit.R;
import com.example.planit.entity.TaskAdapter;
import com.example.planit.entity.SingletonTaskList;
import com.example.planit.entity.Task;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    // RecyclerView para mostrar las tareas
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar el RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Obtener la lista de tareas desde el SingletonTaskList
        List<Task> tasks = SingletonTaskList.getInstance().getTaskList();

        // Comprobar si la lista de tareas está vacía
        if (tasks.isEmpty()) {
            Toast.makeText(this, "No hay tareas disponibles", Toast.LENGTH_SHORT).show();
        }

        // Configurar el adaptador del RecyclerView
        taskAdapter = new TaskAdapter(tasks);
        recyclerView.setAdapter(taskAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Actualizar el adaptador si la lista de tareas cambia
        taskAdapter.notifyDataSetChanged();
    }
}
