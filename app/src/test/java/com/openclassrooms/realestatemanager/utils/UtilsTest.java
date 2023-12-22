package com.openclassrooms.realestatemanager.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowNetworkCapabilities;

import java.util.Calendar;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.Q)
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
        Calendar now = Calendar.getInstance();


        int day = now.get(Calendar.DATE);
        String dayString = "";
        if (day <= 9) {
            dayString = "0" + day;
        } else {
            dayString = "" + day;
        }

        int month = now.get(Calendar.MONTH) + 1;
        String monthString = "";
        if (month <= 9) {
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

    @Test
    public void connected_to_wifi_returns_true() {
        // GIVEN
        Context context = RuntimeEnvironment.application;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);


        NetworkCapabilities capabilities = ShadowNetworkCapabilities.newInstance();
        // fake I'm connected to wifi
        shadowOf(capabilities).addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
        shadowOf(connectivityManager).setNetworkCapabilities(connectivityManager.getActiveNetwork(), capabilities);

        //WHEN
        boolean result = Utils.checkNetworkAvailability(context);

        // THEN

        assertTrue(result);
    }

    @Test
    public void connected_to_cellular_returns_true() {
        // GIVEN
        Context context = RuntimeEnvironment.application;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);


        NetworkCapabilities capabilities = ShadowNetworkCapabilities.newInstance();
        // fake I'm connected to cellular
        shadowOf(capabilities).addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR);
        shadowOf(connectivityManager).setNetworkCapabilities(connectivityManager.getActiveNetwork(), capabilities);

        //WHEN
        boolean result = Utils.checkNetworkAvailability(context);

        // THEN

        assertTrue(result);
    }

    @Test
    public void connected_to_nothing_returns_false() {
        // GIVEN
        Context context = RuntimeEnvironment.application;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);


        NetworkCapabilities capabilities = ShadowNetworkCapabilities.newInstance();
        // fake I'm connected to nothing
        // shadowOf(capabilities).addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR);
        shadowOf(connectivityManager).setNetworkCapabilities(connectivityManager.getActiveNetwork(), capabilities);

        //WHEN
        boolean result = Utils.checkNetworkAvailability(context);

        // THEN

        assertFalse(result);
    }


}