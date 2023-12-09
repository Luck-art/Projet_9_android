package com.luck_art.realestatemanager;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static com.openclassrooms.realestatemanager.utils.Utils.isInternetAvailable;

import android.view.View;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.openclassrooms.realestatemanager.LogInActivity;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest {

    @Rule
    public ActivityScenarioRule<LogInActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(LogInActivity.class);

    @Test
    public void internet_is_available() {
        // turn on the wifi
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc wifi enable");

        //wait 5s to connect
        waitFor(5_000);

        boolean isInternetAvailable = isInternetAvailable(ApplicationProvider.getApplicationContext());
        assert isInternetAvailable == true;
    }

    @Test
    public void internet_not_available() {
        // turn down the wifi
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc wifi disable");

        //wait 5s to disconnect
        waitFor(5_000);

        boolean isInternetAvailable = isInternetAvailable(ApplicationProvider.getApplicationContext());
        assert isInternetAvailable == false;
    }

    private void waitFor(int duration) {
        onView(isRoot()).perform(waitForAction(duration));
    }

    public static ViewAction waitForAction(final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "Wait for " + millis + " milliseconds.";
            }

            @Override
            public void perform(UiController uiController, final View view) {
                uiController.loopMainThreadForAtLeast(millis);
            }
        };
    }

}