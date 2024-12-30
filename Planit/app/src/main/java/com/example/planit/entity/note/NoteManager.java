package com.example.planit.entity.note;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.planit.dbhelper.DbHelper;

import java.util.ArrayList;
import java.util.List;

public class NoteManager {

    private SQLiteDatabase db;

    public NoteManager(Context context) {
        db = DbHelper.getInstance(context).getDatabase();
    }

    // Método para inicializar notas de ejemplo en la BD
    public void initNoteManager() {
//        ContentValues values = new ContentValues();
//        values.put(NoteContract.NoteEntry.COLUMN_NAME_NAME, "Nota 1");
//        values.put(NoteContract.NoteEntry.COLUMN_NAME_CONTENT, "Contenido de la nota 1");
//        db.insert(NoteContract.NoteEntry.TABLE_NAME, null, values);
//
//        values.put(NoteContract.NoteEntry.COLUMN_NAME_NAME, "Nota 2");
//        values.put(NoteContract.NoteEntry.COLUMN_NAME_CONTENT, "Contenido de la nota 2");
//        db.insert(NoteContract.NoteEntry.TABLE_NAME, null, values);
    }

    // Method to get all notes
    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();
        String[] projection = {
                NoteContract.NoteEntry._ID,
                NoteContract.NoteEntry.COLUMN_NAME_NAME,
                NoteContract.NoteEntry.COLUMN_NAME_CONTENT
        };

        Cursor cursor = db.query(
                NoteContract.NoteEntry.TABLE_NAME,  // Table name
                projection,                         // Columns to return
                null,                               // WHERE clause
                null,                               // WHERE clause arguments
                null,                               // GROUP BY
                null,                               // HAVING
                null                                // ORDER BY
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(NoteContract.NoteEntry._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(NoteContract.NoteEntry.COLUMN_NAME_NAME));
            String content = cursor.getString(cursor.getColumnIndexOrThrow(NoteContract.NoteEntry.COLUMN_NAME_CONTENT));

            notes.add(new Note(id, name, content));
        }
        cursor.close();

        return notes;
    }

    // Method to create a new note
    public Note createNote(String name, String content) {
        ContentValues values = new ContentValues();
        values.put(NoteContract.NoteEntry.COLUMN_NAME_NAME, name);
        values.put(NoteContract.NoteEntry.COLUMN_NAME_CONTENT, content);

        long newRowId = db.insert(NoteContract.NoteEntry.TABLE_NAME, null, values);

        if (newRowId == -1) {
            throw new RuntimeException("Error saving the note");
        }

        return new Note((int) newRowId, name, content);
    }

    // Method to edit an existing note
    public void editNote(int noteId, String name, String content) {
        ContentValues values = new ContentValues();
        values.put(NoteContract.NoteEntry.COLUMN_NAME_NAME, name);
        values.put(NoteContract.NoteEntry.COLUMN_NAME_CONTENT, content);

        String selection = NoteContract.NoteEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(noteId)};

        int rowsUpdated = db.update(
                NoteContract.NoteEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );

        if (rowsUpdated == 0) {
            throw new RuntimeException("Error updating the note");
        }
    }

    // Method to delete a note
    public void deleteNote(int noteId) {
        String selection = NoteContract.NoteEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(noteId)};

        db.delete(NoteContract.NoteEntry.TABLE_NAME, selection, selectionArgs);
    }

    protected void finalize() throws Throwable {
        db.close();
        super.finalize();
    }
}