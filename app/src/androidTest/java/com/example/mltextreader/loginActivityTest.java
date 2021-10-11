package com.example.mltextreader;

import android.app.Activity;
import android.app.Instrumentation;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

//good test

public class loginActivityTest {

    @Rule
    public ActivityTestRule<loginActivity> lActivityTestRule = new ActivityTestRule<loginActivity>(loginActivity.class);

    private String email = "user@email.com";
    private String password = "Password1@";

    private loginActivity lActivity = null;

    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(MainMenuActivity.class.getName(),null,false);



    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void testEmailInputLogin()
    {
        // input valid email in the edit text field testing UI input
        onView(withId(R.id.editText_Log_email)).perform(typeText(email));
        // close soft keyboard
        Espresso.closeSoftKeyboard();
    }

    @Test
    public void testPasswordInputLogin()
    {
        // input valid email in the edit text field testing UI input
        onView(withId(R.id.editText_Log_password)).perform(typeText(password));
        // close soft keyboard
        Espresso.closeSoftKeyboard();
    }

    @Test
    public void testEmailAndPasswordInputLogin()
    {
        // input valid email in the edit text field testing UI input
        onView(withId(R.id.editText_Log_email)).perform(typeText(email));
        // close soft keyboard
        Espresso.closeSoftKeyboard();
        // input valid email in the edit text field testing UI input
        onView(withId(R.id.editText_Log_password)).perform(typeText(password));
        // close soft keyboard
        Espresso.closeSoftKeyboard();
        // perform button click
        onView(withId(R.id.button_Login)).perform(click());

    }

    @Test
    public void testOpenMainMenuButtonClick()
    {
       assertNotNull((R.id.button_Login));

       onView(withId(R.id.button_Login)).perform(click());

       Activity MainMenuActivity = getInstrumentation().waitForMonitorWithTimeout(monitor,5000);

    }



}