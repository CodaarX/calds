package com.decagonhq.clads.ui.authentication

import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeUp
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
class ForgotPasswordFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        hiltRule.inject()
        launchFragmentInHiltContainer<ForgotPasswordFragment>(fragmentArgs = Bundle()) {}
    }

    @Test
    fun test_forgot_password_fragment_back_arrow_in_view() {
        onView(withId(R.id.forgot_password_fragment_back_arrow_image_view)).check(matches(isDisplayed()))
    }

    @Test
    fun test_forgot_password_fragment_title_text_view_in_view() {

        onView(withId(R.id.forgot_password_fragment_forgot_password_text_view)).check(matches(isDisplayed()))
    }

    @Test
    fun test_forgot_password_fragment_edit_text_in_view() {
        onView(withId(R.id.forgot_password_fragment_email_edit_text)).check(matches(isDisplayed()))
    }

    @Test
    fun test_forgot_password_fragment_send_request_button_in_view() {
        onView(withId(R.id.forgot_password_fragment_send_request_button)).perform(swipeUp()).check(matches(isDisplayed()))
    }

//    /*Test Navigation to Confirm password fragment*/
//    @ExperimentalCoroutinesApi
//    @Test
//    fun test_forgot_password_fragment_navigation_to_confirm_reset_password_fragment() {
//
//        // Create a TestNavHostController
//        val navController = mock(NavController::class.java)
//
//        launchFragmentInHiltContainer<ForgotPasswordFragment> {
//            this.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
//                if (viewLifecycleOwner != null) {
//                    // The fragment’s view has just been created
//                    Navigation.setViewNavController(this.requireView(), navController)
//                }
//            }
//        }
//
//        /*Input Data*/
//        // disable animation for this to work
//        onView(withId(R.id.forgot_password_fragment_email_edit_text)).perform(
//            replaceText(
//                EMAIL
//            )
//        )
//
// //        /* Verify that performing a click changes the NavController’s state*/
// //        onView(withId(R.id.forgot_password_fragment_send_request_button)).perform(
// //            click()
// //        )
// //
// //        /*Check if Confirmation Password Reset Fragment is Displayed*/
// //        verify(navController).navigate(R.id.action_forgotPasswordFragment_to_confirmPasswordResetFragment)
//    }
}
