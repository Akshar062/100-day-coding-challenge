package com.akshar.todoapp.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.akshar.todoapp.R
import com.akshar.todoapp.model.ToDo

class ToDoAdapter(taskList: MutableList<ToDo>) : RecyclerView.Adapter<ToDoAdapter.ToDOViewHolder?>() {
    private val taskList: MutableList<ToDo>
    private var listener: OnItemClickListener? = null
    private var checkBoxListener: OnCheckBoxClickListener? = null
    private var longClickListener: OnItemLongClickListener? = null

    init {
        this.taskList = taskList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDOViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false)
        return ToDOViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: ToDOViewHolder, position: Int) {
        val todo: ToDo = taskList[position]
        holder.title.text = todo.title
        holder.checkBox.setChecked(todo.completed)

        // make the text strike through if the task is completed
        if (todo.completed) {
            holder.title.paintFlags = holder.title.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.title.paintFlags = holder.title.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
        holder.itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onItemClick(taskList[position])
            }
        }
        holder.checkBox.setOnClickListener {
            taskList[position].completed = holder.checkBox.isChecked
            if (checkBoxListener != null) {
                checkBoxListener!!.onCheckBoxClick(taskList[position])
            }
        }
        holder.itemView.setOnLongClickListener {
            if (longClickListener != null) {
                longClickListener!!.onItemLongClick(taskList[position])
            }
            true
        }
    }

    fun updateTodoList(todoList: List<ToDo>?) {
        taskList.clear()
        taskList.addAll(todoList!!)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    fun setOnCheckBoxClickListener(listener: OnCheckBoxClickListener?) {
        checkBoxListener = listener
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener?) {
        longClickListener = listener
    }

    class ToDOViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView
        var checkBox: CheckBox

        init {
            title = itemView.findViewById(R.id.todoTitle)
            checkBox = itemView.findViewById(R.id.todoCheckBox)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(toDo: ToDo?)
    }

    interface OnCheckBoxClickListener {
        fun onCheckBoxClick(toDo: ToDo?)
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(toDo: ToDo?)
    }
}
