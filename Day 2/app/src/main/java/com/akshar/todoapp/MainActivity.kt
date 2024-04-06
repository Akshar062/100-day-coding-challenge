package com.akshar.todoapp

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.akshar.todoapp.adapter.ToDoAdapter
import com.akshar.todoapp.database.DatabaseHelper
import com.akshar.todoapp.model.ToDo
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private var toDoAdapter: ToDoAdapter? = null
    private var databaseHelper: DatabaseHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val todoView: RecyclerView = findViewById(R.id.todoList)
        val addButton: FloatingActionButton = findViewById(R.id.addTodo)
        addButton.setOnClickListener {
            val addTodoFragment = AddTodoFragment()
            addTodoFragment.show(supportFragmentManager, addTodoFragment.tag)
        }
        // Initialize DatabaseHelper
        databaseHelper = DatabaseHelper(this)
        val todoList: MutableList<ToDo> = databaseHelper!!.allTodoItems.toMutableList()
        toDoAdapter = ToDoAdapter(todoList)
        // Set the adapter to the RecyclerView
        todoView.setAdapter(toDoAdapter)
        toDoAdapter?.setOnItemClickListener(object : ToDoAdapter.OnItemClickListener {
            override fun onItemClick(toDo: ToDo?) {
                toDo?.let { showTodoDetails(it) }
            }
        })
        toDoAdapter?.setOnCheckBoxClickListener(object : ToDoAdapter.OnCheckBoxClickListener {
            override fun onCheckBoxClick(toDo: ToDo?) {
                toDo?.let { updateCheckBox(it) }
            }
        })
        toDoAdapter?.setOnItemLongClickListener(object : ToDoAdapter.OnItemLongClickListener {
            override fun onItemLongClick(toDo: ToDo?) {
                toDo?.let { deleteTodoItem(it) }
            }
        })
    }

    private fun deleteTodoItem(toDo: ToDo) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Item")
        builder.setMessage("Are you sure you want to delete this item?")
        builder.setPositiveButton(
            "Delete"
        ) { _: DialogInterface?, _: Int ->
            databaseHelper?.deleteTodoItem(toDo.id.toString())
            val todoList: List<ToDo> = databaseHelper?.allTodoItems ?: emptyList()
            toDoAdapter?.updateTodoList(todoList)
        }
        builder.setNegativeButton("Cancel", null)
        val dialog = builder.create()
        dialog.show()
    }

    private fun updateCheckBox(toDo: ToDo) {
        databaseHelper?.updateTodoItem(
            toDo.id.toString(),
            toDo.title,
            toDo.description,
            toDo.date,
            toDo.time,
            if (toDo.completed) 1 else 0
        )
        val todoList: List<ToDo> = databaseHelper?.allTodoItems ?: emptyList()
        toDoAdapter?.updateTodoList(todoList)
    }

    private fun showTodoDetails(item: ToDo) {
        val addTodoFragment = AddTodoFragment(item)
        addTodoFragment.show(supportFragmentManager, addTodoFragment.tag)
    }

    fun addTodoItem(toDo: ToDo) {
        databaseHelper?.addTodoItem(
            toDo.title,
            toDo.description,
            toDo.date,
            toDo.time
        )
        val todoList: List<ToDo> = databaseHelper?.allTodoItems ?: emptyList()
        toDoAdapter?.updateTodoList(todoList)
    }

    fun updateTodoItem(toDo: ToDo) {
        databaseHelper?.updateTodoItem(
            toDo.id.toString(),
            toDo.title,
            toDo.description,
            toDo.date,
            toDo.time,
            if (toDo.completed) 1 else 0
        )
        val todoList: List<ToDo> = databaseHelper?.allTodoItems ?: emptyList()
        toDoAdapter?.updateTodoList(todoList)
    }
}