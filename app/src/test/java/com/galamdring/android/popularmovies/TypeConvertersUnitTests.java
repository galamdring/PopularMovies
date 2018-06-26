package com.galamdring.android.popularmovies;

import com.galamdring.android.popularmovies.Data.DataConverter;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TypeConvertersUnitTests {
    @Test
    public void TypeConverterStringToTrailerListNullCheck() {
        List<String> expected = new ArrayList<>();
        assertEquals(expected, DataConverter.stringToTrailerList(null));
    }

    @Test
    public void TypeConvertersStringToTrailerListSingleEntry(){
        ArrayList<String> expected = new ArrayList<String>();
        expected.add("xLPizqfP2n8");
        assertEquals(expected,DataConverter.stringToTrailerList("xLPizqfP2n8"));
    }

    @Test
    public void TypeConvertersStringToTrailerListMultipleEntries(){
        ArrayList<String> expected = new ArrayList<String>();
        expected.add("xLPizqfP2n8");
        expected.add("xLPizqfP2n8");
        assertEquals(expected,DataConverter.stringToTrailerList("xLPizqfP2n8;xLPizqfP2n8"));
    }

    @Test
    public void TypeConvertersTrailersListToStringSingleEntry(){
        String expected = "xLPizqfP2n8";
        ArrayList<String> source = new ArrayList<>();
        source.add("xLPizqfP2n8");
        assertEquals(expected, DataConverter.trailerListToString(source));
    }

    @Test
    public void TypeConvertersTrailersListToStringNullCheck(){
        String expected = "";
        ArrayList<String> source = null;
        assertEquals(expected,DataConverter.trailerListToString(source));
    }

    @Test
    public void TypeConvertersTrailersListToStringMultipleEntries(){
        String expected = "xLPizqfP2n8;xLPizqfP2n8;xLPizqfP2n8";
        ArrayList<String> source = new ArrayList<>();
        source.add("xLPizqfP2n8");
        source.add("xLPizqfP2n8");
        source.add("xLPizqfP2n8");
        assertEquals(expected,DataConverter.trailerListToString(source));
    }
}
