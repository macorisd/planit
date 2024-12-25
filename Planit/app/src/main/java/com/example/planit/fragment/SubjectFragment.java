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
//import com.example.planit.activity.SubjectEditActivity;
import com.example.planit.activity.SubjectEditActivity;
import com.example.planit.entity.SingletonEntity;
import com.example.planit.entity.subject.SubjectAdapter;
import com.example.planit.entity.subject.SubjectManager;
import com.example.planit.entity.subject.Subject;

public class SubjectFragment extends Fragment {
    private SubjectManager subjectManager;

    public static final String SHARED_SUBJECT_LIST = "SHARED_SUBJECT_LIST";

    private void initSubjectManager() {
        subjectManager = (SubjectManager) SingletonEntity.getInstance().get(SHARED_SUBJECT_LIST);
        if (subjectManager == null) {
            subjectManager = new SubjectManager(requireContext());
            SingletonEntity.getInstance().put(SHARED_SUBJECT_LIST, subjectManager);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subject_list, container, false);

        initSubjectManager();
//        subjectManager.initSubjectManager();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewSubjects);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        SubjectAdapter subjectAdapter = new SubjectAdapter(subjectManager.getSubjects());
        recyclerView.setAdapter(subjectAdapter);

        view.findViewById(R.id.buttonAddSubject).setOnClickListener(v -> {
            Subject newSubject = subjectManager.createSubject("", 0);
            Intent editIntent = new Intent(requireContext(), SubjectEditActivity.class);
            editIntent.putExtra("SUBJECT_ID", newSubject.getId());
            editIntent.putExtra("SUBJECT_NAME", newSubject.getName());
            editIntent.putExtra("SUBJECT_COLOR", newSubject.getColor());
            startActivity(editIntent);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getView() != null) {
            RecyclerView recyclerView = getView().findViewById(R.id.recyclerViewSubjects);
            SubjectAdapter subjectAdapter = new SubjectAdapter(subjectManager.getSubjects());
            recyclerView.setAdapter(subjectAdapter);
        }
    }
}