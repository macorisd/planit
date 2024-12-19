package com.example.planit.entity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.planit.R;
import com.example.planit.entity.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;

    public TaskAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        String taskDetails = taskList.get(position).toString();

        // Separamos el texto de la tarea en líneas para mostrarlo de forma adecuada
        String[] details = taskDetails.split("\n");

        // Mostrar nombre y descripción (si están presentes)
        holder.taskName.setText(details[1].replace("Nombre: ", ""));
        holder.taskDescription.setText(details[2].replace("Descripción: ", ""));
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    // Vista para mostrar una tarea
    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        public TextView taskName;
        public TextView taskDescription;

        public TaskViewHolder(View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.taskName);
            taskDescription = itemView.findViewById(R.id.taskDescription);
        }
    }
}
