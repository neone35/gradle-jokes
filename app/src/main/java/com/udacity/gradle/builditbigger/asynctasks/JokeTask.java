package com.udacity.gradle.builditbigger.asynctasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.widget.ProgressBar;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;

public class JokeTask extends AsyncTask<Void, Void, String> {

    private MyApi myApiService;
    private Exception mException;
    private OnAsyncEventListener<String> mCallBack;

    public JokeTask(OnAsyncEventListener<String> callback) {
        mCallBack = callback;
    }

    @Override
    protected final String doInBackground(Void... voids) {
        myApiService = createApiService();
        try {
            // return joke
            return myApiService.getMyJoke().execute().getJokeString();
        } catch (IOException e) {
            mException = e;
            return null;
        }
    }

    protected void onPostExecute(String data) {
        if (mCallBack != null) {
            if (mException == null) {
                mCallBack.onSuccess(data);
            } else {
                mCallBack.onFailure(mException);
            }
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
