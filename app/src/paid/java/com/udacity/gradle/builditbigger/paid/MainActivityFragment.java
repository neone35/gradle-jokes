package com.udacity.gradle.builditbigger.paid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.orhanobut.logger.Logger;
import com.udacity.gradle.builditbigger.AppExecutors;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public static String joke;
    private static MyApi myApiService = null;
    private View rootView;
    private String dataString = null;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        listenForJokeTellClick();
        return rootView;
    }

    public void listenForJokeTellClick() {
        Button btnTellJoke = rootView.findViewById(R.id.btn_tell_joke);
        Context ctx = getActivity();
        btnTellJoke.setOnClickListener(v -> {
            joke = getJokeFromBackend();
            if (joke != null) {
                // start jokeApp module MainActivity
                Intent jokeAppIntent = new Intent(ctx, com.example.aarta.jokeapp.MainActivity.class);
                Bundle jokeAppBundle = new Bundle();
                jokeAppBundle.putString("joke", joke);
                jokeAppIntent.putExtras(jokeAppBundle);
                startActivity(jokeAppIntent);
            }
        });
    }

    public String getJokeFromBackend() {
        AppExecutors executors = AppExecutors.getInstance();
        executors.networkIO().execute(() -> {
            if (myApiService == null) {
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        .setRootUrl("http://10.0.2.2:8080/_ah/api/") // this is localhost on emulator
                        .setGoogleClientRequestInitializer(abstractGoogleClientRequest ->
                                abstractGoogleClientRequest.setDisableGZipContent(true));
                myApiService = builder.build();
            }
            try {
                dataString = myApiService.getMyJoke().execute().getJoke();
            } catch (IOException e) {
                Logger.d(e.getMessage());
            }
        });
        return dataString;
    }
}
