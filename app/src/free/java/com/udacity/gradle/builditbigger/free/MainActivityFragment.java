package com.udacity.gradle.builditbigger.free;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CircularProgressDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.orhanobut.logger.Logger;
import com.udacity.gradle.builditbigger.AppExecutors;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;
import java.util.Objects;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private AppExecutors appExecutors;
    private MyApi myApiService;
    private View rootView;
    private InterstitialAd mInterstitialAd;
    private Context ctx;
    private ProgressBar mCpdProgress;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        appExecutors = AppExecutors.getInstance();
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ctx = rootView.getContext();
        mCpdProgress = rootView.findViewById(R.id.pb_cpd_holder);

        initAds();
        listenForJokeTellClick();
        return rootView;
    }

    private void initAds() {
        // banner
        AdView mAdView = rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
        // interestial
        mInterstitialAd = new InterstitialAd(ctx);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
    }

    public void listenForJokeTellClick() {
        Button btnTellJoke = rootView.findViewById(R.id.btn_tell_joke);
        btnTellJoke.setOnClickListener(v -> {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    mInterstitialAd.show();
                }

                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    // get joke and start jokeApp module MainActivity
                    fetchAndForwardBtnJoke();
                }
            });
        });
    }

    public void fetchAndForwardBtnJoke() {
        appExecutors.networkIO().execute(() -> {
            // show loading
            Objects.requireNonNull(getActivity()).runOnUiThread(() -> mCpdProgress.setVisibility(View.VISIBLE));
            // get api endpoint service
            if (myApiService == null) {
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        .setRootUrl("http://10.0.2.2:8080/_ah/api/") // this is localhost on emulator
                        .setGoogleClientRequestInitializer(abstractGoogleClientRequest ->
                                abstractGoogleClientRequest.setDisableGZipContent(true));
                myApiService = builder.build();
            }
            try {
                // fetch joke
                String data = myApiService.getMyJoke().execute().getJokeString();
                Intent jokeAppIntent = new Intent(ctx, com.example.aarta.jokeapp.MainActivity.class);
                Bundle jokeAppBundle = new Bundle();
                jokeAppBundle.putString("joke", data);
                jokeAppIntent.putExtras(jokeAppBundle);
                // start jokeApp module to display it
                startActivity(jokeAppIntent);
                // hide loading
                Objects.requireNonNull(getActivity()).runOnUiThread(() -> mCpdProgress.setVisibility(View.INVISIBLE));
            } catch (IOException e) {
                Logger.d(e.getMessage());
                Objects.requireNonNull(getActivity()).runOnUiThread(() ->
                        Toast.makeText(ctx, "Joke server unavailable", Toast.LENGTH_SHORT).show());
            }
        });
    }

}
