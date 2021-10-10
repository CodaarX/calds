package com.decagonhq.clads.ui.profile.editprofile

import android.os.Bundle
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
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
class EditProfileFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        hiltRule.inject()
        launchFragmentInHiltContainer<EditProfileFragment>(fragmentArgs = Bundle()) {}
    }

    @Test
    fun test_profile_fragment_is_in_view() {
        Espresso.onView(ViewMatchers.withId(R.id.edit_profile_fragment_root_layout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    // test that fragment is displayed
    @Test
    fun test_viewPagers_are_in_view() {
        Espresso.onView(ViewMatchers.withId(R.id.edit_profile_view_pager)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

//    // ensure tab layout correctly displays its view pager
    @Test
    fun test_tab_layout_displays_correct_view_pager() {
        Espresso.onView(ViewMatchers.withText("Account")).perform(click())
        Espresso.onView(ViewMatchers.withId(R.id.account_fragment_nested_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withText("Specialty")).perform(click())
        Espresso.onView(ViewMatchers.withId(R.id.specialty_fragment_nested_scroll_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.edit_profile_tab_layout)).perform(swipeLeft())
        Espresso.onView(ViewMatchers.withText("Payment Method")).perform(click())
        Espresso.onView(ViewMatchers.withId(R.id.payment_tab_nested_scroll_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.edit_profile_tab_layout)).perform(swipeLeft())
        Espresso.onView(ViewMatchers.withText("Security")).perform(click())
        Espresso.onView(ViewMatchers.withId(R.id.edit_profile_security_fragment)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}
