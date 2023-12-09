package com.openclassrooms.realestatemanager.utils;

import static org.junit.Assert.*;

import org.junit.Test;

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
        float result = Utils.convertEuroToDollar(euro);
        assert result == 1234;
    }

    @Test
    public void convertDollarToEuro_100() {
        int dollar = 100;
        float result = Utils.convertDollarToEuro(dollar);
        assert result == 81.0;
    }



    @Test
    public void convertEuroToDollar_81() {
        int euro = 81;
        float result = Utils.convertEuroToDollar(euro);
        assert result == 100;
    }
}