package com.example.moodswing.customDataTypes;

import java.io.Serializable;
import java.sql.Time;


/**
 * This class creates an Time object
 *      int hr
 *      int min
 */
public class TimeJar {
    private Integer hr;
    private Integer min;

    public TimeJar(){}

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