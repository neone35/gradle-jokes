package com.udacity.gradle.builditbigger.paid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.orhanobut.logger.Logger;
import com.udacity.gradle.builditbigger.AppExecutors;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;
import com.udacity.gradle.builditbigger.backend.myApi.model.MyJoke;

import java.io.IOException;
import java.util.Objects;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private AppExecutors appExecutors;
    private MyApi myApiService;
    private View rootView;
    private Context ctx;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        appExecutors = AppExecutors.getInstance();
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ctx = rootView.getContext();

        listenForJokeTellClick();
        return rootView;
    }

    public void listenForJokeTellClick() {
        Button btnTellJoke = rootView.findViewById(R.id.btn_tell_joke);
        btnTellJoke.setOnClickListener(v -> {
            // fetch joke and start jokeApp module MainActivity
            fetchAndForwardBtnJoke();
        });
    }

    public void fetchAndForwardBtnJoke() {
        appExecutors.networkIO().execute(() -> {
            if (myApiService == null) {
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        .setRootUrl("http://10.0.2.2:8080/_ah/api/") // this is localhost on emulator
                        .setGoogleClientRequestInitializer(abstractGoogleClientRequest ->
                                abstractGoogleClientRequest.setDisableGZipContent(true));
                myApiService = builder.build();
            }
            try {
                String data = myApiService.getMyJoke().execute().getJokeString();
                Intent jokeAppIntent = new Intent(ctx, com.example.aarta.jokeapp.MainActivity.class);
                Bundle jokeAppBundle = new Bundle();
                jokeAppBundle.putString("joke", data);
                jokeAppIntent.putExtras(jokeAppBundle);
                startActivity(jokeAppIntent);
            } catch (IOException e) {
                Logger.d(e.getMessage());
                Objects.requireNonNull(getActivity()).runOnUiThread(() ->
                        Toast.makeText(ctx, "Joke server unavailable", Toast.LENGTH_SHORT).show());
            }
        });
    }
}
