package com.decagonhq.clads.ui.client

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
class AddAddressFragmentTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        hiltRule.inject()
        launchFragmentInHiltContainer<AddAddressFragment>(fragmentArgs = Bundle()) {}
    }

    @Test
    fun verify_add_state_address_displayed() {
        Espresso.onView(ViewMatchers.withId(R.id.add_address_fragment_state_address_edit_text_view)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }

    @Test
    fun verify_all_views_are_displayed() {
        Espresso.onView(ViewMatchers.withId(R.id.client_add_address_fragment_root_layout)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }

    @Test
    fun verify_add_lga_address_displayed() {
        Espresso.onView(ViewMatchers.withId(R.id.add_address_fragment_lga_address_edit_text_view)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }

    @Test
    fun verify_edit_address_edit_text_is_displayed() {
        Espresso.onView(ViewMatchers.withId(R.id.add_address_fragment_enter_delivery_address_edit_text)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }
}
