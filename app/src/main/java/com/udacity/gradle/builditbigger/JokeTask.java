package com.udacity.gradle.builditbigger;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.aarta.jokeapp.MainActivity;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.orhanobut.logger.Logger;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;
import java.util.Objects;

@SuppressLint("StaticFieldLeak")
public class JokeTask extends AsyncTask<Pair<Activity, ProgressBar>, Void, String> {

    private MyApi myApiService;
    private Context ctx;
    private Activity parentActivity;
    private ProgressBar loadingBar;


    @SafeVarargs
    @Override
    protected final String doInBackground(Pair<Activity, ProgressBar>... pairs) {
        ctx = Objects.requireNonNull(pairs[0].first).getApplicationContext();
        parentActivity = pairs[0].first;
        loadingBar = pairs[0].second;
        myApiService = createApiService();
        try {
            // return joke
            return myApiService.getMyJoke().execute().getJokeString();
        } catch (IOException e) {
            Logger.d(e.getMessage());
            Toast.makeText(ctx, "Joke server unavailable", Toast.LENGTH_SHORT).show();
            hideLoadingBar(loadingBar);
            return null;
        }
    }

    @Override
    protected void onPostExecute(String data) {
        hideLoadingBar(loadingBar);
        startJokeAppModule(data, ctx, parentActivity);
    }

    private void hideLoadingBar(ProgressBar loadingBar) {
        if (loadingBar != null) {
            loadingBar.setVisibility(View.INVISIBLE);
        }
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

    private MyApi createApiService() {
        if (myApiService == null) {
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    .setRootUrl("http://10.0.2.2:8080/_ah/api/") // this is localhost on emulator
                    .setGoogleClientRequestInitializer(abstractGoogleClientRequest ->
                            abstractGoogleClientRequest.setDisableGZipContent(true));
            myApiService = builder.build();
        }
        return myApiService;
    }
}
