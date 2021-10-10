package com.decagonhq.clads.ui.profile.editprofile

import android.os.Bundle
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import com.decagonhq.clads.R
import com.decagonhq.clads.launchFragmentInHiltContainer
import com.decagonhq.clads.ui.profile.HomeFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class HomeFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        hiltRule.inject()
        launchFragmentInHiltContainer<HomeFragment>(fragmentArgs = Bundle()) {}
    }

    @Test
    fun test_dashboard_activity_visibility() {
        Espresso.onView(ViewMatchers.withId(R.id.home_fragment_root_layout)).check(matches(ViewMatchers.isDisplayed()))
    }
}
