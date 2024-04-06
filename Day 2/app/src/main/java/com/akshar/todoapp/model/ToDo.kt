package com.akshar.todoapp.model

data class ToDo(
    var id: Int,
    var title: String,
    var description: String,
    var date: String,
    var time: String,
    var completed: Boolean
)

