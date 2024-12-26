package com.example.planit.entity.note;

import android.provider.BaseColumns;

public final class NoteContract {
    private NoteContract() {}

    public static abstract class NoteEntry implements BaseColumns {
        public static final String TABLE_NAME = "note";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_CONTENT = "content";
    }
}
