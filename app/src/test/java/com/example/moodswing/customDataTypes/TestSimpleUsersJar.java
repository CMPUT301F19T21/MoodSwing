package com.example.moodswing.customDataTypes;

import com.google.firebase.firestore.auth.User;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestSimpleUsersJar {



    @Test
    public void testAddUserJars() {
        ArrayList<UserJar> userJars = new ArrayList<>();
        SimpleUserJarAdapter jarAdapter = new SimpleUserJarAdapter( userJars,1);
        //same as .size()
        assertEquals(jarAdapter.getItemCount(),0);
        UserJar userjar = new UserJar();
        jarAdapter.addToUserJars(userjar);
        assertEquals(jarAdapter.getItemCount(),1);

    }

    @Test
    public void testClearUserJars() {
        ArrayList<UserJar> userJars = new ArrayList<>();
        SimpleUserJarAdapter jarAdapter = new SimpleUserJarAdapter( userJars,1);
        UserJar userjar = new UserJar();
        jarAdapter.addToUserJars(userjar);
        assertEquals(jarAdapter.getItemCount(),1);
        jarAdapter.clearUserJars();
        assertEquals(jarAdapter.getItemCount(),0);
    }
}
