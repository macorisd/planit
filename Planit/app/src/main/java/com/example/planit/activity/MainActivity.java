package com.example.planit.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.planit.R;
import com.example.planit.fragment.NoteFragment;
import com.example.planit.fragment.SubjectFragment;
import com.example.planit.fragment.TaskFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);

        // Initialize BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set default fragment if there's no saved state
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new TaskFragment())
                    .commit();
        }

        // Set listener for bottom navigation item selection
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            // Select fragment based on clicked navigation item
            int itemId = item.getItemId();
            if (itemId == R.id.nav_tasks) {
                selectedFragment = new TaskFragment();
            } else if (itemId == R.id.nav_subjects) {
                selectedFragment = new SubjectFragment();
            } else if (itemId == R.id.nav_notes) {
                selectedFragment = new NoteFragment();
            }

            // Replace fragment if a valid selection was made
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });
    }
}
