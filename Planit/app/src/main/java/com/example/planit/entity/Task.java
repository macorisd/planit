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

public class Task {

    private SQLiteDatabase db;

    public Task(Context context, String dbName) {
        TaskDbHelper dbHelper = new TaskDbHelper(context, dbName);
        db = dbHelper.getWritableDatabase();
    }

    // Método para inicializar tareas de ejemplo en la BD
    public void initTasks() {
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

    // Método para buscar tarea por nombre
    @SuppressLint("Range")
    public String searchTaskByName(String taskName) {
        String taskDetails = "";

        String[] columns = {
                TaskContract.TaskEntry._ID,
                TaskContract.TaskEntry.COLUMN_NAME_NAME,
                TaskContract.TaskEntry.COLUMN_NAME_DESCRIPTION,
                TaskContract.TaskEntry.COLUMN_NAME_COMPLETED,
                TaskContract.TaskEntry.COLUMN_NAME_IMPORTANCE,
                TaskContract.TaskEntry.COLUMN_NAME_SUBJECT_ID,
                TaskContract.TaskEntry.COLUMN_NAME_TYPE,
                TaskContract.TaskEntry.COLUMN_NAME_DUE_DATE,
                TaskContract.TaskEntry.COLUMN_NAME_DUE_TIME
        };
        String where = TaskContract.TaskEntry.COLUMN_NAME_NAME + " = ?";
        String[] whereArgs = { taskName };
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE_NAME, columns, where, whereArgs, null, null, null);
        try {
            while (cursor.moveToNext()) {
                taskDetails = "ID: " + cursor.getInt(cursor.getColumnIndex(TaskContract.TaskEntry._ID)) + "\n" +
                        "Nombre: " + cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_NAME)) + "\n" +
                        "Descripción: " + cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_DESCRIPTION)) + "\n" +
                        "Completada: " + cursor.getInt(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_COMPLETED)) + "\n" +
                        "Importancia: " + cursor.getInt(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_IMPORTANCE)) + "\n" +
                        "Fecha de vencimiento: " + cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_DUE_DATE)) + "\n" +
                        "Hora de vencimiento: " + cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_DUE_TIME));
            }
        } finally {
            cursor.close();
        }
        return taskDetails;
    }

    // Método para añadir una nueva tarea
    public void addTask(String name, String description, int completed, int importance, int subjectId, String type, String dueDate, String dueTime) {
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COLUMN_NAME_NAME, name);
        values.put(TaskContract.TaskEntry.COLUMN_NAME_DESCRIPTION, description);
        values.put(TaskContract.TaskEntry.COLUMN_NAME_COMPLETED, completed);
        values.put(TaskContract.TaskEntry.COLUMN_NAME_IMPORTANCE, importance);
        values.put(TaskContract.TaskEntry.COLUMN_NAME_SUBJECT_ID, subjectId);
        values.put(TaskContract.TaskEntry.COLUMN_NAME_TYPE, type);
        values.put(TaskContract.TaskEntry.COLUMN_NAME_DUE_DATE, dueDate);
        values.put(TaskContract.TaskEntry.COLUMN_NAME_DUE_TIME, dueTime);
        db.insert(TaskContract.TaskEntry.TABLE_NAME, null, values);
    }

    // Método para obtener todas las tareas
    @SuppressLint("Range")
    public List<String> getAllTasks() {
        List<String> taskList = new ArrayList<>();

        String[] columns = {
                TaskContract.TaskEntry._ID,
                TaskContract.TaskEntry.COLUMN_NAME_NAME,
                TaskContract.TaskEntry.COLUMN_NAME_DESCRIPTION,
                TaskContract.TaskEntry.COLUMN_NAME_COMPLETED,
                TaskContract.TaskEntry.COLUMN_NAME_IMPORTANCE,
                TaskContract.TaskEntry.COLUMN_NAME_SUBJECT_ID,
                TaskContract.TaskEntry.COLUMN_NAME_TYPE,
                TaskContract.TaskEntry.COLUMN_NAME_DUE_DATE,
                TaskContract.TaskEntry.COLUMN_NAME_DUE_TIME
        };
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE_NAME, columns, null, null, null, null, null);
        try {
            while (cursor.moveToNext()) {
                String taskDetails = "ID: " + cursor.getInt(cursor.getColumnIndex(TaskContract.TaskEntry._ID)) + "\n" +
                        "Nombre: " + cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_NAME)) + "\n" +
                        "Descripción: " + cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_DESCRIPTION)) + "\n" +
                        "Completada: " + cursor.getInt(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_COMPLETED)) + "\n" +
                        "Importancia: " + cursor.getInt(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_IMPORTANCE)) + "\n" +
                        "Fecha de vencimiento: " + cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_DUE_DATE)) + "\n" +
                        "Hora de vencimiento: " + cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_DUE_TIME));
                taskList.add(taskDetails);
            }
        } finally {
            cursor.close();
        }
        return taskList;
    }

    @SuppressLint("Range")
    public String getNameFromCursor(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_NAME));
    }

    @SuppressLint("Range")
    public String getDescriptionFromCursor(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_DESCRIPTION));
    }

    @Override
    protected void finalize() throws Throwable {
        db.close();
        super.finalize();
    }
}
