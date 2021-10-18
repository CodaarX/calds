package com.decagonhq.clads.ui.media

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
class MediaFragmentTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        hiltRule.inject()
        launchFragmentInHiltContainer<MediaFragment>(fragmentArgs = Bundle()) {}
    }

    @Test
    fun test_media_fragment_is_in_view() {
        Espresso.onView(ViewMatchers.withId(R.id.media_fragment_photo_icon_image_View)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }

    @Test
    fun test_images_are_not_in_view_when_recycler_list_is_empty() {
        Espresso.onView(ViewMatchers.withId(R.id.media_fragment_you_have_no_photo_in_gallery_text_view)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }
}
