package com.decagonhq.clads.ui.resource

import android.os.Bundle
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.decagonhq.clads.R
import com.decagonhq.clads.launchFragmentInHiltContainer
import com.decagonhq.clads.ui.transactionhistory.TransactionHistory
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class ResourceGeneralFragmentTest {
        @get:Rule
            var hiltRule = HiltAndroidRule(this)

            @ExperimentalCoroutinesApi
            @Before
            fun setUp() {
                hiltRule.inject()
                launchFragmentInHiltContainer<ResourceGeneralFragment>(fragmentArgs = Bundle()) {}
            }

            @Test
            fun verify_resource_general_fragment_videos_rv_displayed(){
                Espresso.onView(ViewMatchers.withId(R.id.resources_general_fragment_videos_recycler_view)).check(
                    ViewAssertions.matches(
                        ViewMatchers.isDisplayed()
                    )
                )
            }

    @Test
    fun verify_article_publication_title_displayed(){
        Espresso.onView(ViewMatchers.withId(R.id.resources_general_fragment_article_publication_text_view)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }


    @Test
    fun verify_article_publication_rv_displayed(){
        Espresso.onView(ViewMatchers.withId(R.id.resources_general_fragment_image_article_publication_recycler_view)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }

}