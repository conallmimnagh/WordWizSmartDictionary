package com.example.mltextreader;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RecyclerViewContentCheck {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void recyclerViewContentCheck() {
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.editText_Log_email), isDisplayed()));

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.editText_Log_password),
                        isDisplayed()));

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.button_Login), withText("Login"),
                        isDisplayed()));

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.FavWordsPageOpenBtn), withText("Favourite Words"),
                        isDisplayed()));

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recyclerView),
                        isDisplayed()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.word), withText("quantum"),
                        isDisplayed()));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.wordDef), withText("a discrete quantity of energy proportional in magnitude to the frequency of the radiation it represents."),
                        isDisplayed()));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.wordDef), withText("a discrete quantity of energy proportional in magnitude to the frequency of the radiation it represents."),
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
