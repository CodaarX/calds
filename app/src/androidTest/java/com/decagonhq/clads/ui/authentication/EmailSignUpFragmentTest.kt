package com.decagonhq.clads.ui.authentication

import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
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
class EmailSignUpFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        hiltRule.inject()
        launchFragmentInHiltContainer<EmailSignUpFragment>(fragmentArgs = Bundle()) {}
    }

    @Test
    fun test_email_fragment_title_text_view_in_view() {
        onView(withId(R.id.email_sign_up_fragment_title_text_view)).check(matches(isDisplayed()))
    }

    @Test
    fun test_email_fragment_first_name_text_view_in_view() {
        onView(withId(R.id.email_sign_up_fragment_first_name_edit_text))
            .check(matches(isDisplayed()))
    }

    @Test
    fun test_email_fragment_account_category_dropdown_in_view() {
        onView(withId(R.id.email_sign_up_fragment_account_category_text_view)).check(matches(isDisplayed()))
    }

    @Test
    fun test_email_fragment_account_sign_up_button_in_view() {
        onView(withId(R.id.email_sign_up_fragment_password_edit_text)).perform(swipeUp(), replaceText(PASSWORD))
        onView(withId(R.id.email_sign_up_fragment_confirm_password_edit_text)).perform(swipeUp(), replaceText(PASSWORD))
        closeSoftKeyboard()
        swipeUp()
        onView(withId(R.id.email_sign_up_fragment_signup_button)).check(matches(isDisplayed()))
    }

//    /*Test Navigation to Profile Activity*/
//    @ExperimentalCoroutinesApi
//    @Test
//    fun test_email_signup_fragment_navigation_to_email_confirmation_fragment() {
//        // Create a TestNavHostController
//        val navController = mock(NavController::class.java)
//
//        launchFragmentInHiltContainer<EmailSignUpFragment>(fragmentArgs = Bundle()) {
//            this.viewLifecycleOwnerLiveData .observeForever { viewLifecycleOwner ->
//                if (viewLifecycleOwner != null) {
//                    // The fragment’s view has just been created
//                    Navigation.setViewNavController(this.requireView(), navController)
//                }
//            }
//        }
//
// //        val scenario =
// //            launchFragmentInContainer(themeResId = R.style.Base_Theme_MaterialComponents) {
// //                EmailSignUpFragment().also { fragment ->
// //
// //                    fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
// //                        if (viewLifecycleOwner != null) {
// //                            // The fragment’s view has just been created
// //                            Navigation.setViewNavController(fragment.requireView(), navController)
// //                        }
// //                    }
// //                }
// //            }
//
//        /*Input Data*/
//        onView(withId(R.id.email_sign_up_fragment_first_name_edit_text)).perform(replaceText(FIRST_NAME))
//        closeSoftKeyboard()
//        onView(withId(R.id.email_sign_up_fragment_last_name_edit_text)).perform(replaceText(LAST_NAME))
//        closeSoftKeyboard()
//        onView(withId(R.id.email_sign_up_fragment_email_edit_text)).perform(replaceText(EMAIL))
//        closeSoftKeyboard()
//        onView(withId(R.id.email_sign_up_fragment_phone_number_edit_text)).perform(replaceText(PHONE))
//        closeSoftKeyboard()
//        /*Select Spinner Item*/
//        onView(withId(R.id.email_sign_up_fragment_account_category_text_view)).perform(replaceText(ACCOUNT_TYPE))
//        onView(withId(R.id.email_sign_up_fragment_password_edit_text)).perform(swipeUp(), replaceText(PASSWORD))
//        closeSoftKeyboard()
//        onView(withId(R.id.email_sign_up_fragment_confirm_password_edit_text)).perform(
//            swipeUp(),
//            replaceText(PASSWORD),
//            swipeUp()
//        )
//        closeSoftKeyboard()
//        Thread.sleep(250)
//        /* Verify that performing a click changes the NavController’s state*/
//        swipeUp()
//        onView(withId(R.id.email_sign_up_fragment_signup_button)).perform(click())
//        Thread.sleep(5000)
//        onView(withId(R.id.email_confirmation_fragment_card_view)).check(matches(isDisplayed()))
//    }

    companion object {
        const val FIRST_NAME = "John"
        const val LAST_NAME = "Doe"
        const val PHONE = "08167930847"
        const val EMAIL = "johndoe@gmail.com"
        const val PASSWORD = "Abcde@1234"
        const val ACCOUNT_TYPE = "Tailor"
    }
}
