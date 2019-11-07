package com.example.moodswing.customDataTypes;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestMoodEventUtility {

    @Test
    public void testReturnMonthStr() {
        MoodEvent mood = new MoodEvent();
        DateJar date = new DateJar(1993,10,10);
        mood.setDate(date);

        assertEquals(mood.getDate(), date);
    }


}
