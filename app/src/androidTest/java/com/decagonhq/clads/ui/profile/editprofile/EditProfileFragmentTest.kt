package com.decagonhq.clads.ui.profile.editprofile

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
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
class EditProfileFragmentTest {

    @Before
    fun setUp() { val scenerio = launchFragmentInContainer<EditProfileFragment>(themeResId = R.style.Base_Theme_MaterialComponents_Light)}
    // test that fragment is displayed
    @Test
    fun test_profile_fragment_is_in_view(){
        Espresso.onView(ViewMatchers.withId(R.id.edit_profile_fragment_root_layout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    // test that fragment is displayed
    @Test
    fun test_viewPagers_are_in_view(){
        Espresso.onView(ViewMatchers.withId(R.id.edit_profile_view_pager)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    // ensure tab layout correctly displays its view pager
    @Test
    fun test_tab_layout_displays_correct_view_pager(){
        Espresso.onView(ViewMatchers.withId(R.id.account_tab_item)).perform(click())
        Espresso.onView(ViewMatchers.withId(R.id.account_fragment_nested_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.specialty_tab_item)).perform(click())
        Espresso.onView(ViewMatchers.withId(R.id.specialty_fragment_nested_scroll_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.payment_tab_item)).perform(click())
        Espresso.onView(ViewMatchers.withId(R.id.payment_tab_nested_scroll_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.edit_profile_tab_layout)).perform(swipeLeft())
        Espresso.onView(ViewMatchers.withId(R.id.security_tab_item)).perform(click())
        Espresso.onView(ViewMatchers.withId(R.id.edit_profile_security_fragment)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }
}