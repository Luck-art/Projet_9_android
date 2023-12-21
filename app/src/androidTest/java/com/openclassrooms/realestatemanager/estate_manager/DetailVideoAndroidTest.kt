package com.openclassrooms.realestatemanager.estate_manager


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.GrantPermissionRule
import com.openclassrooms.realestatemanager.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.`is`
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class DetailVideoAndroidTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(RealEstateManagerActivity::class.java)

    @Rule
    @JvmField
    val readStoragePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.READ_EXTERNAL_STORAGE)

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun detailVideoAndroidTest() {
        val recyclerView = onView(
            allOf(
                withId(R.id.realEstateRecyclerView),
                childAtPosition(
                    withClassName(`is`("android.widget.LinearLayout")),
                    0
                )
            )
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val recyclerView2 = onView(
            allOf(
                withId(R.id.image_list),
                childAtPosition(
                    withClassName(`is`("android.widget.LinearLayout")),
                    9
                )
            )
        )
        recyclerView2.perform(actionOnItemAtPosition<ViewHolder>(2, click()))

        val appCompatImageButton = onView(
            allOf(
                withId(R.id.playButton),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        8
                    ),
                    2
                )
            )
        )
        appCompatImageButton.perform(scrollTo(), click())

        val appCompatImageButton2 = onView(
            allOf(
                withId(R.id.pauseButton),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        8
                    ),
                    3
                )
            )
        )
        appCompatImageButton2.perform(scrollTo(), click())

        val imageButton = onView(
            allOf(
                withId(R.id.playButton),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        imageButton.check(matches(isDisplayed()))

        val textView = onView(
            allOf(
                withId(R.id.estate_address),
                withText("Adresse: 2620 Main St, Santa Monica, CA 90405"),
                withParent(withParent(withId(R.id.scrollView))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Adresse: 2620 Main St, Santa Monica, CA 90405")))
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
}
