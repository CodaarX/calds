package com.decagonhq.clads.ui.profile

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.decagonhq.clads.R
import dagger.hilt.android.AndroidEntryPoint
import org.junit.Before
import org.junit.runner.RunWith


@AndroidEntryPoint
@RunWith(AndroidJUnit4ClassRunner::class)

class HomeFragmentTest {

    
    @Before
    fun setUp() {

        fun setUp() {
            val scenerio = launchFragmentInContainer<HomeFragment>(themeResId = R.style.Base_Theme_MaterialComponents_Light)
        }
    }
    
    fun test_dashboard_activity_visibility(){
        Espresso.onView(ViewMatchers.withId(R.id.home_fragment_root_layout)).check(matches(ViewMatchers.isDisplayed()))
    }

}