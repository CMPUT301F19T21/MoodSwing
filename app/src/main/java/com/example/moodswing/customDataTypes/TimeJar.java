package com.example.moodswing.customDataTypes;

import java.io.Serializable;
import java.sql.Time;


/**
 * This class creates an Time object
 *      int hr(24-hour clock)
 *      int min
 */
//To add: enforcing upper and lower bound, adding seconds field
public class TimeJar {
    private Integer hr;
    private Integer min;

    public TimeJar(){}

    /**
     * Every TimeJar has the hour(in 24-hour clock), and the minutes
     * @param hr hours, 1-24
     * @param min minutes, 0-59
     */
    public TimeJar(int hr, int min){
        this.setHr(hr);
        this.setMin(min);

    }

    public Integer getHr() {
        return hr;
    }

    public void setHr(Integer hr) {
        this.hr = hr;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

}