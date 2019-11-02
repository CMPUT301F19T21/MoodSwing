package com.example.moodswing.customDataTypes;

import java.io.Serializable;
import java.sql.Time;

/**
 * This class creates an Time object
 *      int hr
 *      int min
 */
public class TimeJar {
    private int hr;
    private int min;

    public TimeJar(){}

    public TimeJar(int hr, int min){
        this.setHr(hr);
        this.setMin(min);

    }

    public int getHr() {
        return hr;
    }

    public void setHr(int hr) {
        this.hr = hr;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }
}