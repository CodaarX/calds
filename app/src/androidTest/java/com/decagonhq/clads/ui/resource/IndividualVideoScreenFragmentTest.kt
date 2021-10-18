package com.decagonhq.clads.ui.resource

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
class IndividualVideoScreenFragmentTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        hiltRule.inject()
        launchFragmentInHiltContainer<IndividualVideoScreenFragment>(fragmentArgs = Bundle()) {}
    }

    @Test
    fun verify_video_screen_fragment_image_player_displayed() {
        Espresso.onView(ViewMatchers.withId(R.id.individual_video_screen_fragment_player_view)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }

    @Test
    fun verify_video_screen_fragment_image_player_title_displayed() {
        Espresso.onView(ViewMatchers.withId(R.id.individual_video_screen_fragment_video_title_text_view)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }

    @Test
    fun verify_video_screen_fragment_rv_displayed() {
        Espresso.onView(ViewMatchers.withId(R.id.individual_video_screen_fragment_recycler_view)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }
}
