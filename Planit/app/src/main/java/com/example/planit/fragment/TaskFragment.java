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

public class TaskFragment extends Fragment {
    private TaskManager taskManager;

    public static final String SHARED_TASK_LIST = "SHARED_TASK_LIST";

    // Initialize TaskManager and Singleton
    private void initTaskManager() {
        // Try to get TaskManager from Singleton
        taskManager = (TaskManager) SingletonEntity.getInstance().get(SHARED_TASK_LIST);
        if (taskManager == null) {
            // If not present, create a new instance and store it in Singleton
            taskManager = new TaskManager(requireContext());
            SingletonEntity.getInstance().put(SHARED_TASK_LIST, taskManager);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for the fragment
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        // Initialize TaskManager and set up data
        initTaskManager();
        taskManager.initTaskManager();

        // Separate tasks into pending, overdue, and completed
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

        // Add sections for each task category
        ViewGroup taskSectionsContainer = view.findViewById(R.id.taskSectionsContainer);
        addSection(taskSectionsContainer, getString(R.string.pending_tasks), pendingTasks);
        addSection(taskSectionsContainer, getString(R.string.overdue_tasks), overdueTasks);
        addSection(taskSectionsContainer, getString(R.string.completed_tasks), completedTasks);

        // Button to add a new task
        view.findViewById(R.id.buttonAddTask).setOnClickListener(v -> {
            // Launch TaskCreateActivity to add a new task
            Intent intent = new Intent(requireContext(), TaskCreateActivity.class);
            startActivity(intent);
        });

        return view;
    }

    // Adds a section (with a title and list of tasks) to the container
    private void addSection(ViewGroup container, String title, List<Task> tasks) {
        if (tasks.isEmpty()) {
            return; // No section added if there are no tasks
        }

        // Create and add a section title
        TextView sectionTitle = new TextView(requireContext());
        sectionTitle.setText(title);
        sectionTitle.setTextSize(18);
        sectionTitle.setPadding(16, 16, 16, 8);
        container.addView(sectionTitle);

        // Set up RecyclerView for the task list
        RecyclerView sectionRecyclerView = new RecyclerView(requireContext());
        sectionRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        sectionRecyclerView.setNestedScrollingEnabled(false);

        // Set the adapter for RecyclerView with the given tasks
        TaskAdapter taskAdapter = new TaskAdapter(requireContext(), tasks);
        sectionRecyclerView.setAdapter(taskAdapter);

        container.addView(sectionRecyclerView);

        // Adjust RecyclerView height based on the number of items
        sectionRecyclerView.post(() -> setRecyclerViewHeightBasedOnItems(sectionRecyclerView));
    }

    // Adjusts RecyclerView height to fit the number of items
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

    // Get the current date and time as a string
    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getView() != null) {
            // Remove all existing sections when fragment is resumed
            ViewGroup taskSectionsContainer = getView().findViewById(R.id.taskSectionsContainer);
            taskSectionsContainer.removeAllViews();

            // Separate tasks into pending, overdue, and completed again
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

            // Add updated sections for each task category
            addSection(taskSectionsContainer, getString(R.string.pending_tasks), pendingTasks);
            addSection(taskSectionsContainer, getString(R.string.overdue_tasks), overdueTasks);
            addSection(taskSectionsContainer, getString(R.string.completed_tasks), completedTasks);
        }
    }
}
