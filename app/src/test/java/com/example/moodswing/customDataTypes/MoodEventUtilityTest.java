package com.example.moodswing.customDataTypes;


import org.junit.Test;

import static org.junit.Assert.*;

public class MoodEventUtilityTest {

    //testing getDateStr method, which is supposed to convert UTC to Date String
    @Test
    public void getDateStr() {
        //setting input values
        Long input = 1574938070120L;

        //setting expected value
        String expected =  "November 28, 2019";

        //getting output provided by method
        MoodEventUtility moodEventUtility = new MoodEventUtility();
        String output = MoodEventUtility.getDateStr(input);

        //assert to check if method works as expecting, comparing expected and output
        assertEquals(expected, output);
    }

    //testing getTimeStr method, which is supposed to convert UTC to Time String
    @Test
    public void getTimeStr() {
        //setting input values
        Long input = 1574938070120L;

        //setting expected value
        String expected = "3:47 AM";

        //getting output provided by method
        MoodEventUtility moodEventUtility = new MoodEventUtility();
        String output =  MoodEventUtility.getTimeStr(input);

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
        output = MoodEventUtility.getMoodType(input);

        //assert to check if method works as expecting, comparing expected and output
        assertEquals(expected, output);
    }

    //testing returnMonthStr method, which is supposed to int of Month to Corresponding String

}