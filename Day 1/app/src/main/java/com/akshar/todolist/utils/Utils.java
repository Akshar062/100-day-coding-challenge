package com.akshar.todolist.utils;

import java.sql.Time;
import java.util.Date;

public class Utils {

        public static String getCurrentDate() {
            // return current Date in DD/mm/yyyy format
            return new Date().toString();
        }
        public static String getCurrentTime() {
            // return time in HH:MM format
            return new Time(System.currentTimeMillis()).toString();
        }
}
