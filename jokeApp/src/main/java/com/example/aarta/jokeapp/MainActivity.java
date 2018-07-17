package com.example.aarta.jokeapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_JOKE = "joke";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_jokeapp);

        Bundle appExtrasBundle = getIntent().getExtras();
        if (appExtrasBundle != null) {
            String joke = appExtrasBundle.getString(KEY_JOKE);
            TextView tvJokeApp = findViewById(R.id.tv_jokeapp_joke);
            tvJokeApp.setText(joke);
        }
    }
}
