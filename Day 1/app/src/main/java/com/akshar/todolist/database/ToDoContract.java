package com.akshar.todolist.database;

import android.provider.BaseColumns;

public final class ToDoContract {
    private ToDoContract() {}

    public static class ToDoEntry implements BaseColumns {
        public static final String TABLE_NAME = "todos";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_COMPLETED = "completed";
    }
}
