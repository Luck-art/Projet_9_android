package com.openclassrooms.realestatemanager.estate_manager


import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.net.toUri
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.isInternal
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
import org.hamcrest.Matchers.not
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File


@LargeTest
@RunWith(AndroidJUnit4::class)
class AddEstateAndroidTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(RealEstateManagerActivity::class.java)

    @Rule
    @JvmField
    val readStoragePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.READ_EXTERNAL_STORAGE)

    @Test
    fun addEstateAndroidTest() {
        Intents.init()

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

        val resultData = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            data = Uri.parse("https://www.picsum.photos/300/300")
        }
        intending(not(isInternal())).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK,
                resultData
            )
        )
        val frameLayout = onView(
            allOf(
                withId(R.id.photoContainer),
                childAtPosition(
                    childAtPosition(
                        withId(androidx.appcompat.R.id.custom),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        frameLayout.perform(click())


        Thread.sleep(3000)

        // check if it has a photo
        onView(withId(R.id.selectedPhoto)).check(matches(hasDrawable()))

        val name = "church ${System.currentTimeMillis()}"

        val appCompatEditText = onView(
            allOf(
                withId(R.id.editName),
                childAtPosition(
                    childAtPosition(
                        withId(androidx.appcompat.R.id.custom),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText.perform(replaceText(name), closeSoftKeyboard())

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.editDescription),
                childAtPosition(
                    childAtPosition(
                        withId(androidx.appcompat.R.id.custom),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatEditText2.perform(replaceText("italia church "), closeSoftKeyboard())

        val appCompatEditText3 = onView(
            allOf(
                withId(R.id.editAddress),
                childAtPosition(
                    childAtPosition(
                        withId(androidx.appcompat.R.id.custom),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatEditText3.perform(replaceText("23 av de l'église, Rome"), closeSoftKeyboard())

        val appCompatEditText4 = onView(
            allOf(
                withId(R.id.editPrice),
                childAtPosition(
                    childAtPosition(
                        withId(androidx.appcompat.R.id.custom),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        appCompatEditText4.perform(replaceText("30000"), closeSoftKeyboard())

        val appCompatEditText5 = onView(
            allOf(
                withId(R.id.editPrice), withText("30000"),
                childAtPosition(
                    childAtPosition(
                        withId(androidx.appcompat.R.id.custom),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        appCompatEditText5.perform(pressImeActionButton())

        val appCompatEditText6 = onView(
            allOf(
                withId(R.id.editSurface),
                childAtPosition(
                    childAtPosition(
                        withId(androidx.appcompat.R.id.custom),
                        0
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        appCompatEditText6.perform(replaceText("250"), closeSoftKeyboard())

        val appCompatEditText7 = onView(
            allOf(
                withId(R.id.editSurface), withText("250"),
                childAtPosition(
                    childAtPosition(
                        withId(androidx.appcompat.R.id.custom),
                        0
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        appCompatEditText7.perform(pressImeActionButton())

        val appCompatEditText8 = onView(
            allOf(
                withId(R.id.editRooms),
                childAtPosition(
                    childAtPosition(
                        withId(androidx.appcompat.R.id.custom),
                        0
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        appCompatEditText8.perform(replaceText("8"), closeSoftKeyboard())

        val appCompatEditText9 = onView(
            allOf(
                withId(R.id.editRooms), withText("8"),
                childAtPosition(
                    childAtPosition(
                        withId(androidx.appcompat.R.id.custom),
                        0
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        appCompatEditText9.perform(pressImeActionButton())

        val materialRadioButton = onView(
            allOf(
                withId(R.id.radioButtonOnSale), withText("for sale"),
                childAtPosition(
                    allOf(
                        withId(R.id.radioGroupSended),
                        childAtPosition(
                            withClassName(`is`("android.widget.LinearLayout")),
                            7
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialRadioButton.perform(click())

        val materialButton = onView(
            allOf(
                withId(R.id.buttonAddEstate), withText("OK"),
                childAtPosition(
                    childAtPosition(
                        withId(androidx.appcompat.R.id.custom),
                        0
                    ),
                    8
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())

        //final check - the name should be in the list
        onView(
            allOf(
                withText(name),
            )
        ).check(matches(isDisplayed()))
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

    fun hasDrawable(): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("ImageView or AppCompatImageView has a drawable")
            }

            override fun matchesSafely(view: View): Boolean {
                if (view is ImageView) {
                    return view.drawable != null
                } else if (view is AppCompatImageView) {
                    return view.drawable != null
                }
                return false
            }
        }
    }



    fun matchesSafely(view: View): Boolean {
            return view is ImageView && view.drawable != null
        }
    }

