package com.decagonhq.clads.ui.profile.editprofile

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.decagonhq.clads.R
import dagger.hilt.android.AndroidEntryPoint
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@AndroidEntryPoint
@RunWith(AndroidJUnit4::class)
class MapFragmentTest {


    @Before
    fun setUp() { val scenerio = launchFragmentInContainer<MapFragment>(themeResId = R.style.Base_Theme_MaterialComponents_Light) }


    @Test
    fun test_fragment_is_visible(){
        Espresso.onView(ViewMatchers.withId(R.id.mapFragment)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}