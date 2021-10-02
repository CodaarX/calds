package com.decagonhq.clads.ui.profile.editprofile

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.decagonhq.clads.R
import dagger.hilt.android.AndroidEntryPoint
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@AndroidEntryPoint
@RunWith(AndroidJUnit4ClassRunner::class)
class SecurityFragmentTest {

    @Before
    fun setUp() { val scenario = launchFragmentInContainer<SecurityFragment>(themeResId = R.style.Base_Theme_MaterialComponents_Light) }


    // test that fragment is displayed
    @Test
    fun test_security_fragment_is_in_view() {
        Espresso.onView(ViewMatchers.withId(R.id.edit_profile_security_fragment)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()))
    }

    // test that fragment views are displayed
    @Test
    fun test_security_fragment_save_button_is_in_view() {
        Espresso.onView(ViewMatchers.withId(R.id.security_fragment_save_changes_button)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()))
    }


    // test that input field  is displayed
    @Test
    fun test_security_fragment_phone_number_edit_text_is_in_view() {
        Espresso.onView(ViewMatchers.withId(R.id.security_fragment_phone_number_input_text_view)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()))
    }

}