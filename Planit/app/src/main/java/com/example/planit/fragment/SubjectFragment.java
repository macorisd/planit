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
import com.example.planit.activity.SubjectCreateActivity;
import com.example.planit.entity.SingletonEntity;
import com.example.planit.entity.subject.SubjectAdapter;
import com.example.planit.entity.subject.SubjectManager;

public class SubjectFragment extends Fragment {
    private SubjectManager subjectManager;

    public static final String SHARED_SUBJECT_LIST = "SHARED_SUBJECT_LIST";

    // Initialize SubjectManager and Singleton
    private void initSubjectManager() {
        // Try to get SubjectManager from Singleton
        subjectManager = (SubjectManager) SingletonEntity.getInstance().get(SHARED_SUBJECT_LIST);
        if (subjectManager == null) {
            // If not present, create a new instance and store it in Singleton
            subjectManager = new SubjectManager(requireContext());
            SingletonEntity.getInstance().put(SHARED_SUBJECT_LIST, subjectManager);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for the fragment
        View view = inflater.inflate(R.layout.fragment_subject_list, container, false);

        // Initialize SubjectManager and set up data
        initSubjectManager();

        // Set up RecyclerView to display subjects
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewSubjects);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        SubjectAdapter subjectAdapter = new SubjectAdapter(subjectManager.getSubjects());
        recyclerView.setAdapter(subjectAdapter);

        // Set up the button to create a new subject
        view.findViewById(R.id.buttonAddSubject).setOnClickListener(v -> {
            // Launch SubjectCreateActivity to add a new subject
            Intent intent = new Intent(requireContext(), SubjectCreateActivity.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Update RecyclerView when fragment is resumed
        if (getView() != null) {
            RecyclerView recyclerView = getView().findViewById(R.id.recyclerViewSubjects);
            SubjectAdapter subjectAdapter = new SubjectAdapter(subjectManager.getSubjects());
            recyclerView.setAdapter(subjectAdapter);
        }
    }
}
