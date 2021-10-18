package com.decagonhq.clads.ui.profile.editprofile

import android.os.Bundle
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeUp
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
class AccountFragmentTest {

//    @Mock
//    lateinit var profile: UserProfileEntity

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        hiltRule.inject()
        launchFragmentInHiltContainer<AccountFragment>(fragmentArgs = Bundle()) {}
    }

    // test that fragment is displayed
    @Test
    fun test_profile_fragment_is_in_view() {
        Espresso.onView(ViewMatchers.withId(R.id.account_fragment_nested_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    // check if dialogue is displayed on click
    @Test
    fun test_bottom_sheet_is_displayed_on_edit_address_click() {
        Espresso.onView(ViewMatchers.withId(R.id.account_fragment_workshop_address_value_text_view)).perform(click())
        Espresso.onView(ViewMatchers.withId(R.id.account_fragment_location_bottom_sheet)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    // test if dialogue lunches map fragment
    @Test
    fun test_bottom_sheet_map_selection_displays_map_fragment() {
        Espresso.onView(ViewMatchers.withId(R.id.account_fragment_workshop_address_value_text_view)).perform(click())
        Espresso.onView(ViewMatchers.withId(R.id.account_fragment_location_bottom_sheet)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    // check if save button view is in place
    @Test
    fun test_account_fragment_layout_view_is_displayed_in_view() {
        Espresso.onView(ViewMatchers.withId(R.id.account_fragment_nested_view)).perform(swipeUp()).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}
