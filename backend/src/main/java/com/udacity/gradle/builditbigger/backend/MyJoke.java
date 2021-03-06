package com.udacity.gradle.builditbigger.backend;

import com.example.aarta.jokelib.JokeProvider;

/**
 * The object model for the library data we are sending through endpoints
 */
public class MyJoke {

    private String mJokeString;

    public String getJokeString() {
        mJokeString = JokeProvider.getJokeString();
        return mJokeString;
    }

    public void setJokeString(String data) {
        mJokeString = data;
    }
}