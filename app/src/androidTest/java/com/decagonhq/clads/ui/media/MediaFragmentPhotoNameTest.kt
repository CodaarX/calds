package com.decagonhq.clads.ui.media

import android.os.Bundle
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.decagonhq.clads.R
import com.decagonhq.clads.launchFragmentInHiltContainer
import com.decagonhq.clads.ui.client.ClientFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MediaFragmentPhotoNameTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        hiltRule.inject()
        launchFragmentInHiltContainer<MediaFragmentPhotoName>(fragmentArgs = Bundle()) {}
    }


    @Test
    fun test_photo_fragment_visibility() {
        Espresso.onView(ViewMatchers.withId(R.id.photoGalleryImage)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }

    @Test
    fun test_photo_title_view_visibility() {
        Espresso.onView(ViewMatchers.withId(R.id.media_fragment_photo_name_edit_text)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }

    @Test
    fun test_photo_fab_view_visibility() {
        Espresso.onView(ViewMatchers.withId(R.id.media_fragment_photo_name_edit_text)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }

}