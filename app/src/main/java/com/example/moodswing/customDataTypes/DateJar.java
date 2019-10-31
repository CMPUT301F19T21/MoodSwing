package com.example.moodswing.customDataTypes;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.util.Date;


/**
 * This class creates an Date object
 *      int year
 *      int month
 *      int day
 */
public class DateJar {
    private int year;
    private int month;
    private int day;

    public DateJar(){}

    public DateJar(int year, int month, int day) {
        this.setYear(year);
        this.setMonth(month);
        this.setDay(day);
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }
}