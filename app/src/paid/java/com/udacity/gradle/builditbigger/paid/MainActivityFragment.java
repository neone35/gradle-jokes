package com.udacity.gradle.builditbigger.paid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.udacity.gradle.builditbigger.JokeTask;
import com.udacity.gradle.builditbigger.R;



/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private View rootView;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        listenForJokeTellClick();
        return rootView;
    }

    @SuppressWarnings("unchecked")
    public void listenForJokeTellClick() {
        Button btnTellJoke = rootView.findViewById(R.id.btn_tell_joke);
        btnTellJoke.setOnClickListener(v -> {
            // fetch joke and start jokeApp module MainActivity
            new JokeTask().execute(new Pair<>(getActivity(), null));
        });
    }
}
