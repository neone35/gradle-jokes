package com.udacity.gradle.builditbigger.free;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.udacity.gradle.builditbigger.JokeTask;
import com.udacity.gradle.builditbigger.R;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private View rootView;
    private InterstitialAd mInterstitialAd;
    private Context ctx;
    private ProgressBar mCpdProgress;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ctx = rootView.getContext();
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
        mInterstitialAd = new InterstitialAd(ctx);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
    }

    @SuppressWarnings("unchecked")
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
                    // get joke and start jokeApp module MainActivity
                    mCpdProgress.setVisibility(View.VISIBLE);
                    new JokeTask().execute(new Pair<>(getActivity(), mCpdProgress));
                }
            });
        });
    }

}
