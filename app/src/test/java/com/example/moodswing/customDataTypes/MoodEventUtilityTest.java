package com.example.moodswing.customDataTypes;


import org.junit.Test;

import static org.junit.Assert.*;

public class MoodEventUtilityTest {

    //testing getDateStr method, which is supposed to convert DateJar to String
    @Test
    public void getDateStr() {
        //setting input values
        DateJar input = new DateJar();
        input.setDay(13);
        input.setMonth(2);
        input.setYear(2019);

        //setting expected value
        String expected =  "March 13, 2019";

        //getting output provided by method
        MoodEventUtility moodEventUtility = new MoodEventUtility();
        String output = moodEventUtility.getDateStr(input);

        //assert to check if method works as expecting, comparing expected and output
        assertEquals(expected, output);
    }

    //testing getTimeStr method, which is supposed to convert TimeJar to String
    @Test
    public void getTimeStr() {
        //setting input values
        TimeJar input = new TimeJar();
        input.setHr(13);
        input.setMin(45);

        //setting expected value
        String expected = "1:45 PM";

        //getting output provided by method
        MoodEventUtility moodEventUtility = new MoodEventUtility();
        String output =  moodEventUtility.getTimeStr(input);

        //assert to check if method works as expecting, comparing expected and output
        assertEquals(expected, output);
    }

    @Test
    public void getMoodType() {
        //initializing values
        int input;
        String expected;
        MoodEventUtility moodEventUtility = new MoodEventUtility();
        String output;

        //setting values
        input = 1;
        expected = "Happy";
        output = moodEventUtility.getMoodType(input);

        //assert to check if method works as expecting, comparing expected and output
        assertEquals(expected, output);
    }

    //testing returnMonthStr method, which is supposed to int of Month to Corresponding String
    @Test
    public void returnMonthStr() {
        //setting input values
        int input = 4;

        //setting expected value
        String expected = "May";

        //getting output provided by method
        MoodEventUtility moodEventUtility = new MoodEventUtility();
        String output = moodEventUtility.returnMonthStr(input);

        //assert to check if method works as expecting, comparing expected and output
        assertEquals(expected,output);
    }
}