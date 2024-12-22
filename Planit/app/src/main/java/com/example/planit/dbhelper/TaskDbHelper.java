package com.example.planit.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.planit.contract.TaskContract;

public class TaskDbHelper extends DbHelper {
    // Crear la tabla task con las columnas definidas en TaskContract
    private static final String SQL_CREATE_ENTRIES =
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
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE_NAME;

    public TaskDbHelper(Context context) {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear la tabla de tareas
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Actualizar la base de datos eliminando y recreando la tabla
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // En caso de una degradación, eliminamos y recreamos la tabla
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
