package com.akshar.todolist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.akshar.todolist.model.ToDo;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "todo.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_TODO = "todos";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_COMPLETED = "completed";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_TODO + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_TIME + " TEXT, " +
                COLUMN_COMPLETED + " INTEGER DEFAULT 0);";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
        onCreate(db);
    }

    public long addTodoItem(String title, String description, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_COMPLETED, 0);
        return db.insert(TABLE_TODO, null, values);
    }

    public int updateTodoItem(String id, String title, String description, String date, String time, int completed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_COMPLETED, completed);
        return db.update(TABLE_TODO, values, COLUMN_ID + " = ?", new String[]{id});
    }

    public int deleteTodoItem(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_TODO, COLUMN_ID + " = ?", new String[]{id});
    }

    public List<ToDo> getAllTodoItems() {
        List<ToDo> todoList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TODO;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                ToDo todo = new ToDo();
                int idIndex = cursor.getColumnIndex(COLUMN_ID);
                if (idIndex != -1) {
                    todo.setId(cursor.getString(idIndex));
                }

                int titleIndex = cursor.getColumnIndex(COLUMN_TITLE);
                if (titleIndex != -1) {
                    todo.setTitle(cursor.getString(titleIndex));
                }

                int descriptionIndex = cursor.getColumnIndex(COLUMN_DESCRIPTION);
                if (descriptionIndex != -1) {
                    todo.setDescription(cursor.getString(descriptionIndex));
                }

                int dateIndex = cursor.getColumnIndex(COLUMN_DATE);
                if (dateIndex != -1) {
                    todo.setDate(cursor.getString(dateIndex));
                }

                int timeIndex = cursor.getColumnIndex(COLUMN_TIME);
                if (timeIndex != -1) {
                    todo.setTime(cursor.getString(timeIndex));
                }

                int completedIndex = cursor.getColumnIndex(COLUMN_COMPLETED);
                if (completedIndex != -1) {
                    todo.setCompleted(cursor.getInt(completedIndex) == 1);
                }
                todoList.add(todo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return todoList;
    }
}