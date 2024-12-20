package com.example.planit.entity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.planit.contract.TaskContract;
import com.example.planit.dbhelper.TaskDbHelper;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    private SQLiteDatabase db;

    public TaskManager(Context context, String dbName) {
        TaskDbHelper dbHelper = new TaskDbHelper(context, dbName);
        db = dbHelper.getWritableDatabase();
    }

    // Método para inicializar tareas de ejemplo en la BD
    public void initTaskManager() {
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COLUMN_NAME_NAME, "Tarea 1");
        values.put(TaskContract.TaskEntry.COLUMN_NAME_DESCRIPTION, "Descripción de tarea 1");
        values.put(TaskContract.TaskEntry.COLUMN_NAME_COMPLETED, 0); // 0 para no completada
        values.put(TaskContract.TaskEntry.COLUMN_NAME_IMPORTANCE, 1);
        values.put(TaskContract.TaskEntry.COLUMN_NAME_SUBJECT_ID, 1);
        values.put(TaskContract.TaskEntry.COLUMN_NAME_TYPE, "Tipo 1");
        values.put(TaskContract.TaskEntry.COLUMN_NAME_DUE_DATE, "2024-12-25");
        values.put(TaskContract.TaskEntry.COLUMN_NAME_DUE_TIME, "12:00:00");
        db.insert(TaskContract.TaskEntry.TABLE_NAME, null, values);

        values.put(TaskContract.TaskEntry.COLUMN_NAME_NAME, "Tarea 2");
        values.put(TaskContract.TaskEntry.COLUMN_NAME_DESCRIPTION, "Descripción de tarea 2");
        values.put(TaskContract.TaskEntry.COLUMN_NAME_COMPLETED, 1); // 1 para completada
        values.put(TaskContract.TaskEntry.COLUMN_NAME_IMPORTANCE, 2);
        values.put(TaskContract.TaskEntry.COLUMN_NAME_SUBJECT_ID, 2);
        values.put(TaskContract.TaskEntry.COLUMN_NAME_TYPE, "Tipo 2");
        values.put(TaskContract.TaskEntry.COLUMN_NAME_DUE_DATE, "2024-12-30");
        values.put(TaskContract.TaskEntry.COLUMN_NAME_DUE_TIME, "15:00:00");
        db.insert(TaskContract.TaskEntry.TABLE_NAME, null, values);
    }

    // COMPLETAR: MÉTODO PARA OBTENER LAS TAREAS
    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        String[] projection = {
                TaskContract.TaskEntry.COLUMN_NAME_NAME,
                TaskContract.TaskEntry.COLUMN_NAME_DESCRIPTION,
                TaskContract.TaskEntry.COLUMN_NAME_COMPLETED,
                TaskContract.TaskEntry.COLUMN_NAME_IMPORTANCE
        };

        Cursor cursor = db.query(
                TaskContract.TaskEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_NAME_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_NAME_NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_NAME_DESCRIPTION));
            int completed = cursor.getInt(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_NAME_COMPLETED));
            int importance = cursor.getInt(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_NAME_IMPORTANCE));

            tasks.add(new Task(id, name, description, completed == 1, importance));
        }
        cursor.close();

        return tasks;
    }

    // Método para eliminar una tarea
    public void deleteTask(int taskId) {
        String selection = TaskContract.TaskEntry.COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = {String.valueOf(taskId)};

        // Eliminar la tarea de la base de datos
        db.delete(TaskContract.TaskEntry.TABLE_NAME, selection, selectionArgs);
    }



    @Override
    public void finalize() throws Throwable {
        db.close();
        super.finalize();
    }
}
