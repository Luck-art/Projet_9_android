package com.openclassrooms.realestatemanager.estate_manager


import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.isInternal
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.openclassrooms.realestatemanager.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import android.net.Uri
import androidx.test.rule.GrantPermissionRule
import org.junit.After
import org.junit.Before


@LargeTest
@RunWith(AndroidJUnit4::class)
class AddEstateAndroidTest {

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
    fun addEstateAndroidTest() {
        val appCompatImageView = onView(
            allOf(
                withId(R.id.add_estate),
                childAtPosition(
                    allOf(
                        withId(R.id.my_toolbar),
                        childAtPosition(
                            withClassName(`is`("android.widget.LinearLayout")),
                            0
                        )
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        appCompatImageView.perform(click())


        val resultData = Intent().apply {
            val imageUri: Uri = Uri.parse("android.resource://com.openclassrooms.realestatemanager/res/drawable/estate_for_add_test.jpeg")
            data = imageUri
        }
        intending(not(isInternal())).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, resultData))


        val frameLayout = onView(
            allOf(
                withId(R.id.photoContainer),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    0
                )
            )
        )
        frameLayout.perform(scrollTo(), click())

        val materialCheckBox = onView(
            allOf(
                withId(R.id.checkBoxApartment), withText("Apartment"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.checkBoxGroupTypeEstate),
                        0
                    ),
                    2
                )
            )
        )
        materialCheckBox.perform(scrollTo(), click())

        val appCompatEditText = onView(
            allOf(
                withId(R.id.editName),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    4
                )
            )
        )
        appCompatEditText.perform(
            scrollTo(),
            replaceText("colorful apartment "),
            closeSoftKeyboard()
        )

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.editDescription),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    5
                )
            )
        )
        appCompatEditText2.perform(scrollTo(), replaceText("coloful"), closeSoftKeyboard())

        val appCompatEditText3 = onView(
            allOf(
                withId(R.id.editAddress),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    6
                )
            )
        )
        appCompatEditText3.perform(
            scrollTo(),
            replaceText("Av. Andr� Beauduc, 06100 Nice"),
            closeSoftKeyboard()
        )

        val appCompatEditText4 = onView(
            allOf(
                withId(R.id.editPrice),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    7
                )
            )
        )
        appCompatEditText4.perform(scrollTo(), replaceText("50000"), closeSoftKeyboard())

        val appCompatEditText5 = onView(
            allOf(
                withId(R.id.editSurface),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    8
                )
            )
        )
        appCompatEditText5.perform(scrollTo(), replaceText("650"), closeSoftKeyboard())

        val appCompatEditText6 = onView(
            allOf(
                withId(R.id.editSurface), withText("650"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    8
                )
            )
        )
        appCompatEditText6.perform(pressImeActionButton())

        val appCompatEditText7 = onView(
            allOf(
                withId(R.id.editRooms),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    9
                )
            )
        )
        appCompatEditText7.perform(scrollTo(), replaceText("8"), closeSoftKeyboard())

        val appCompatEditText8 = onView(
            allOf(
                withId(R.id.estateAgent),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    10
                )
            )
        )
        appCompatEditText8.perform(scrollTo(), replaceText("Lucas"), closeSoftKeyboard())



        val materialCheckBox2 = onView(allOf(
            withId(R.id.checkBoxSchool), withText("School"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.checkBoxGroupPointsOfInterest),
                    0
                ),
                0
            )
        ))
        materialCheckBox2.perform(scrollTo(), click())



        val materialCheckBox3 = onView(
            allOf(
                withId(R.id.checkBoxShops), withText("Shop"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.checkBoxGroupPointsOfInterest),
                        0
                    ),
                    1
                )
            )
        )
        materialCheckBox3.perform(scrollTo(), click())

        val materialCheckBox4 = onView(
            allOf(
                withId(R.id.checkBoxRestaurants), withText("Restaurant"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.checkBoxGroupPointsOfInterest),
                        0
                    ),
                    2
                )
            )
        )
        materialCheckBox4.perform(scrollTo(), click())

        val materialCheckBox5 = onView(
            allOf(
                withId(R.id.checkBoxGym), withText("Gymnast"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.checkBoxGroupPointsOfInterest),
                        1
                    ),
                    1
                )
            )
        )
        materialCheckBox5.perform(scrollTo(), click())

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

        val appCompatEditText9 = onView(
            allOf(
                withId(R.id.editTextSaleDate),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    15
                )
            )
        )
        appCompatEditText9.perform(scrollTo(), click())

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
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    17
                )
            )
        )
        materialButton2.perform(scrollTo(), click())

        onView(withId(R.id.realEstateRecyclerView))
            .check(matches(hasDescendant(allOf(withId(R.id.estate_name), withText("colorful apartment ")))))

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

