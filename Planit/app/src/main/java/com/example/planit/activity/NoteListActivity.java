package com.example.planit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planit.R;
import com.example.planit.entity.SingletonEntity;
import com.example.planit.entity.note.NoteAdapter;
import com.example.planit.entity.note.NoteManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NoteListActivity extends AppCompatActivity {
    public static final String SHARED_NOTE_LIST = "SHARED_NOTE_LIST";

    private NoteManager noteManager;

    private void initNoteManager() {
        noteManager = (NoteManager) SingletonEntity.getInstance().get(NoteListActivity.SHARED_NOTE_LIST);
        if (noteManager == null) {
            noteManager = new NoteManager(getApplicationContext());
            SingletonEntity.getInstance().put(NoteListActivity.SHARED_NOTE_LIST, noteManager);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        initNoteManager();
        noteManager.initNoteManager();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewNotes);

        // Configure el RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        NoteAdapter noteAdapter = new NoteAdapter(noteManager.getAllNotes());
        recyclerView.setAdapter(noteAdapter);

        // Configurar el botón para añadir una nota
        findViewById(R.id.buttonAddNote).setOnClickListener(v -> {
            // Iniciar la actividad para añadir una nueva nota
//            Intent intent = new Intent(NoteListActivity.this, NoteCreateActivity.class);
//            startActivity(intent);
        });

        // Configurar la barra de navegación inferior
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_tasks) {
                // Navegar a la actividad de tareas
                Intent taskIntent = new Intent(NoteListActivity.this, MainActivity.class);
                startActivity(taskIntent);
                return true;
            } else if (itemId == R.id.nav_notes) {
                // Mantenerse en la actividad actual
                return true;
            } else {
                return false;
            }
        });

        // Establecer el ítem seleccionado como "Notas"
        bottomNavigationView.setSelectedItemId(R.id.nav_notes);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Actualizar la lista de notas
        RecyclerView recyclerView = findViewById(R.id.recyclerViewNotes);
        NoteAdapter noteAdapter = new NoteAdapter(noteManager.getAllNotes());
        recyclerView.setAdapter(noteAdapter);
    }
}
