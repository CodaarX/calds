package com.decagonhq.clads.ui.profile.editprofile

import android.os.Bundle
import androidx.test.espresso.Espresso
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
class SecurityFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        hiltRule.inject()
        launchFragmentInHiltContainer<SecurityFragment>(fragmentArgs = Bundle()) {}
    }

    // test that fragment is displayed
    @Test
    fun test_security_fragment_is_in_view() {
        Espresso.onView(ViewMatchers.withId(R.id.edit_profile_security_fragment)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }

    // test that fragment views are displayed
    @Test
    fun test_security_fragment_save_button_is_in_view() {
        Espresso.onView(ViewMatchers.withId(R.id.security_fragment_save_changes_button)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }

    // test that input field  is displayed
    @Test
    fun test_security_fragment_phone_number_edit_text_is_in_view() {
        Espresso.onView(ViewMatchers.withId(R.id.security_fragment_phone_number_input_text_view)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }
}
