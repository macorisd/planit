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
import com.example.planit.activity.NoteEditActivity;
import com.example.planit.entity.SingletonEntity;
import com.example.planit.entity.note.NoteAdapter;
import com.example.planit.entity.note.NoteManager;
import com.example.planit.entity.note.Note;

public class NoteFragment extends Fragment {
    private NoteManager noteManager;

    public static final String SHARED_NOTE_LIST = "SHARED_NOTE_LIST";

    private void initNoteManager() {
        noteManager = (NoteManager) SingletonEntity.getInstance().get(SHARED_NOTE_LIST);
        if (noteManager == null) {
            noteManager = new NoteManager(requireContext());
            SingletonEntity.getInstance().put(SHARED_NOTE_LIST, noteManager);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_list, container, false);

        initNoteManager();
        noteManager.initNoteManager();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewNotes);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        NoteAdapter noteAdapter = new NoteAdapter(noteManager.getAllNotes());
        recyclerView.setAdapter(noteAdapter);

        view.findViewById(R.id.buttonAddNote).setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), NoteCreateActivity.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getView() != null) {
            RecyclerView recyclerView = getView().findViewById(R.id.recyclerViewNotes);
            NoteAdapter noteAdapter = new NoteAdapter(noteManager.getAllNotes());
            recyclerView.setAdapter(noteAdapter);
        }
    }
}