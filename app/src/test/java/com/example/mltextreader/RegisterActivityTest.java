package com.example.mltextreader;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RegisterActivityTest {

    RegisterActivity registerActivity = new RegisterActivity();


    //Testing email validation method

    @Test
    public void validateEmail_CorrectEmailSimple() {
        boolean result = registerActivity.validateEmail("user@email.com");
        assertThat(result, is(equalTo(true)));
    }

    @Test
    public void validateEmail_CorrectwithNumbers() {
        boolean result = registerActivity.validateEmail("user1000@email.com");
        assertThat(result, is(equalTo(true)));
    }

    @Test
    public void validateEmail_falseToomanyAts() {
        boolean result = registerActivity.validateEmail("user1000@@@@@@email.com");
        assertThat(result, is(equalTo(false)));
    }

    @Test
    public void validateEmail_IncorrectNoDot() {
        boolean result = registerActivity.validateEmail("user@emailcom");
        assertThat(result, is(equalTo(false)));
    }

    @Test
    public void validateEmail_IncorrectNoAt() {
        boolean result = registerActivity.validateEmail("useremail.com");
        assertThat(result, is(equalTo(false)));
    }

    @Test
    public void validateEmail_IncorrectNoAtAndNoDot() {
        boolean result = registerActivity.validateEmail("useremailcom");
        assertThat(result, is(equalTo(false)));
    }

    @Test
    public void validateEmail_IncorrectEmpty() {
        boolean result = registerActivity.validateEmail("");
        assertThat(result, is(equalTo(false)));
    }

    //Testing password validation method

    @Test
    public void validateCorrectPassword() {
        boolean result = registerActivity.validatePassword("Password100@");
        assertThat(result, is(equalTo(true)));
    }

    @Test
    public void validateCorrectPassword5chars() {
        boolean result = registerActivity.validatePassword("P100@");
        assertThat(result, is(equalTo(true)));
    }

    @Test
    public void validateIncorrectPassword4chars() {
        boolean result = registerActivity.validatePassword("P10@");
        assertThat(result, is(equalTo(false)));
    }

    @Test
    public void validateIncorrectPasswordNoCapital() {
        boolean result = registerActivity.validatePassword("password100@");
        assertThat(result, is(equalTo(false)));
    }

    @Test
    public void validateIncorrectPasswordNoNumber() {
        boolean result = registerActivity.validatePassword("Password@");
        assertThat(result, is(equalTo(false)));
    }

    @Test
    public void validateIncorrectPasswordNoSpecialChar() {
        boolean result = registerActivity.validatePassword("Password100");
        assertThat(result, is(equalTo(false)));
    }

    @Test
    public void validateIncorrectPasswordEmpty() {
        boolean result = registerActivity.validatePassword("");
        assertThat(result, is(equalTo(false)));
    }

}