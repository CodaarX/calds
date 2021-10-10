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
class PaymentMethodFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        hiltRule.inject()
        launchFragmentInHiltContainer<PaymentMethodFragment>(fragmentArgs = Bundle()) {}
    }

    // check if fragment is displayed
    @Test
    fun payment_fragment_is_displayed() {
        Espresso.onView(ViewMatchers.withId(R.id.payment_tab_nested_scroll_view))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    // check if button is displayed
    @Test
    fun save_payment_button() {
        Espresso.onView(ViewMatchers.withId(R.id.payment_method_fragment_save_changes_button))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}
