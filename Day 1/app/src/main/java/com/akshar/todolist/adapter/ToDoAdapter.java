package com.akshar.todolist.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akshar.todolist.R;
import com.akshar.todolist.model.ToDo;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDOViewHolder> {

    private List<ToDo> taskList;
    private OnItemClickListener listener;
    private OnCheckBoxClickListener checkBoxListener;
    private OnItemLongClickListener longClickListener;

    public ToDoAdapter(List<ToDo> taskList) {
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public ToDOViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item, parent, false);
        return new ToDOViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDOViewHolder holder, int position) {
        ToDo todo = taskList.get(position);
        holder.title.setText(todo.getTitle());
        holder.checkBox.setChecked(todo.isCompleted());

        // make the text strike through if the task is completed
        if (todo.isCompleted()) {
            holder.title.setPaintFlags(holder.title.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.title.setPaintFlags(holder.title.getPaintFlags() & (~android.graphics.Paint.STRIKE_THRU_TEXT_FLAG));
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(taskList.get(position));
            }
        });
        holder.checkBox.setOnClickListener(v -> {
            taskList.get(position).setCompleted(holder.checkBox.isChecked());
            if (checkBoxListener != null) {
                checkBoxListener.onCheckBoxClick(taskList.get(position));
            }
        });
        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onItemLongClick(taskList.get(position));
            }
            return true;
        });
    }

    public void updateTodoList(List<ToDo> todoList) {
        taskList.clear();
        taskList.addAll(todoList);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnCheckBoxClickListener(OnCheckBoxClickListener listener) {
        this.checkBoxListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class ToDOViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public CheckBox checkBox;

        public ToDOViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.todoTitle);
            checkBox = itemView.findViewById(R.id.todoCheckBox);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ToDo toDo);
    }

    public interface OnCheckBoxClickListener {
        void onCheckBoxClick(ToDo toDo);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(ToDo toDo);
    }
}
