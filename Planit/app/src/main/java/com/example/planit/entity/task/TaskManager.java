package com.example.planit.entity.task;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.planit.dbhelper.DbHelper;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    private SQLiteDatabase db;

    public TaskManager(Context context) {
        db = DbHelper.getInstance(context).getDatabase();
    }

    // Método para inicializar tareas de ejemplo en la BD
    public void initTaskManager() {
//        ContentValues values = new ContentValues();
//        values.put(TaskContract.TaskEntry.COLUMN_NAME_NAME, "Tarea 1");
//        values.put(TaskContract.TaskEntry.COLUMN_NAME_DESCRIPTION, "Descripción de tarea 1");
//        values.put(TaskContract.TaskEntry.COLUMN_NAME_COMPLETED, 0); // 0 para no completada
//        values.put(TaskContract.TaskEntry.COLUMN_NAME_IMPORTANCE, 1);
//        values.put(TaskContract.TaskEntry.COLUMN_NAME_SUBJECT_ID, 1);
//        values.put(TaskContract.TaskEntry.COLUMN_NAME_TYPE, "Tipo 1");
//        values.put(TaskContract.TaskEntry.COLUMN_NAME_DUE_DATE, "2024-12-25");
//        values.put(TaskContract.TaskEntry.COLUMN_NAME_DUE_TIME, "12:00:00");
//        db.insert(TaskContract.TaskEntry.TABLE_NAME, null, values);
//
//        values.put(TaskContract.TaskEntry.COLUMN_NAME_NAME, "Tarea 2");
//        values.put(TaskContract.TaskEntry.COLUMN_NAME_DESCRIPTION, "Descripción de tarea 2");
//        values.put(TaskContract.TaskEntry.COLUMN_NAME_COMPLETED, 1); // 1 para completada
//        values.put(TaskContract.TaskEntry.COLUMN_NAME_IMPORTANCE, 2);
//        values.put(TaskContract.TaskEntry.COLUMN_NAME_SUBJECT_ID, 2);
//        values.put(TaskContract.TaskEntry.COLUMN_NAME_TYPE, "Tipo 2");
//        values.put(TaskContract.TaskEntry.COLUMN_NAME_DUE_DATE, "2024-12-30");
//        values.put(TaskContract.TaskEntry.COLUMN_NAME_DUE_TIME, "15:00:00");
//        db.insert(TaskContract.TaskEntry.TABLE_NAME, null, values);
    }

    // COMPLETAR: MÉTODO PARA OBTENER LAS TAREAS
    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        String[] projection = {
                TaskContract.TaskEntry._ID,
                TaskContract.TaskEntry.COLUMN_NAME_NAME,
                TaskContract.TaskEntry.COLUMN_NAME_DESCRIPTION,
                TaskContract.TaskEntry.COLUMN_NAME_COMPLETED,
                TaskContract.TaskEntry.COLUMN_NAME_IMPORTANCE,
                TaskContract.TaskEntry.COLUMN_NAME_TYPE,
                TaskContract.TaskEntry.COLUMN_NAME_SUBJECT_ID,
                TaskContract.TaskEntry.COLUMN_NAME_DUE_DATE,
                TaskContract.TaskEntry.COLUMN_NAME_DUE_TIME
        };

        Cursor cursor = db.query(
                TaskContract.TaskEntry.TABLE_NAME,  // Nombre de la tabla
                projection,                        // Columnas a devolver
                null,                              // Condición WHERE (null para todas)
                null,                              // Argumentos para la condición
                null,                              // GROUP BY
                null,                              // HAVING
                null                               // ORDER BY
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_NAME_NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_NAME_DESCRIPTION));
            boolean completed = cursor.getInt(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_NAME_COMPLETED)) == 1;
            int importance = cursor.getInt(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_NAME_IMPORTANCE));
            String type = cursor.getString(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_NAME_TYPE));
            int subjectId = cursor.getInt(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_NAME_SUBJECT_ID));
            String dueDate = cursor.getString(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_NAME_DUE_DATE));
            String dueTime = cursor.getString(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_NAME_DUE_TIME));

            // Crear un objeto Task con todos los datos recuperados
            tasks.add(new Task(id, name, description, completed, importance, type, subjectId, dueDate, dueTime));
        }
        cursor.close();

        return tasks;
    }


    public void createTask(String name, String description, int importance, String type, int subjectId, String dueDate, String dueTime) {
        // Verificar que los parámetros no estén vacíos
        if (name.isEmpty() || dueDate.isEmpty() || type.isEmpty()) {
            throw new IllegalArgumentException("Alguno de los campos obligatorios está vacío");
        }

        // Crear un objeto ContentValues para insertar los valores en la base de datos
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COLUMN_NAME_NAME, name);
        values.put(TaskContract.TaskEntry.COLUMN_NAME_DESCRIPTION, description);
        values.put(TaskContract.TaskEntry.COLUMN_NAME_COMPLETED, 0);  // Tarea no completada inicialmente
        values.put(TaskContract.TaskEntry.COLUMN_NAME_IMPORTANCE, importance);
        values.put(TaskContract.TaskEntry.COLUMN_NAME_TYPE, type);
        values.put(TaskContract.TaskEntry.COLUMN_NAME_SUBJECT_ID, subjectId);
        values.put(TaskContract.TaskEntry.COLUMN_NAME_DUE_DATE, dueDate);
        values.put(TaskContract.TaskEntry.COLUMN_NAME_DUE_TIME, dueTime);

        // Insertar los valores en la base de datos
        long newRowId = db.insert(TaskContract.TaskEntry.TABLE_NAME, null, values);


        if (newRowId == -1) {
            // Si la inserción falla
            throw new RuntimeException("Error al guardar la tarea");
        }
    }

    // Método para editar una tarea
    public void editTask(int taskId, String name, String description, int importance, String type, int subjectId, String dueDate, String dueTime, int completed) {
        // Verificar que los parámetros no estén vacíos
        if (name.isEmpty() || dueDate.isEmpty() || type.isEmpty()) {
            throw new IllegalArgumentException("Todos los campos son obligatorios");
        }

        // Crear un objeto ContentValues con los nuevos valores
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COLUMN_NAME_NAME, name);
        values.put(TaskContract.TaskEntry.COLUMN_NAME_DESCRIPTION, description);
        values.put(TaskContract.TaskEntry.COLUMN_NAME_COMPLETED, completed);
        values.put(TaskContract.TaskEntry.COLUMN_NAME_IMPORTANCE, importance);
        values.put(TaskContract.TaskEntry.COLUMN_NAME_TYPE, type);
        values.put(TaskContract.TaskEntry.COLUMN_NAME_SUBJECT_ID, subjectId);
        values.put(TaskContract.TaskEntry.COLUMN_NAME_DUE_DATE, dueDate);
        values.put(TaskContract.TaskEntry.COLUMN_NAME_DUE_TIME, dueTime);

        // Especificar la condición para actualizar la tarea con el ID correspondiente
        String selection = TaskContract.TaskEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(taskId)};

        // Actualizar la tarea en la base de datos
        int rowsUpdated = db.update(
                TaskContract.TaskEntry.TABLE_NAME,  // Nombre de la tabla
                values,                             // Nuevos valores
                selection,                          // Condición de selección (ID de la tarea)
                selectionArgs                      // Argumentos para la condición
        );

        if (rowsUpdated == 0) {
            // Si no se actualizó ninguna fila, lanzar un error
            throw new RuntimeException("Error al actualizar la tarea");
        }
    }

    // Método para alternar el estado de compleción de una tarea
    public void toggleCompleted(int taskId) {
        // Consultar el estado actual de la tarea
        String[] projection = { TaskContract.TaskEntry.COLUMN_NAME_COMPLETED };
        String selection = TaskContract.TaskEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(taskId) };

        Cursor cursor = db.query(
                TaskContract.TaskEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            int currentCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_NAME_COMPLETED));
            cursor.close();

            // Alternar el estado de compleción
            int newCompleted = (currentCompleted == 0) ? 1 : 0;

            // Actualizar el valor en la base de datos
            ContentValues values = new ContentValues();
            values.put(TaskContract.TaskEntry.COLUMN_NAME_COMPLETED, newCompleted);

            int rowsUpdated = db.update(
                    TaskContract.TaskEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs
            );

            if (rowsUpdated == 0) {
                throw new RuntimeException("Error al alternar el estado de compleción de la tarea");
            }
        } else {
            cursor.close();
            throw new IllegalArgumentException("No se encontró una tarea con el ID proporcionado");
        }
    }




    // Método para eliminar una tarea
    public void deleteTask(int taskId) {
        String selection = TaskContract.TaskEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(taskId)};

        // Eliminar la tarea de la base de datos
        db.delete(TaskContract.TaskEntry.TABLE_NAME, selection, selectionArgs);
    }

    protected void finalize() throws Throwable {
        db.close();
        super.finalize();
    }
}
