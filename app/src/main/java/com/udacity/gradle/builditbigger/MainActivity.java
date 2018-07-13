package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.aarta.jokelib.JokeProvider;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private static MyApi myApiService = null;
    private String dataString = null;
    public static String joke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Logger.addLogAdapter(new AndroidLogAdapter());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void tellJoke(View view) {
        joke = getJokeFromBackend();
        if (joke != null) {
            // start jokeApp module MainActivity
            Intent jokeAppIntent = new Intent(this, com.example.aarta.jokeapp.MainActivity.class);
            Bundle jokeAppBundle = new Bundle();
            jokeAppBundle.putString("joke", joke);
            jokeAppIntent.putExtras(jokeAppBundle);
            startActivity(jokeAppIntent);
        }
    }

    public String getJoke() {
        return joke;
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
