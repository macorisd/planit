package com.example.planit.entity.subject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.planit.dbhelper.DbHelper;

import java.util.ArrayList;
import java.util.List;

public class SubjectManager {

    private SQLiteDatabase db;

    // Constructor to initialize the database
    public SubjectManager(Context context) {
        db = DbHelper.getInstance(context).getDatabase();
    }

    // Initialize the subject manager (currently not used for any action)
    public void initSubjectManager() {
        // Uncomment and use this method to insert test data if needed
//        ContentValues values = new ContentValues();
//        values.put(SubjectContract.SubjectEntry.COLUMN_NAME_NAME, "Subject 1");
//        values.put(SubjectContract.SubjectEntry.COLUMN_NAME_COLOR, 0xFF000000);
//        db.insert(SubjectContract.SubjectEntry.TABLE_NAME, null, values);
//
//        values.put(SubjectContract.SubjectEntry.COLUMN_NAME_NAME, "Subject 2");
//        values.put(SubjectContract.SubjectEntry.COLUMN_NAME_COLOR, 0xFF000000);
//        db.insert(SubjectContract.SubjectEntry.TABLE_NAME, null, values);
    }

    // Retrieve a list of all subjects from the database
    public List<Subject> getSubjects() {
        List<Subject> subjects = new ArrayList<>();
        String[] projection = {
                SubjectContract.SubjectEntry._ID,
                SubjectContract.SubjectEntry.COLUMN_NAME_NAME,
                SubjectContract.SubjectEntry.COLUMN_NAME_COLOR
        };

        Cursor cursor = db.query(
                SubjectContract.SubjectEntry.TABLE_NAME,
                projection,
                null,  // No selection criteria
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(SubjectContract.SubjectEntry._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(SubjectContract.SubjectEntry.COLUMN_NAME_NAME));
            int color = cursor.getInt(cursor.getColumnIndexOrThrow(SubjectContract.SubjectEntry.COLUMN_NAME_COLOR));

            subjects.add(new Subject(id, name, color));
        }
        cursor.close(); // Always close the cursor to avoid memory leaks

        return subjects;
    }

    // Retrieve a single subject by its ID
    public Subject getById(int id) {
        String[] projection = {
                SubjectContract.SubjectEntry._ID,
                SubjectContract.SubjectEntry.COLUMN_NAME_NAME,
                SubjectContract.SubjectEntry.COLUMN_NAME_COLOR
        };

        String selection = SubjectContract.SubjectEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query(
                SubjectContract.SubjectEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(SubjectContract.SubjectEntry.COLUMN_NAME_NAME));
            int color = cursor.getInt(cursor.getColumnIndexOrThrow(SubjectContract.SubjectEntry.COLUMN_NAME_COLOR));

            return new Subject(id, name, color);
        }

        return null;
    }

    // Create a new subject and insert it into the database
    public Subject createSubject(String name, int color) {
        ContentValues values = new ContentValues();
        values.put(SubjectContract.SubjectEntry.COLUMN_NAME_NAME, name);
        values.put(SubjectContract.SubjectEntry.COLUMN_NAME_COLOR, color);

        long newRowId = db.insert(SubjectContract.SubjectEntry.TABLE_NAME, null, values);

        if (newRowId == -1) {
            throw new RuntimeException("Error creating subject");
        }

        return new Subject((int) newRowId, name, color);
    }

    // Edit an existing subject by updating its name and color
    public void editSubject(int subjectId, String name, int color) {
        ContentValues values = new ContentValues();
        values.put(SubjectContract.SubjectEntry.COLUMN_NAME_NAME, name);
        values.put(SubjectContract.SubjectEntry.COLUMN_NAME_COLOR, color);

        String selection = SubjectContract.SubjectEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(subjectId)};

        int rowsUpdated = db.update(
                SubjectContract.SubjectEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );

        if (rowsUpdated == 0) {
            throw new RuntimeException("Error updating subject");
        }
    }

    // Delete a subject from the database by its ID
    public void deleteSubject(int subjectId) {
        String selection = SubjectContract.SubjectEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(subjectId)};

        db.delete(SubjectContract.SubjectEntry.TABLE_NAME, selection, selectionArgs);
    }

    // Close the database connection when the object is garbage collected
    protected void finalize() throws Throwable {
        db.close();
        super.finalize();
    }
}
