package com.udacity.gradle.builditbigger.paid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.aarta.jokeapp.MainActivity;
import com.orhanobut.logger.Logger;
import com.udacity.gradle.builditbigger.asynctasks.JokeTask;
import com.udacity.gradle.builditbigger.asynctasks.OnAsyncEventListener;
import com.udacity.gradle.builditbigger.R;



/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private View rootView;
    private Context mCtx;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mCtx = getContext();
        listenForJokeTellClick();
        return rootView;
    }

    public void listenForJokeTellClick() {
        Button btnTellJoke = rootView.findViewById(R.id.btn_tell_joke);
        btnTellJoke.setOnClickListener(v -> {
            startJokeTask();
        });
    }

    private void startJokeTask() {
        // fetch joke and start jokeApp module MainActivity
        JokeTask jokeTask = new JokeTask(new OnAsyncEventListener<String>() {
            @Override
            public void onSuccess(String data) {
                startJokeAppModule(data, mCtx, getActivity());
            }

            @Override
            public void onFailure(Exception e) {
                Logger.d(e.getMessage());
                Toast.makeText(mCtx, "Joke server unavailable", Toast.LENGTH_SHORT).show();
            }
        });
        jokeTask.execute(null, null, null);
    }

    private void startJokeAppModule(String data, Context ctx, Activity parentActivity) {
        if (data != null) {
            Intent jokeAppIntent = new Intent(ctx, com.example.aarta.jokeapp.MainActivity.class);
            Bundle jokeAppBundle = new Bundle();
            jokeAppBundle.putString(MainActivity.KEY_JOKE, data);
            jokeAppIntent.putExtras(jokeAppBundle);
            parentActivity.startActivity(jokeAppIntent);
        } else {
            Toast.makeText(ctx, "Joke data not received", Toast.LENGTH_SHORT).show();
        }
    }
}
