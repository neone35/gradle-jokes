package com.example.aarta.jokelib;

import java.util.Random;

import javax.swing.text.View;

public class JokeProvider {

    private static String joke;
    public static String[] jokes =
            {"Gradle makes methods out of properties",
                    "An apple a day keeps doctor away"};

    public static String getJoke() {
        int rnd = new Random().nextInt(jokes.length);
        joke = jokes[rnd];
        return joke;
    }

    public void setJoke(String data) {
        joke = data;
    }
}
