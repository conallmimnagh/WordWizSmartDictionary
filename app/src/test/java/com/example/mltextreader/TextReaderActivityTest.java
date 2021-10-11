package com.example.mltextreader;

import org.junit.Test;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TextReaderActivityTest {

    TextReaderActivity textReaderActivity = new TextReaderActivity();


    @Test
    public void validateCorrectTextExtracted() {
        boolean result = textReaderActivity.validateExtractedWord("Onomatopoeia");
        assertThat(result, is(equalTo(true)));
    }

    @Test
    public void validateCorrectTextExtracted5chars() {
        boolean result = textReaderActivity.validateExtractedWord("Hello");
        assertThat(result, is(equalTo(true)));
    }

    @Test
    public void validateIncorrectTextExtractedSpecialChar() {
        boolean result = textReaderActivity.validateExtractedWord("Word@");
        assertThat(result, is(equalTo(false)));
    }

    @Test
    public void validateIncorrectTextExtractedNumber() {
        boolean result = textReaderActivity.validateExtractedWord("Learn100");
        assertThat(result, is(equalTo(false)));
    }

    @Test
    public void validateIncorrectTextExtractedEmpty() {
        boolean result = textReaderActivity.validateExtractedWord("");
        assertThat(result, is(equalTo(false)));
    }

}