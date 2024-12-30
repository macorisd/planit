package com.example.planit.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.planit.entity.note.NoteContract;
import com.example.planit.entity.subject.SubjectContract;
import com.example.planit.entity.task.TaskContract;

public class DbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "planitDB.db";
    public static final int DATABASE_VERSION = 1;
    private static DbHelper instance;
    private static SQLiteDatabase database;

    // Singleton pattern to get the instance of DbHelper
    public static DbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DbHelper(context.getApplicationContext());
        }
        return instance;
    }

    // Return the database instance, ensuring it's open
    public synchronized SQLiteDatabase getDatabase() {
        if (database == null || !database.isOpen()) {
            database = getWritableDatabase();
        }
        return database;
    }

    // Constructor for the DbHelper
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // SQL query to create the task table
    private static final String SQL_CREATE_TASK_ENTRIES =
            "CREATE TABLE " + TaskContract.TaskEntry.TABLE_NAME + " (" +
                    TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    TaskContract.TaskEntry.COLUMN_NAME_NAME + " TEXT," +
                    TaskContract.TaskEntry.COLUMN_NAME_DESCRIPTION + " TEXT," +
                    TaskContract.TaskEntry.COLUMN_NAME_COMPLETED + " INTEGER," +
                    TaskContract.TaskEntry.COLUMN_NAME_IMPORTANCE + " INTEGER," +
                    TaskContract.TaskEntry.COLUMN_NAME_SUBJECT_ID + " INTEGER," +
                    TaskContract.TaskEntry.COLUMN_NAME_TYPE + " TEXT," +
                    TaskContract.TaskEntry.COLUMN_NAME_DUE_DATE + " TEXT," +
                    TaskContract.TaskEntry.COLUMN_NAME_DUE_TIME + " TEXT" +
                    " )";

    // SQL query to delete the task table
    private static final String SQL_DELETE_TASK_ENTRIES =
            "DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE_NAME;

    // SQL query to create the note table
    private static final String SQL_CREATE_NOTE_ENTRIES =
            "CREATE TABLE " + NoteContract.NoteEntry.TABLE_NAME + " (" +
                    NoteContract.NoteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    NoteContract.NoteEntry.COLUMN_NAME_NAME + " TEXT," +
                    NoteContract.NoteEntry.COLUMN_NAME_CONTENT + " TEXT" +
                    " )";

    // SQL query to delete the note table
    private static final String SQL_DELETE_NOTE_ENTRIES =
            "DROP TABLE IF EXISTS " + NoteContract.NoteEntry.TABLE_NAME;

    // SQL query to create the subject table
    private static final String SQL_CREATE_SUBJECT_ENTRIES =
            "CREATE TABLE " + SubjectContract.SubjectEntry.TABLE_NAME + " (" +
                    SubjectContract.SubjectEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SubjectContract.SubjectEntry.COLUMN_NAME_NAME + " TEXT," +
                    SubjectContract.SubjectEntry.COLUMN_NAME_COLOR + " INTEGER" +
                    " )";

    // SQL query to delete the subject table
    private static final String SQL_DELETE_SUBJECT_ENTRIES =
            "DROP TABLE IF EXISTS " + SubjectContract.SubjectEntry.TABLE_NAME;

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create the tables in the database
        sqLiteDatabase.execSQL(SQL_CREATE_TASK_ENTRIES);
        sqLiteDatabase.execSQL(SQL_CREATE_NOTE_ENTRIES);
        sqLiteDatabase.execSQL(SQL_CREATE_SUBJECT_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Drop the old tables and recreate them for the new version
        sqLiteDatabase.execSQL(SQL_DELETE_TASK_ENTRIES);
        sqLiteDatabase.execSQL(SQL_DELETE_NOTE_ENTRIES);
        sqLiteDatabase.execSQL(SQL_DELETE_SUBJECT_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database downgrade by dropping tables and recreating them
        db.execSQL(SQL_DELETE_TASK_ENTRIES);
        db.execSQL(SQL_DELETE_NOTE_ENTRIES);
        db.execSQL(SQL_DELETE_SUBJECT_ENTRIES);
        onCreate(db);
    }

    @Override
    public void close() {
        // Close the database if it is open
        if (database != null && database.isOpen()) {
            Log.d("DbHelper", "Closing database");
            database.close();
        }
        super.close();
    }
}
