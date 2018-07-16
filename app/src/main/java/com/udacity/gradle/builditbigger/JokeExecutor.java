package com.udacity.gradle.builditbigger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.orhanobut.logger.Logger;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;
import java.util.Objects;

public class JokeExecutor {

    private static MyApi myApiService;
    private ProgressBar mCpdProgress;

    public static void fetchAndForwardBtnJoke(Context ctx, Activity parentActivity, ProgressBar loadingBar) {
        AppExecutors appExecutors = AppExecutors.getInstance();
        appExecutors.networkIO().execute(() -> {
            // show loading
            if (loadingBar != null)
                Objects.requireNonNull(parentActivity).runOnUiThread(() -> loadingBar.setVisibility(View.VISIBLE));
            // get api endpoint service
            myApiService = createApiService();
            try {
                // fetch joke
                String data = myApiService.getMyJoke().execute().getJokeString();
                // start module to display it
                startJokeAppModule(data, ctx, parentActivity);
                // hide loading
                if (loadingBar != null)
                    Objects.requireNonNull(parentActivity).runOnUiThread(() -> loadingBar.setVisibility(View.INVISIBLE));
            } catch (IOException e) {
                Logger.d(e.getMessage());
                Objects.requireNonNull(parentActivity).runOnUiThread(() ->
                        Toast.makeText(ctx, "Joke server unavailable", Toast.LENGTH_SHORT).show());
                if (loadingBar != null)
                    Objects.requireNonNull(parentActivity).runOnUiThread(() -> loadingBar.setVisibility(View.INVISIBLE));
            }
        });
    }

    private static void startJokeAppModule(String data, Context ctx, Activity parentActivity) {
        Intent jokeAppIntent = new Intent(ctx, com.example.aarta.jokeapp.MainActivity.class);
        Bundle jokeAppBundle = new Bundle();
        jokeAppBundle.putString("joke", data);
        jokeAppIntent.putExtras(jokeAppBundle);
        parentActivity.startActivity(jokeAppIntent);
    }

    private static MyApi createApiService() {
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
