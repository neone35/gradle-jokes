package com.udacity.gradle.builditbigger.free;

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
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.orhanobut.logger.Logger;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.asynctasks.JokeTask;
import com.udacity.gradle.builditbigger.asynctasks.OnAsyncEventListener;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private View rootView;
    private InterstitialAd mInterstitialAd;
    private Context mCtx;
    private ProgressBar mCpdProgress;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mCtx = rootView.getContext();
        mCpdProgress = rootView.findViewById(R.id.pb_cpd_holder);

        initAds();
        listenForJokeAndAdCloseClick();
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
        mInterstitialAd = new InterstitialAd(mCtx);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
    }

    public void listenForJokeAndAdCloseClick() {
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
                    mCpdProgress.setVisibility(View.VISIBLE);
                    startJokeTask();
                }
            });
        });
    }

    private void startJokeTask() {
        // fetch joke and start jokeApp module MainActivity
        JokeTask jokeTask = new JokeTask(new OnAsyncEventListener<String>() {
            @Override
            public void onSuccess(String data) {
                mCpdProgress.setVisibility(View.INVISIBLE);
                startJokeAppModule(data, mCtx, getActivity());
            }

            @Override
            public void onFailure(Exception e) {
                mCpdProgress.setVisibility(View.INVISIBLE);
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
