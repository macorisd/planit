package com.example.planit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planit.R;
import com.example.planit.entity.SingletonEntity;
import com.example.planit.entity.subject.SubjectAdapter;
import com.example.planit.entity.subject.SubjectManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SubjectListActivity extends AppCompatActivity {
    public static final String SHARED_SUBJECT_LIST = "SHARED_SUBJECT_LIST";

    private SubjectManager subjectManager;

    private void initSubjectManager() {
        subjectManager = (SubjectManager) SingletonEntity.getInstance().get(SHARED_SUBJECT_LIST);
        if (subjectManager == null) {
            subjectManager = new SubjectManager(getApplicationContext());
            SingletonEntity.getInstance().put(SHARED_SUBJECT_LIST, subjectManager);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_list);

        initSubjectManager();
        subjectManager.initSubjectManager();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewSubjects);

        // Configure the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SubjectAdapter subjectAdapter = new SubjectAdapter(subjectManager.getSubjects());
        recyclerView.setAdapter(subjectAdapter);

        // Configure the button to add a subject
        Button addButton = findViewById(R.id.buttonAddSubject);
        addButton.setOnClickListener(v -> {
            // Start the activity to add a new subject
//            Intent intent = new Intent(SubjectListActivity.this, SubjectEditActivity.class);
//            startActivity(intent);
        });

        // Configure the bottom navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_tasks) {
                // Navigate to the task activity
                Intent taskIntent = new Intent(SubjectListActivity.this, MainActivity.class);
                startActivity(taskIntent);
                return true;
            } else if (itemId == R.id.nav_notes) {
                // Navigate to the note activity
                Intent noteIntent = new Intent(SubjectListActivity.this, NoteListActivity.class);
                startActivity(noteIntent);
                return true;
            } else if (itemId == R.id.nav_subjects) {
                // Stay in the current activity
                return true;
            } else {
                return false;
            }
        });

        // Set the selected item as "Subjects"
        bottomNavigationView.setSelectedItemId(R.id.nav_subjects);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Update the list of subjects
        RecyclerView recyclerView = findViewById(R.id.recyclerViewSubjects);
        SubjectAdapter subjectAdapter = new SubjectAdapter(subjectManager.getSubjects());
        recyclerView.setAdapter(subjectAdapter);
    }
}