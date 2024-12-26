package com.example.planit.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planit.R;
import com.example.planit.activity.TaskCreateActivity;
import com.example.planit.entity.SingletonEntity;
import com.example.planit.entity.task.Task;
import com.example.planit.entity.task.TaskAdapter;
import com.example.planit.entity.task.TaskManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskFragment extends Fragment {
    private TaskManager taskManager;

    public static final String SHARED_TASK_LIST = "SHARED_TASK_LIST";

    private void initTaskManager() {
        taskManager = (TaskManager) SingletonEntity.getInstance().get(SHARED_TASK_LIST);
        if (taskManager == null) {
            taskManager = new TaskManager(requireContext());
            SingletonEntity.getInstance().put(SHARED_TASK_LIST, taskManager);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        initTaskManager();
        taskManager.initTaskManager();

        List<Task> allTasks = taskManager.getTasks();
        List<Task> pendingTasks = new ArrayList<>();
        List<Task> overdueTasks = new ArrayList<>();
        List<Task> completedTasks = new ArrayList<>();

        String currentDateTime = getCurrentDateTime();

        for (Task task : allTasks) {
            if (task.getCompleted()) {
                completedTasks.add(task);
            } else {
                String taskDateTime = task.getDueDate() + " " + task.getDueTime();
                if (taskDateTime.compareTo(currentDateTime) > 0) {
                    pendingTasks.add(task);
                } else {
                    overdueTasks.add(task);
                }
            }
        }

        ViewGroup taskSectionsContainer = view.findViewById(R.id.taskSectionsContainer);
        addSection(taskSectionsContainer, "Tareas Pendientes", pendingTasks);
        addSection(taskSectionsContainer, "Tareas Atrasadas", overdueTasks);
        addSection(taskSectionsContainer, "Tareas Completadas", completedTasks);

        view.findViewById(R.id.buttonAddTask).setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), TaskCreateActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void addSection(ViewGroup container, String title, List<Task> tasks) {
        TextView sectionTitle = new TextView(requireContext());
        sectionTitle.setText(title);
        sectionTitle.setTextSize(18);
        sectionTitle.setPadding(16, 16, 16, 8);
        container.addView(sectionTitle);

        RecyclerView sectionRecyclerView = new RecyclerView(requireContext());
        sectionRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        TaskAdapter taskAdapter = new TaskAdapter(requireContext(), tasks);
        sectionRecyclerView.setAdapter(taskAdapter);
        container.addView(sectionRecyclerView);
    }

    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getView() != null) {
            ViewGroup taskSectionsContainer = getView().findViewById(R.id.taskSectionsContainer);
            taskSectionsContainer.removeAllViews();

            List<Task> allTasks = taskManager.getTasks();
            List<Task> pendingTasks = new ArrayList<>();
            List<Task> overdueTasks = new ArrayList<>();
            List<Task> completedTasks = new ArrayList<>();

            String currentDateTime = getCurrentDateTime();

            for (Task task : allTasks) {
                if (task.getCompleted()) {
                    completedTasks.add(task);
                } else {
                    String taskDateTime = task.getDueDate() + " " + task.getDueTime();
                    if (taskDateTime.compareTo(currentDateTime) > 0) {
                        pendingTasks.add(task);
                    } else {
                        overdueTasks.add(task);
                    }
                }
            }

            addSection(taskSectionsContainer, "Tareas Pendientes", pendingTasks);
            addSection(taskSectionsContainer, "Tareas Atrasadas", overdueTasks);
            addSection(taskSectionsContainer, "Tareas Completadas", completedTasks);
        }
    }
}