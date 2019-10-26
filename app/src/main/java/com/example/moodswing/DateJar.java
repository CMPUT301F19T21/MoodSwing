package com.example.moodswing;

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

    public DateJar(int year, int month, int day) {
        this.setDate(year,month,day);
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

    // no point of having individual setter, so ignored for now

    public void setDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

}