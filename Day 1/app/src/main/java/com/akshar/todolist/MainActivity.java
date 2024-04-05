package com.akshar.todolist;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private ExpandableListView todoList;

    private FloatingActionButton addButton;

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

        todoList = findViewById(R.id.todoList);
        addButton = findViewById(R.id.addTodo);

        // on click listener for add button
        addButton.setOnClickListener(v -> {
            showAddDialog();
        });
    }

    private void showAddDialog() {
        // create a dialog
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_todo_dialog);
        dialog.show();

        // get the views from the dialog
        EditText title = dialog.findViewById(R.id.title);
        EditText description = dialog.findViewById(R.id.description);
        Button addButton = dialog.findViewById(R.id.addButton);

        // on click listener for add button
        addButton.setOnClickListener(v -> {
            // get the title and description
            String titleText = title.getText().toString();
            String descriptionText = description.getText().toString();
            // add the todo to the list
            addTodoToList(titleText, descriptionText);
            // dismiss the dialog
            dialog.dismiss();
        });
    }
}