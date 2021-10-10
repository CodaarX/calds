package com.decagonhq.clads.ui.authentication

import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.decagonhq.clads.R
import com.decagonhq.clads.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class LoginFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        hiltRule.inject()
        launchFragmentInHiltContainer<LoginFragment>(fragmentArgs = Bundle()) {}
    }

    // Test view visibility
    @Test
    fun login_fragment_parent_layout_visibility() {
        onView(withId(R.id.login_fragment_parent_layout)).check(matches(isDisplayed()))
    }

    @Test
    fun login_fragment_email_address_edit_text() {
        onView(withId(R.id.login_fragment_email_address_edit_text)).check(matches(isDisplayed()))
    }

    @Test
    fun login_fragment_password_edit_text() {
        onView(withId(R.id.login_fragment_password_edit_text)).check(matches(isDisplayed()))
    }

    @Test
    fun login_fragment_log_in_card_view() {
        onView(withId(R.id.login_fragment_log_in_button)).check(matches(isDisplayed()))
    }
}
