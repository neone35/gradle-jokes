package com.example.aarta.jokelib;

import java.util.Random;

import javax.swing.text.View;

public class JokeProvider {

    public static String[] jokes =
            {"Gradle makes methods out of properties",
                    "An apple a day keeps doctor away",
                    "Seems like apple has bite by Android"};
    private static String joke = null;

    public static String getJokeString() {
        int rnd = new Random().nextInt(jokes.length);
        joke = jokes[rnd];
        return joke;
    }

    public void setJokeString(String data) {
        joke = data;
    }
}
