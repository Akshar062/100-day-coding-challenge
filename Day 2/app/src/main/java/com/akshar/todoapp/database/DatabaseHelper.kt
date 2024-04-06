package com.akshar.todoapp.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.akshar.todoapp.model.ToDo

class DatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE " + TABLE_TODO + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_TIME + " TEXT, " +
                COLUMN_COMPLETED + " INTEGER DEFAULT 0);"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO)
        onCreate(db)
    }

    fun addTodoItem(title: String?, description: String?, date: String?, time: String?): Long {
        val db: SQLiteDatabase = this.getWritableDatabase()
        val values = ContentValues()
        values.put(COLUMN_TITLE, title)
        values.put(COLUMN_DESCRIPTION, description)
        values.put(COLUMN_DATE, date)
        values.put(COLUMN_TIME, time)
        values.put(COLUMN_COMPLETED, 0)
        return db.insert(TABLE_TODO, null, values)
    }

    fun updateTodoItem(
        id: String,
        title: String?,
        description: String?,
        date: String?,
        time: String?,
        completed: Int
    ): Int {
        val db: SQLiteDatabase = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_TITLE, title)
        values.put(COLUMN_DESCRIPTION, description)
        values.put(COLUMN_DATE, date)
        values.put(COLUMN_TIME, time)
        values.put(COLUMN_COMPLETED, completed)
        return db.update(TABLE_TODO, values, "$COLUMN_ID = ?", arrayOf(id))
    }

    fun deleteTodoItem(id: String): Int {
        val db: SQLiteDatabase = this.writableDatabase
        return db.delete(TABLE_TODO, "$COLUMN_ID = ?", arrayOf(id))
    }


    val allTodoItems: List<ToDo>
        get() {
            val todoList: MutableList<ToDo> = ArrayList()
            val selectQuery = "SELECT * FROM $TABLE_TODO"
            val db: SQLiteDatabase = this.writableDatabase
            val cursor: Cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    val todo = ToDo(
                        id = 1,
                        title = "Example Title",
                        description = "Example Description",
                        date = "2024-04-06",
                        time = "10:00 AM",
                        completed = false
                    )
                    val idIndex = cursor.getColumnIndex(COLUMN_ID)
                    if (idIndex != -1) {
                        todo.id = cursor.getInt(idIndex)
                    }
                    val titleIndex = cursor.getColumnIndex(COLUMN_TITLE)
                    if (titleIndex != -1) {
                        todo.title = cursor.getString(titleIndex)
                    }
                    val descriptionIndex = cursor.getColumnIndex(COLUMN_DESCRIPTION)
                    if (descriptionIndex != -1) {
                        todo.description = cursor.getString(descriptionIndex)
                    }
                    val dateIndex = cursor.getColumnIndex(COLUMN_DATE)
                    if (dateIndex != -1) {
                        todo.date = cursor.getString(dateIndex)
                    }
                    val timeIndex = cursor.getColumnIndex(COLUMN_TIME)
                    if (timeIndex != -1) {
                        todo.time = cursor.getString(timeIndex)
                    }
                    val completedIndex = cursor.getColumnIndex(COLUMN_COMPLETED)
                    if (completedIndex != -1) {
                        todo.completed = cursor.getInt(completedIndex) == 1
                    }
                    todoList.add(todo)
                } while (cursor.moveToNext())
            }
            cursor.close()
            return todoList
        }

    companion object {
        private const val DATABASE_NAME = "todo.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_TODO = "todos"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_DATE = "date"
        const val COLUMN_TIME = "time"
        const val COLUMN_COMPLETED = "completed"
    }
}