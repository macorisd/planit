package com.example.planit.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planit.R;
import com.example.planit.activity.TaskCreateActivity;
import com.example.planit.entity.SingletonEntity;
import com.example.planit.entity.task.TaskAdapter;
import com.example.planit.entity.task.TaskManager;

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

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        TaskAdapter taskAdapter = new TaskAdapter(taskManager.getTasks());
        recyclerView.setAdapter(taskAdapter);

        view.findViewById(R.id.buttonAddTask).setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), TaskCreateActivity.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getView() != null) {
            RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
            TaskAdapter taskAdapter = new TaskAdapter(taskManager.getTasks());
            recyclerView.setAdapter(taskAdapter);
        }
    }
}
