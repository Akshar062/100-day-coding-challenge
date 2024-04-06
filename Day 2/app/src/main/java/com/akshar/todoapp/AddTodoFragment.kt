package com.akshar.todoapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import com.akshar.todoapp.model.ToDo
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class AddTodoFragment : BottomSheetDialogFragment {
    private var title: TextInputEditText? = null
    private var description: TextInputEditText? = null
    private var date: TextInputEditText? = null
    private var time: TextInputEditText? = null
    private var close: ImageView? = null
    private var addTodo: MaterialButton? = null
    private var saveBtn: MaterialButton? = null
    private var completed: CheckBox? = null
    private var toDo: ToDo? = null

    constructor()
    constructor(toDo: ToDo?) {
        this.toDo = toDo
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.add_todo_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title = view.findViewById(R.id.todoTitle)
        description = view.findViewById(R.id.todoDescription)
        date = view.findViewById(R.id.todoDate)
        time = view.findViewById(R.id.todoTime)
        addTodo = view.findViewById(R.id.addTodo)
        close = view.findViewById(R.id.close)
        saveBtn = view.findViewById(R.id.saveTodo)
        completed = view.findViewById(R.id.todoCheckBox)
        if (toDo != null) {
            showDetails()
        } else {
            showAddTodo()
        }
    }

    private fun showAddTodo() {
        saveBtn?.visibility = View.GONE
        // set current date and time
        date?.setText(Utils.currentDate)
        time?.setText(Utils.currentTime)
        close?.setOnClickListener { dismiss() }
        addTodo?.setOnClickListener {
            val todoTitle: String = title?.text.toString()
            val todoDescription: String = description?.text.toString()
            val todoDate: String = date?.text.toString()
            val todoTime: String = time?.text.toString()
            if (todoTitle.isEmpty()) {
                title?.error = "Title cannot be empty"
                return@setOnClickListener
            }
            if (todoDescription.isEmpty()) {
                description?.error = "Description cannot be empty"
                return@setOnClickListener
            }
            if (todoDate.isEmpty()) {
                date?.error = "Date cannot be empty"
                return@setOnClickListener
            }
            if (todoTime.isEmpty()) {
                time?.error = "Time cannot be empty"
                return@setOnClickListener
            }
            val toDo = ToDo(
                id = 0, // Assuming 0 as default id
                title = todoTitle,
                description = todoDescription,
                date = todoDate,
                time = todoTime,
                completed = completed?.isChecked ?: false
            )
            (requireActivity() as MainActivity).addTodoItem(toDo)
            dismiss()
        }
    }

    private fun showDetails() {
        title?.setText(toDo?.title)
        description?.setText(toDo?.description)
        date?.setText(toDo?.date)
        time?.setText(toDo?.time)
        completed?.setChecked(toDo?.completed ?: false)
        disableFields()
        addTodo?.text = getString(R.string.edit)
        saveBtn?.visibility = View.VISIBLE
        close?.setOnClickListener { dismiss() }
        addTodo?.setOnClickListener { enableFields() }
        saveBtn?.setOnClickListener {
            val todoTitle: String = title?.text.toString()
            val todoDescription: String = description?.text.toString()
            val todoDate: String = date?.text.toString()
            val todoTime: String = time?.text.toString()
            if (todoTitle.isEmpty()) {
                title?.error = "Title cannot be empty"
                return@setOnClickListener
            }
            if (todoDescription.isEmpty()) {
                description?.error = "Description cannot be empty"
                return@setOnClickListener
            }
            if (todoDate.isEmpty()) {
                date?.error = "Date cannot be empty"
                return@setOnClickListener
            }
            if (todoTime.isEmpty()) {
                time?.error = "Time cannot be empty"
                return@setOnClickListener
            }
            toDo?.title = todoTitle
            toDo?.description = todoDescription
            toDo?.date = todoDate
            toDo?.time = todoTime
            toDo?.completed = completed?.isChecked ?: false
            toDo?.let { it1 -> (requireActivity() as MainActivity).updateTodoItem(it1) }
            dismiss()
        }
    }
    private fun enableFields() {
        title?.isEnabled = true
        description?.isEnabled = true
        date?.isEnabled = true
        time?.isEnabled = true
    }

    private fun disableFields() {
        title?.isEnabled = false
        description?.isEnabled = false
        date?.isEnabled = false
        time?.isEnabled = false
    }
}