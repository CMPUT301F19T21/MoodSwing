package com.example.moodswing.customDataTypes;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoodAdapterTest {



    @Test
    public void testAddMoodEvents() {

        ArrayList<MoodEvent> moods = new ArrayList<>();
        MoodAdapter moodAdapter = new MoodAdapter(moods);

        assertEquals(moods.size(),0);
        moodAdapter.addToMoods(new MoodEvent());
        assertEquals(moods.size(),1);

    }


    @Test
    public void  testClearMoodEvents() {
        ArrayList<MoodEvent> moods = new ArrayList<>();
        MoodAdapter moodAdapter = new MoodAdapter(moods);
        moodAdapter.addToMoods(new MoodEvent());
        assertEquals(moods.size(),1);
        moodAdapter.clearMoodEvents();
        assertEquals(moods.size(),0);
    }

}
