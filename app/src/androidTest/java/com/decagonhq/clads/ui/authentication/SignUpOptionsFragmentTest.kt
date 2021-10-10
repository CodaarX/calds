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
class SignUpOptionsFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        hiltRule.inject()
        launchFragmentInHiltContainer<SignUpOptionsFragment>(fragmentArgs = Bundle()) {}
    }

    @Test
    fun test_sign_up_options_clads_logo_in_view() {
        onView(withId(R.id.sign_up_options_fragment_clads_logo_image_view)).check(
            matches(
                isDisplayed()
            )
        )
    }

    @Test
    fun test_sign_up_options_fragment_clads_sign_up_with_google_button_in_view() {
        onView(withId(R.id.sign_up_options_fragment_clads_sign_up_with_google_button))
            .check(matches(isDisplayed()))
    }

    @Test
    fun test_sign_up_options_fragment_login_text_view_in_view() {
        onView(withId(R.id.sign_up_options_fragment_login_text_view))
            .check(matches(isDisplayed()))
    }

//    /*Test Navigation to Profile Activity*/
//    @ExperimentalCoroutinesApi
//    @Test
//    fun test_signup_options_navigation_to_email_signup_fragment() {
//        // Create a TestNavHostController
//        val navController = mock(NavController::class.java)
//        launchFragmentInHiltContainer<SignUpOptionsFragment> {
//            this.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
//                if (viewLifecycleOwner != null) {
//                    // The fragment’s view has just been created
//                    Navigation.setViewNavController(this.requireView(), navController)
//                }
//            }
//        }
//
//
//        /* Verify that performing a click changes the NavController’s state*/
//        onView(withId(R.id.sign_up_options_fragment_sign_up_with_email_button)).perform(click())
//        verify(navController).navigate(R.id.email_sign_up_fragment)
//    }
}
