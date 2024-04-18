package com.akshar.todolist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.akshar.todolist.model.ToDo;
import com.akshar.todolist.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class AddTodoFragment extends BottomSheetDialogFragment {

    private TextInputEditText title, description, date, time;
    private ImageView close;
    private MaterialButton addTodo,saveBtn;
    private CheckBox completed;
    private ToDo toDo;
    public AddTodoFragment() {
    }

    // constructor that accepts a ToDo object
    public AddTodoFragment(ToDo toDo) {
        this.toDo = toDo;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.add_todo_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        title = view.findViewById(R.id.todoTitle);
        description = view.findViewById(R.id.todoDescription);
        date = view.findViewById(R.id.todoDate);
        time = view.findViewById(R.id.todoTime);
        addTodo = view.findViewById(R.id.addTodo);
        close = view.findViewById(R.id.close);
        saveBtn = view.findViewById(R.id.saveTodo);
        completed = view.findViewById(R.id.todoCheckBox);

        if (toDo != null) {
            showDetails();
        } else {
            showAddTodo();
        }
    }

    private void showAddTodo() {
        saveBtn.setVisibility(View.GONE);
        // set current date and time
        date.setText(Utils.getCurrentDate());
        time.setText(Utils.getCurrentTime());

        close.setOnClickListener(v -> dismiss());

        addTodo.setOnClickListener(v -> {
            String todoTitle = Objects.requireNonNull(title.getText()).toString();
            String todoDescription = Objects.requireNonNull(description.getText()).toString();
            String todoDate = Objects.requireNonNull(date.getText()).toString();
            String todoTime = Objects.requireNonNull(time.getText()).toString();

            if (todoTitle.isEmpty()) {
                title.setError("Title cannot be empty");
                return;
            }

            if (todoDescription.isEmpty()) {
                description.setError("Description cannot be empty");
                return;
            }

            if (todoDate.isEmpty()) {
                date.setError("Date cannot be empty");
                return;
            }

            if (todoTime.isEmpty()) {
                time.setError("Time cannot be empty");
                return;
            }

            ToDo toDo = new ToDo(todoTitle, todoDescription, todoDate, todoTime, todoTime, completed.isChecked());
            ((MainActivity) requireActivity()).addTodoItem(toDo);
            dismiss();
        });
    }

    private void showDetails() {
        title.setText(toDo.getTitle());
        description.setText(toDo.getDescription());
        date.setText(toDo.getDate());
        time.setText(toDo.getTime());
        completed.setChecked(toDo.isCompleted());
        disableFields();
        addTodo.setText("Edit");
        saveBtn.setVisibility(View.VISIBLE);

        close.setOnClickListener(v -> dismiss());

        addTodo.setOnClickListener(v -> enableFields());
        saveBtn.setOnClickListener(v -> {
            String todoTitle = Objects.requireNonNull(title.getText()).toString();
            String todoDescription = Objects.requireNonNull(description.getText()).toString();
            String todoDate = Objects.requireNonNull(date.getText()).toString();
            String todoTime = Objects.requireNonNull(time.getText()).toString();

            if (todoTitle.isEmpty()) {
                title.setError("Title cannot be empty");
                return;
            }

            if (todoDescription.isEmpty()) {
                description.setError("Description cannot be empty");
                return;
            }

            if (todoDate.isEmpty()) {
                date.setError("Date cannot be empty");
                return;
            }

            if (todoTime.isEmpty()) {
                time.setError("Time cannot be empty");
                return;
            }

            toDo.setTitle(todoTitle);
            toDo.setDescription(todoDescription);
            toDo.setDate(todoDate);
            toDo.setTime(todoTime);
            toDo.setCompleted(completed.isChecked());

            ((MainActivity) requireActivity()).updateTodoItem(toDo);
            dismiss();
        });


    }

    private void enableFields() {
        title.setEnabled(true);
        description.setEnabled(true);
        date.setEnabled(true);
        time.setEnabled(true);
    }

    private void disableFields() {
        title.setEnabled(false);
        description.setEnabled(false);
        date.setEnabled(false);
        time.setEnabled(false);
    }
}