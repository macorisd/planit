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
import com.example.planit.entity.SingletonEntity;
import com.example.planit.entity.subject.Subject;
import com.example.planit.entity.subject.SubjectManager;
import com.example.planit.fragment.SubjectFragment;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private SubjectManager subjectManager;

    // Initialize SubjectManager to manage the subjects in the app
    private void initSubjectManager(Context context) {
        // Retrieve the SubjectManager from SingletonEntity or initialize a new one
        subjectManager = (SubjectManager) SingletonEntity.getInstance().get(SubjectFragment.SHARED_SUBJECT_LIST);
        if (subjectManager == null) {
            subjectManager = new SubjectManager(context);
            SingletonEntity.getInstance().put(SubjectFragment.SHARED_SUBJECT_LIST, subjectManager);
        }
    }

    // Constructor for TaskAdapter, initializes task list and SubjectManager
    public TaskAdapter(Context context, List<Task> taskList) {
        this.taskList = taskList;
        initSubjectManager(context);
    }

    // Inflate the layout for each task item and create the view holder
    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout for tasks
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    // Bind data to the view for each task item in the list
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        // Set the task name and formatted due date
        holder.taskName.setText(task.getName());
        holder.taskDueDate.setText(Task.formatDate(task.getDueDate()));
        holder.taskDueTime.setText(task.getDueTime().isEmpty() ? "" : holder.itemView.getContext().getString(R.string.at_time) + " " + task.getDueTime());

        // Retrieve the subject information associated with the task
        Subject subject = subjectManager.getById(task.getSubjectId());
        if (subject != null) {
            holder.taskSubjectName.setText(subject.getName());
            holder.subjectColorStrip.setBackgroundColor(subject.getColor());
        } else {
            holder.taskSubjectName.setText(holder.itemView.getContext().getString(R.string.no_subject));
            holder.subjectColorStrip.setBackgroundColor(holder.itemView.getContext().getResources().getColor(android.R.color.white));
        }

        // Determine which priority icon to display based on the task importance
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

        // Set an onClickListener to open the task detail activity when a task item is clicked
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), TaskDetailActivity.class);
            // Pass task data to the detail activity through the Intent
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

    // Return the total number of tasks in the list
    @Override
    public int getItemCount() {
        return taskList.size();
    }

    // ViewHolder for binding task views in each item
    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskName;
        TextView taskDueDate;
        TextView taskDueTime;
        TextView taskSubjectName;
        ImageView priorityIcon;
        View subjectColorStrip;

        // Constructor to initialize the views for each task item
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.taskName);
            taskDueDate = itemView.findViewById(R.id.taskDueDate);
            taskDueTime = itemView.findViewById(R.id.taskDueTime);
            taskSubjectName = itemView.findViewById(R.id.taskSubjectName);
            priorityIcon = itemView.findViewById(R.id.priorityIcon);
            subjectColorStrip = itemView.findViewById(R.id.subjectColorStrip);
        }
    }
}
