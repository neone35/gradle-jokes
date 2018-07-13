package com.udacity.gradle.builditbigger;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.example.aarta.jokelib.JokeProvider;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.stream.IntStream;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTestRec {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    private String[] exptectedJokes = JokeProvider.jokes;

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    @Test
    public void mainActivityTestRec() {

        // try to click "tell joke" button twice
        IntStream.range(0, 2).forEach($ -> {
            ViewInteraction button = onView(
                    allOf(withId(R.id.btn_tell_joke),
                            childAtPosition(
                                    allOf(withId(R.id.fragment),
                                            childAtPosition(
                                                    withId(android.R.id.content),
                                                    0)),
                                    1),
                            isDisplayed()));
            button.check(matches(isDisplayed()));


            ViewInteraction appCompatButton = onView(
                    allOf(withId(R.id.btn_tell_joke), withText("Tell Joke"),
                            childAtPosition(
                                    allOf(withId(R.id.fragment),
                                            childAtPosition(
                                                    withId(android.R.id.content),
                                                    0)),
                                    1),
                            isDisplayed()));
            appCompatButton.perform(click());
        });

        // check received value
        ViewInteraction textView = onView(
                allOf(withId(R.id.tv_jokeapp_joke),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        // joke is displayed
        textView.check(matches(isDisplayed()));
        // not null
        textView.check(matches(textViewHasValue()));
        // matches one of the possible jokes
        textView.check(matches(textViewMatchesArray(exptectedJokes)));

    }

    private Matcher<View> textViewHasValue() {

        return new TypeSafeMatcher<View>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("The TextView has value");
            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof TextView)) {
                    return false;
                }
                String text;
                text = ((TextView) view).getText().toString();
                return (!TextUtils.isEmpty(text));
            }
        };
    }

    private Matcher<View> textViewMatchesArray(String[] stringArray) {

        return new TypeSafeMatcher<View>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("The TextView has one of array values");
            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof TextView)) {
                    return false;
                }
                String text = ((TextView) view).getText().toString();

                return Arrays.asList(stringArray).contains(text);
            }
        };
    }
}
