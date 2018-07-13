package com.udacity.gradle.builditbigger.backend;

import com.example.aarta.jokelib.JokeProvider;

/** The object model for the data we are sending through endpoints */
public class MyJoke {

    private String mJokeString;

    public String getJoke() {
        mJokeString = JokeProvider.getJoke();
        return mJokeString;
    }

    public void setJoke(String data) {
        mJokeString = data;
    }
}