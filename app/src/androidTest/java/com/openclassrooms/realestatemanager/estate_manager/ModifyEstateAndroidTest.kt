package com.openclassrooms.realestatemanager.estate_manager


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
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
import org.hamcrest.Matchers.anything
import org.hamcrest.Matchers.`is`
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class ModifyEstateAndroidTest {

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
    fun modifyEstateAndroidTest() {
        val appCompatImageView = onView(
            allOf(
                withId(R.id.edit_estate),
                childAtPosition(
                    allOf(
                        withId(R.id.my_toolbar),
                        childAtPosition(
                            withClassName(`is`("android.widget.LinearLayout")),
                            0
                        )
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        appCompatImageView.perform(click())

        val materialTextView = onData(anything())
            .inAdapterView(
                allOf(
                    withId(androidx.appcompat.R.id.select_dialog_listview),
                    childAtPosition(
                        withId(androidx.appcompat.R.id.contentPanel),
                        0
                    )
                )
            )
            .atPosition(1)
        materialTextView.perform(click())

        val appCompatEditText = onView(
            allOf(
                withId(R.id.editName), withText("Sweet home"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.scrollForAddEstate),
                        0
                    ),
                    4
                )
            )
        )
        appCompatEditText.perform(scrollTo(), replaceText("Sweet apartment"))

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.editName), withText("Sweet apartment"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.scrollForAddEstate),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        appCompatEditText2.perform(closeSoftKeyboard())

        val appCompatEditText3 = onView(
            allOf(
                withId(R.id.editPrice), withText("15000"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.scrollForAddEstate),
                        0
                    ),
                    7
                )
            )
        )
        appCompatEditText3.perform(scrollTo(), replaceText("18000"))

        val appCompatEditText4 = onView(
            allOf(
                withId(R.id.editPrice), withText("18000"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.scrollForAddEstate),
                        0
                    ),
                    7
                ),
                isDisplayed()
            )
        )
        appCompatEditText4.perform(closeSoftKeyboard())

        val appCompatEditText5 = onView(
            allOf(
                withId(R.id.editSurface), withText("2.5"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.scrollForAddEstate),
                        0
                    ),
                    8
                )
            )
        )
        appCompatEditText5.perform(scrollTo(), replaceText("500"))

        val appCompatEditText6 = onView(
            allOf(
                withId(R.id.editSurface), withText("500"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.scrollForAddEstate),
                        0
                    ),
                    8
                ),
                isDisplayed()
            )
        )
        appCompatEditText6.perform(closeSoftKeyboard())


        val materialRadioButton = onView(
            allOf(
                withId(R.id.radioButtonOnSale), withText("for sale"),
                childAtPosition(
                    allOf(
                        withId(R.id.radioGroupSended),
                        childAtPosition(
                            withClassName(`is`("android.widget.LinearLayout")),
                            14
                        )
                    ),
                    0
                )
            )
        )
        materialRadioButton.perform(scrollTo(), click())

        val appCompatEditText7 = onView(
            allOf(
                withId(R.id.editTextSaleDate),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.scrollForAddEstate),
                        0
                    ),
                    15
                )
            )
        )
        appCompatEditText7.perform(scrollTo(), click())

        val materialButton = onView(
            allOf(
                withId(android.R.id.button1), withText("OK"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    3
                )
            )
        )
        materialButton.perform(scrollTo(), click())

        val materialButton2 = onView(
            allOf(
                withId(R.id.buttonAddEstate), withText("OK"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.scrollForAddEstate),
                        0
                    ),
                    17
                )
            )
        )
        materialButton2.perform(scrollTo(), click())

        val textView = onView(
            allOf(
                withId(R.id.estate_name), withText("Sweet apartment"),
                withParent(withParent(withId(R.id.realEstateRecyclerView))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Sweet apartment")))
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
