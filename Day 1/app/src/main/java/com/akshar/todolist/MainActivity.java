package com.akshar.todolist;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.akshar.todolist.adapter.ToDoAdapter;
import com.akshar.todolist.database.DatabaseHelper;
import com.akshar.todolist.model.ToDo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ToDoAdapter toDoAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView todoView = findViewById(R.id.todoList);
        FloatingActionButton addButton = findViewById(R.id.addTodo);
        addButton.setOnClickListener(v -> {
            AddTodoFragment addTodoFragment = new AddTodoFragment();
            addTodoFragment.show(getSupportFragmentManager(), addTodoFragment.getTag());
        });
        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);
        List<ToDo> todoList = databaseHelper.getAllTodoItems();
        toDoAdapter = new ToDoAdapter(todoList);
        // Set the adapter to the RecyclerView
        todoView.setAdapter(toDoAdapter);
        toDoAdapter.setOnItemClickListener(this::showTodoDetails);
        toDoAdapter.setOnCheckBoxClickListener(this::updateCheckBox);
        toDoAdapter.setOnItemLongClickListener(this::deleteTodoItem);
    }

    private void deleteTodoItem(ToDo toDo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Item");
        builder.setMessage("Are you sure you want to delete this item?");
        builder.setPositiveButton("Delete", (dialog, which) -> {
            databaseHelper.deleteTodoItem(toDo.getId());
            List<ToDo> todoList = databaseHelper.getAllTodoItems();
            toDoAdapter.updateTodoList(todoList);
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void updateCheckBox(@NonNull ToDo toDo) {
        databaseHelper.updateTodoItem(toDo.getId(), toDo.getTitle(), toDo.getDescription(), toDo.getDate(), toDo.getTime(), toDo.isCompleted() ? 1 : 0);
        List<ToDo> todoList = databaseHelper.getAllTodoItems();
        toDoAdapter.updateTodoList(todoList);
    }
    private void showTodoDetails(ToDo item) {
        AddTodoFragment addTodoFragment = new AddTodoFragment(item);
        addTodoFragment.show(getSupportFragmentManager(), addTodoFragment.getTag());

    }
    public void addTodoItem(@NonNull ToDo toDo) {
        databaseHelper.addTodoItem(toDo.getTitle(), toDo.getDescription(), toDo.getDate(), toDo.getTime());
        List<ToDo> todoList = databaseHelper.getAllTodoItems();
        toDoAdapter.updateTodoList(todoList);
    }
    public void updateTodoItem(@NonNull ToDo toDo) {
        databaseHelper.updateTodoItem(toDo.getId(), toDo.getTitle(), toDo.getDescription(), toDo.getDate(), toDo.getTime(), toDo.isCompleted() ? 1 : 0);
        List<ToDo> todoList = databaseHelper.getAllTodoItems();
        toDoAdapter.updateTodoList(todoList);
    }
}