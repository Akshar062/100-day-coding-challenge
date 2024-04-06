package com.akshar.todoapp

import java.sql.Time
import java.util.Date

object Utils {
    val currentDate: String
        get() {
            // return current date in MM/DD/YYYY format
            var date = Date(System.currentTimeMillis()).toString()
            date = date.substring(4, 10) + ", " + date.substring(date.length - 4)
            return date
        }
    val currentTime: String
        get() =// return time in HH:MM format
            Time(System.currentTimeMillis()).toString()
}
