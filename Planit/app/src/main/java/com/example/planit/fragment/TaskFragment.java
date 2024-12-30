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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import java.util.Collections;
import java.util.Comparator;

public class TaskFragment extends Fragment {
    private TaskManager taskManager;

    public static final String SHARED_TASK_LIST = "SHARED_TASK_LIST";

    // Initialize TaskManager and Singleton
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

        // Get and sort tasks by date
        List<Task> allTasks = taskManager.getTasks();
        sortTasksByDate(allTasks);

        // Separate tasks into pending, overdue, and completed
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
        addSection(taskSectionsContainer, getString(R.string.pending_tasks), pendingTasks);
        addSection(taskSectionsContainer, getString(R.string.overdue_tasks), overdueTasks);
        addSection(taskSectionsContainer, getString(R.string.completed_tasks), completedTasks);

        view.findViewById(R.id.buttonAddTask).setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), TaskCreateActivity.class);
            startActivity(intent);
        });

        return view;
    }

    // Sort tasks by due date and time
    private void sortTasksByDate(List<Task> tasks) {
        Collections.sort(tasks, (task1, task2) -> {
            String dateTime1 = task1.getDueDate() + " " + task1.getDueTime();
            String dateTime2 = task2.getDueDate() + " " + task2.getDueTime();
            return dateTime1.compareTo(dateTime2);
        });
    }

    private void addSection(ViewGroup container, String title, List<Task> tasks) {
        if (tasks.isEmpty()) {
            return;
        }

        TextView sectionTitle = new TextView(requireContext());
        sectionTitle.setText(title);
        sectionTitle.setTextSize(18);
        sectionTitle.setPadding(16, 16, 16, 8);
        container.addView(sectionTitle);

        RecyclerView sectionRecyclerView = new RecyclerView(requireContext());
        sectionRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        sectionRecyclerView.setNestedScrollingEnabled(false);

        TaskAdapter taskAdapter = new TaskAdapter(requireContext(), tasks);
        sectionRecyclerView.setAdapter(taskAdapter);

        container.addView(sectionRecyclerView);

        sectionRecyclerView.post(() -> setRecyclerViewHeightBasedOnItems(sectionRecyclerView));
    }

    private void setRecyclerViewHeightBasedOnItems(RecyclerView recyclerView) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter == null) {
            return;
        }

        int totalItems = adapter.getItemCount();
        recyclerView.measure(
                View.MeasureSpec.makeMeasureSpec(recyclerView.getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.UNSPECIFIED
        );

        int itemHeight = recyclerView.getMeasuredHeight() / Math.max(1, totalItems);
        recyclerView.getLayoutParams().height = itemHeight * totalItems;
        recyclerView.requestLayout();
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
            sortTasksByDate(allTasks);

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

            addSection(taskSectionsContainer, getString(R.string.pending_tasks), pendingTasks);
            addSection(taskSectionsContainer, getString(R.string.overdue_tasks), overdueTasks);
            addSection(taskSectionsContainer, getString(R.string.completed_tasks), completedTasks);
        }
    }
}
