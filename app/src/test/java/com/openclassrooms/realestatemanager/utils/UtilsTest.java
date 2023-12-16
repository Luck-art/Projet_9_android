package com.openclassrooms.realestatemanager.utils;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

public class UtilsTest {

    @Test
    public void convertDollarToEuro_1234() {
        int dollar = 1234;
        float result = Utils.convertDollarToEuro(dollar);
        assert result == 1002;
    }

    @Test
    public void convertEuroToDollar_1002() {
        int euro = 1002;
        int expectedDollars = (int) Math.round(euro * 0.812);
        int result = Utils.convertEuroToDollar(euro);
        assert result == expectedDollars;
    }


    @Test
    public void convertDollarToEuro_100() {
        int dollar = 100;
        float result = Utils.convertDollarToEuro(dollar);
        assert result == 81.0;
    }

    @Test
    public void getTodayDate() {
        Calendar now =  Calendar.getInstance();


        int day = now.get(Calendar.DATE);
        String dayString = "";
        if(day <= 9) {
            dayString = "0" + day;
        } else {
            dayString = "" + day;
        }

        int month = now.get(Calendar.MONTH) + 1 ;
        String monthString = "";
        if(month <= 9) {
            monthString = "0" + month;
        } else {
            monthString = "" + month;
        }

        String year = "" + now.get(Calendar.YEAR);

        String result = Utils.getTodayDate();
        assertEquals(result, dayString + "/" + monthString + "/" + year);
    }



    @Test
    public void convertEuroToDollar_66() {
        int euro = 81;
        float result = Utils.convertEuroToDollar(euro);
        float expected = 66.0f;
        assertEquals(expected, result, 0.01f);
    }





}