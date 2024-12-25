package com.example.planit.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.planit.contract.NoteContract;
import com.example.planit.contract.SubjectContract;
import com.example.planit.contract.TaskContract;

public class DbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "planitDB.db";
    public static final int DATABASE_VERSION = 1;
    private static DbHelper instance;

    public static DbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DbHelper(context.getApplicationContext());
        }
        return instance;
    }

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Crear la tabla task con las columnas definidas en TaskContract
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

    // Eliminar la tabla task si existe
    private static final String SQL_DELETE_TASK_ENTRIES =
            "DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE_NAME;


    // Crear la tabla note con las columnas definidas en NoteContract
    private static final String SQL_CREATE_NOTE_ENTRIES =
            "CREATE TABLE " + NoteContract.NoteEntry.TABLE_NAME + " (" +
                    NoteContract.NoteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    NoteContract.NoteEntry.COLUMN_NAME_NAME + " TEXT," +
                    NoteContract.NoteEntry.COLUMN_NAME_CONTENT + " TEXT" +
                    " )";

    // Eliminar la tabla note si existe
    private static final String SQL_DELETE_NOTE_ENTRIES =
            "DROP TABLE IF EXISTS " + NoteContract.NoteEntry.TABLE_NAME;

    // Crear la tabla subject con las columnas definidas en SubjectContract
    private static final String SQL_CREATE_SUBJECT_ENTRIES =
            "CREATE TABLE " + SubjectContract.SubjectEntry.TABLE_NAME + " (" +
                    SubjectContract.SubjectEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SubjectContract.SubjectEntry.COLUMN_NAME_NAME + " TEXT," +
                    SubjectContract.SubjectEntry.COLUMN_NAME_COLOR + " INTEGER" +
                    " )";

    // Eliminar la tabla subject si existe
    private static final String SQL_DELETE_SUBJECT_ENTRIES =
            "DROP TABLE IF EXISTS " + SubjectContract.SubjectEntry.TABLE_NAME;

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TASK_ENTRIES);
        sqLiteDatabase.execSQL(SQL_CREATE_NOTE_ENTRIES);
        sqLiteDatabase.execSQL(SQL_CREATE_SUBJECT_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_TASK_ENTRIES);
        sqLiteDatabase.execSQL(SQL_DELETE_NOTE_ENTRIES);
        sqLiteDatabase.execSQL(SQL_DELETE_SUBJECT_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TASK_ENTRIES);
        db.execSQL(SQL_DELETE_NOTE_ENTRIES);
        db.execSQL(SQL_DELETE_SUBJECT_ENTRIES);
        onCreate(db);
    }
}
