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
class ClientFragmentTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        hiltRule.inject()
        launchFragmentInHiltContainer<ClientFragment>(fragmentArgs = Bundle()) {}
    }


    @Test
    fun test_client_fragment_recycler_view_visibility() {
        Espresso.onView(ViewMatchers.withId(R.id.client_list_screen_recycler_view)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }

    @Test
    fun test_fab_visibility() {
        Espresso.onView(ViewMatchers.withId(R.id.client_list_screen_recycler_view)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }

    @Test
    fun test_client_root_layout_visibility() {
        Espresso.onView(ViewMatchers.withId(R.id.client_fragment_root_layout)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }


}