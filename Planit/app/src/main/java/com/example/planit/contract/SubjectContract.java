package com.example.planit.contract;

import android.provider.BaseColumns;

public final class SubjectContract {
    private SubjectContract() {}

    public static abstract class SubjectEntry implements BaseColumns {
        public static final String TABLE_NAME = "subject";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_COLOR = "color";
    }
}