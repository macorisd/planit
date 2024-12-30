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

    // Constructor to initialize the TaskManager with a database instance
    public TaskManager(Context context) {
        db = DbHelper.getInstance(context).getDatabase();
    }

    // Method to initialize the TaskManager (currently commented out as a sample)
    public void initTaskManager() {
        // You can initialize some sample tasks here if needed.
        // ContentValues values = new ContentValues();
        // values.put(TaskContract.TaskEntry.COLUMN_NAME_NAME, "Task 1");
        // values.put(TaskContract.TaskEntry.COLUMN_NAME_DESCRIPTION, "Description of task 1");
        // values.put(TaskContract.TaskEntry.COLUMN_NAME_COMPLETED, 0); // 0 for incomplete
        // values.put(TaskContract.TaskEntry.COLUMN_NAME_IMPORTANCE, 1);
        // values.put(TaskContract.TaskEntry.COLUMN_NAME_SUBJECT_ID, 1);
        // values.put(TaskContract.TaskEntry.COLUMN_NAME_TYPE, "Type 1");
        // values.put(TaskContract.TaskEntry.COLUMN_NAME_DUE_DATE, "2024-12-25");
        // values.put(TaskContract.TaskEntry.COLUMN_NAME_DUE_TIME, "12:00:00");
        // db.insert(TaskContract.TaskEntry.TABLE_NAME, null, values);
    }

    // Method to fetch all tasks from the database
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

        // Query to select all tasks from the database
        Cursor cursor = db.query(
                TaskContract.TaskEntry.TABLE_NAME,  // Table name
                projection,                        // Columns to return
                null,                              // WHERE clause (null for all rows)
                null,                              // Arguments for WHERE clause
                null,                              // GROUP BY
                null,                              // HAVING
                null                               // ORDER BY
        );

        // Loop through the results and create Task objects
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

            // Create a Task object with the data from the database
            tasks.add(new Task(id, name, description, completed, importance, type, subjectId, dueDate, dueTime));
        }
        cursor.close();

        return tasks;
    }

    // Method to create a new task and insert it into the database
    public void createTask(String name, String description, int importance, String type, int subjectId, String dueDate, String dueTime) {
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COLUMN_NAME_NAME, name);
        values.put(TaskContract.TaskEntry.COLUMN_NAME_DESCRIPTION, description);
        values.put(TaskContract.TaskEntry.COLUMN_NAME_COMPLETED, 0); // New tasks are incomplete by default
        values.put(TaskContract.TaskEntry.COLUMN_NAME_IMPORTANCE, importance);
        values.put(TaskContract.TaskEntry.COLUMN_NAME_TYPE, type);
        values.put(TaskContract.TaskEntry.COLUMN_NAME_SUBJECT_ID, subjectId);
        values.put(TaskContract.TaskEntry.COLUMN_NAME_DUE_DATE, dueDate);
        values.put(TaskContract.TaskEntry.COLUMN_NAME_DUE_TIME, dueTime);

        long newRowId = db.insert(TaskContract.TaskEntry.TABLE_NAME, null, values);

        // If the insertion fails, throw an exception
        if (newRowId == -1) {
            throw new RuntimeException("Error saving the task");
        }
    }

    // Method to edit an existing task
    public void editTask(int taskId, String name, String description, int importance, String type, int subjectId, String dueDate, String dueTime, int completed) {
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COLUMN_NAME_NAME, name);
        values.put(TaskContract.TaskEntry.COLUMN_NAME_DESCRIPTION, description);
        values.put(TaskContract.TaskEntry.COLUMN_NAME_COMPLETED, completed);
        values.put(TaskContract.TaskEntry.COLUMN_NAME_IMPORTANCE, importance);
        values.put(TaskContract.TaskEntry.COLUMN_NAME_TYPE, type);
        values.put(TaskContract.TaskEntry.COLUMN_NAME_SUBJECT_ID, subjectId);
        values.put(TaskContract.TaskEntry.COLUMN_NAME_DUE_DATE, dueDate);
        values.put(TaskContract.TaskEntry.COLUMN_NAME_DUE_TIME, dueTime);

        String selection = TaskContract.TaskEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(taskId)};

        // Update the task in the database based on its ID
        int rowsUpdated = db.update(
                TaskContract.TaskEntry.TABLE_NAME,  // Table name
                values,                             // New values
                selection,                          // WHERE clause (task ID)
                selectionArgs                      // Arguments for WHERE clause
        );

        // If no rows were updated, throw an exception
        if (rowsUpdated == 0) {
            throw new RuntimeException("Error updating the task");
        }
    }

    // Method to toggle the completion status of a task
    public void toggleCompleted(int taskId) {
        String[] projection = { TaskContract.TaskEntry.COLUMN_NAME_COMPLETED };
        String selection = TaskContract.TaskEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(taskId) };

        // Query to get the current completion status of the task
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

            // Toggle the completion status (0 to 1 or 1 to 0)
            int newCompleted = (currentCompleted == 0) ? 1 : 0;

            ContentValues values = new ContentValues();
            values.put(TaskContract.TaskEntry.COLUMN_NAME_COMPLETED, newCompleted);

            // Update the task in the database
            int rowsUpdated = db.update(
                    TaskContract.TaskEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs
            );

            // If no rows were updated, throw an exception
            if (rowsUpdated == 0) {
                throw new RuntimeException("Error toggling the task completion status");
            }
        } else {
            cursor.close();
            throw new IllegalArgumentException("No task found with the provided ID");
        }
    }

    // Method to delete a task from the database
    public void deleteTask(int taskId) {
        String selection = TaskContract.TaskEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(taskId)};

        // Delete the task from the database based on its ID
        db.delete(TaskContract.TaskEntry.TABLE_NAME, selection, selectionArgs);
    }

    // Finalize method to close the database connection when the object is garbage collected
    protected void finalize() throws Throwable {
        db.close();
        super.finalize();
    }
}
