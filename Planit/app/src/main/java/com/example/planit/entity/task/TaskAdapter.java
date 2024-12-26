package com.example.planit.entity.task;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planit.R;
import com.example.planit.activity.TaskDetailActivity;
import com.example.planit.entity.subject.Subject;
import com.example.planit.entity.subject.SubjectManager;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private SubjectManager subjectManager;

    public TaskAdapter(Context context, List<Task> taskList) {
        this.taskList = taskList;
        this.subjectManager = new SubjectManager(context);
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.taskName.setText(task.getName());
        holder.taskDueDate.setText(task.getDueDate());
        holder.taskDueTime.setText("a las " + task.getDueTime());

        // Obtener el nombre de la asignatura
        Subject subject = subjectManager.getById(task.getSubjectId());
        if (subject != null) {
            holder.taskSubjectName.setText(subject.getName());
        } else {
            holder.taskSubjectName.setText("(Sin asignatura)");
        }

        // Establecer el icono de prioridad según la importancia
        int priorityIcon;
        switch (task.getImportance()) {
            case 1:
                priorityIcon = R.drawable.stat_1_24px;
                break;
            case 2:
                priorityIcon = R.drawable.stat_2_24px;
                break;
            case 3:
                priorityIcon = R.drawable.stat_3_24px;
                break;
            default:
                priorityIcon = R.drawable.stat_1_24px;
                break;
        }
        holder.priorityIcon.setImageResource(priorityIcon);

        // Manejar clics en los elementos de la lista
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), TaskDetailActivity.class);
            intent.putExtra("TASK_ID", task.getId());
            intent.putExtra("TASK_NAME", task.getName());
            intent.putExtra("TASK_DESCRIPTION", task.getDescription());
            intent.putExtra("TASK_COMPLETED", task.getCompleted());
            intent.putExtra("TASK_IMPORTANCE", task.getImportance());
            intent.putExtra("TASK_TYPE", task.getType());
            intent.putExtra("TASK_SUBJECT_ID", task.getSubjectId());
            intent.putExtra("TASK_DUE_DATE", task.getDueDate());
            intent.putExtra("TASK_DUE_TIME", task.getDueTime());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
    TextView taskName;
    TextView taskDueDate;
    TextView taskDueTime;
    TextView taskSubjectName;
    ImageView priorityIcon;

    public TaskViewHolder(@NonNull View itemView) {
        super(itemView);
        taskName = itemView.findViewById(R.id.taskName);
        taskDueDate = itemView.findViewById(R.id.taskDueDate);
        taskDueTime = itemView.findViewById(R.id.taskDueTime);
        taskSubjectName = itemView.findViewById(R.id.taskSubjectName);
        priorityIcon = itemView.findViewById(R.id.priorityIcon);
    }
}
}