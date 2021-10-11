package com.example.mltextreader;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class FavouriteWordsActivtyAndSpeechDictionaryAcitvity {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void favouriteWordsActivtyAndSpeechDictionaryAcitvity() {

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.editText_Log_password),
                        isDisplayed()));

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.button_Login), withText("Login"),
                        isDisplayed()));

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.FavWordsPageOpenBtn), withText("Favourite Words"),
                        isDisplayed()));


        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.SpeechPageOpenBtn), withText("Speech Dictionary"),
                        isDisplayed()));

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.btnSpeak),
                        isDisplayed()));
    }

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
}
