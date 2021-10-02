package com.decagonhq.clads.ui.profile.editprofile

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.decagonhq.clads.R
import dagger.hilt.android.AndroidEntryPoint
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@AndroidEntryPoint
@RunWith(AndroidJUnit4ClassRunner::class)
class AccountFragmentTest {

    @Before
    fun setUp() { val scenario = launchFragmentInContainer<AccountFragment>(themeResId = R.style.Base_Theme_MaterialComponents_Light) }

    // test that fragment is displayed
    @Test
    fun test_profile_fragment_is_in_view() {
        Espresso.onView(ViewMatchers.withId(R.id.account_fragment_nested_view)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()))
    }

    // check if dialogue is displayed on click
    @Test
    fun test_bottom_sheet_is_displayed_on_edit_address_click(){
        Espresso.onView(ViewMatchers.withId(R.id.account_fragment_workshop_address_value_text_view))
            .perform(click())
        Espresso.onView(ViewMatchers.withId(R.id.account_fragment_location_bottom_sheet))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    // test if dialogue lunches map fragment
    @Test
    fun test_bottom_sheet_map_selection_displays_map_fragment(){
        Espresso.onView(ViewMatchers.withId(R.id.account_fragment_workshop_address_value_text_view)).perform(
            click()
        )
        Espresso.onView(ViewMatchers.withId(R.id.account_fragment_location_bottom_sheet)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.account_fragment_location_bottom_sheet_set_location_now_radio_button)).perform(
            click()
        )
        Espresso.onView(ViewMatchers.withId(R.id.mapFragment)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    // check if save button view is in place
    @Test
    fun test_account_fragment_layout_view_is_displayed_in_view() {
        Espresso.onView(ViewMatchers.withId(R.id.account_fragment_nested_view)).perform(ViewActions.swipeUp()).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}


