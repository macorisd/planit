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
import com.example.planit.activity.NoteCreateActivity;
import com.example.planit.entity.SingletonEntity;
import com.example.planit.entity.note.NoteAdapter;
import com.example.planit.entity.note.NoteManager;

public class NoteFragment extends Fragment {
    private NoteManager noteManager;

    public static final String SHARED_NOTE_LIST = "SHARED_NOTE_LIST";

    // Initialize NoteManager and Singleton
    private void initNoteManager() {
        // Try to get NoteManager from Singleton
        noteManager = (NoteManager) SingletonEntity.getInstance().get(SHARED_NOTE_LIST);
        if (noteManager == null) {
            // If not present, create a new instance and store it in Singleton
            noteManager = new NoteManager(requireContext());
            SingletonEntity.getInstance().put(SHARED_NOTE_LIST, noteManager);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for the fragment
        View view = inflater.inflate(R.layout.fragment_note_list, container, false);

        // Initialize NoteManager and set up the data
        initNoteManager();
        noteManager.initNoteManager();

        // Set up RecyclerView to display notes
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewNotes);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        NoteAdapter noteAdapter = new NoteAdapter(noteManager.getAllNotes());
        recyclerView.setAdapter(noteAdapter);

        // Set up the button to create a new note
        view.findViewById(R.id.buttonAddNote).setOnClickListener(v -> {
            // Launch NoteCreateActivity to add a new note
            Intent intent = new Intent(requireContext(), NoteCreateActivity.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Update the RecyclerView when the fragment is resumed
        if (getView() != null) {
            RecyclerView recyclerView = getView().findViewById(R.id.recyclerViewNotes);
            NoteAdapter noteAdapter = new NoteAdapter(noteManager.getAllNotes());
            recyclerView.setAdapter(noteAdapter);
        }
    }
}
