package com.akshar.todolist.utils;

import java.sql.Time;
import java.util.Date;

public class Utils {

        public static String getCurrentDate() {
            // return current date in MM/DD/YYYY format
            String date = new Date(System.currentTimeMillis()).toString();
            date = date.substring(4, 10) + ", " + date.substring(date.length() - 4);
            return date;
        }
        public static String getCurrentTime() {
            // return time in HH:MM format
            return new Time(System.currentTimeMillis()).toString();
        }
}
