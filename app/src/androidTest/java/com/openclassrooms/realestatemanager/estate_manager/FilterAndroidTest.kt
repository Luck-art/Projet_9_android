package com.openclassrooms.realestatemanager.estate_manager


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.google.android.material.slider.RangeSlider
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.estate_manager.logic.SearchFilter
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.`is`
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class FilterAndroidTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(RealEstateManagerActivity::class.java)

    @Test
    fun filterAndroidTest() {

        onView(
            allOf(
                withId(R.id.realEstateRecyclerView),
                isDisplayed()
            )
        ).check(RecyclerViewItemCountAssertion((2)))

        val appCompatImageView = onView(
            allOf(
                withId(R.id.search_estate),
                childAtPosition(
                    allOf(
                        withId(R.id.my_toolbar),
                        childAtPosition(
                            withClassName(`is`("android.widget.LinearLayout")),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatImageView.perform(click())

        onView(
            allOf(
                withId(SearchFilter.PRICE_ID),
            )
        ).perform(setRangeSliderValue(
            maxValue = 20000f
        ))

        waitFor(2_000)

        val materialButton = onView(
            allOf(
                withId(android.R.id.button1), withText("OK"),
                childAtPosition(
                    childAtPosition(
                        withId(androidx.appcompat.R.id.buttonPanel),
                        0
                    ),
                    3
                )
            )
        )
        materialButton.perform(scrollTo(), click())

        waitFor(2_000)

        onView(
            allOf(
                withId(R.id.realEstateRecyclerView),
                isDisplayed()
            )
        ).check(RecyclerViewItemCountAssertion((1)))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }


    class RecyclerViewItemCountAssertion : ViewAssertion {
        private val matcher: Matcher<Int>

        constructor(expectedCount: Int) {
            matcher = `is`(expectedCount)
        }

        constructor(matcher: Matcher<Int>) {
            this.matcher = matcher
        }

        override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
            if (noViewFoundException != null) {
                throw noViewFoundException
            }
            val recyclerView = view as RecyclerView
            val adapter = recyclerView.adapter
            assertThat(adapter!!.itemCount, matcher)
        }
    }


    fun setRangeSliderValue(minValue: Float? = null, maxValue: Float? = null): ViewAction {
        return object : ViewAction {
            override fun getDescription(): String {
                return "Set Slider value to min:$minValue max:$maxValue"
            }

            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isAssignableFrom(RangeSlider::class.java)
            }

            override fun perform(uiController: UiController?, view: View) {
                val seekBar = view as RangeSlider
                val actualValues = seekBar.values
                val newMin = minValue ?: actualValues[0]
                val newMax = maxValue ?: actualValues[1]
                seekBar.values = listOf(newMin, newMax)
            }
        }
    }

    private fun waitFor(duration: Int) {
        onView(isRoot()).perform(waitForAction(duration.toLong()))
    }

    fun waitForAction(millis: Long): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isRoot()
            }

            override fun getDescription(): String {
                return "Wait for $millis milliseconds."
            }

            override fun perform(uiController: UiController, view: View) {
                uiController.loopMainThreadForAtLeast(millis)
            }
        }
    }

}
