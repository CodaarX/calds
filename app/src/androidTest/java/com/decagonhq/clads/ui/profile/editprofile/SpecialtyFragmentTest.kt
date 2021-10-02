package com.decagonhq.clads.ui.profile.editprofile

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
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
class SpecialtyFragmentTest {

    @Before
    fun setUp() { val scenario = launchFragmentInContainer<SpecialtyFragment>(themeResId = R.style.Base_Theme_MaterialComponents_Light) }

    // test that fragment is displayed
    @Test
    fun test_specialty_fragment_is_in_view() {
        Espresso.onView(ViewMatchers.withId(R.id.specialty_fragment_nested_scroll_view)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()))
    }

    // test that add specialty button  is displayed
    @Test
    fun test_add_specialty_text_is_in_view() {
        Espresso.onView(ViewMatchers.withId(R.id.specialty_fragment_add_new_specialty_icon)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()))
    }

    // test that fragment recycler view is displayed
    @Test
    fun test_recycler_view_is_in_view() {
        Espresso.onView(ViewMatchers.withId(R.id.specialty_fragment_recycler_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    // test that fragment  save button is displayed
    @Test
    fun test_add_specialty_save_button_is_in_view() {
        Espresso.onView(ViewMatchers.withId(R.id.specialty_fragment_nested_scroll_view)).perform(ViewActions.swipeUp())
        Espresso.onView(ViewMatchers.withId(R.id.specialty_fragment_save_changes_material_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

}