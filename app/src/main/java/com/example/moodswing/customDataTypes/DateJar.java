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
//A DateJar object contains a specific date, including Year,month, and day
public class DateJar {
    private Integer year;
    private Integer month;
    private Integer day;

    public DateJar(){}

    /**
     * @param year The year
     * @param month The month
     * @param day The day of the month
     */
    public DateJar(int year, int month, int day) {
        this.setYear(year);
        this.setMonth(month);
        this.setDay(day);
    }


    public Integer getYear() {
        return year;
    }



    public Integer getMonth() {
        return month;
    }



    public Integer getDay() {
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