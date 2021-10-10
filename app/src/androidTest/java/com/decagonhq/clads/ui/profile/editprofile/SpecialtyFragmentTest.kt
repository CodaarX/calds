package com.decagonhq.clads.ui.profile.editprofile

import android.os.Bundle
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.decagonhq.clads.R
import com.decagonhq.clads.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class SpecialtyFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        hiltRule.inject()
        launchFragmentInHiltContainer<SpecialtyFragment>(fragmentArgs = Bundle()) {}
    }

    // test that fragment is displayed
    @Test
    fun test_specialty_fragment_is_in_view() {
        Espresso.onView(ViewMatchers.withId(R.id.specialty_fragment_nested_scroll_view)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }

    // test that fragment recycler view is displayed
    @Test
    fun test_recycler_view_is_in_view() {
        Espresso.onView(ViewMatchers.withId(R.id.specialty_fragment_recycler_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    // test that fragment  save button is displayed
    @Test
    fun test_add_specialty_save_button_is_in_view() {
        Espresso.onView(ViewMatchers.withId(R.id.specialty_fragment_nested_scroll_view)).perform(ViewActions.swipeUp())
        Espresso.onView(ViewMatchers.withId(R.id.specialty_fragment_save_changes_material_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}
