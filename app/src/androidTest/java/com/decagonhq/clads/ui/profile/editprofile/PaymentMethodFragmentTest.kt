package com.decagonhq.clads.ui.profile.editprofile

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.decagonhq.clads.R
import dagger.hilt.android.AndroidEntryPoint
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@AndroidEntryPoint
@RunWith(AndroidJUnit4ClassRunner::class)
class PaymentMethodFragmentTest {

    @Before
    fun setUp() { val scenerio = launchFragmentInContainer<PaymentMethodFragment>(themeResId = R.style.Base_Theme_MaterialComponents_Light)
    }

    // check if fragment is displayed
    @Test
    fun payment_fragment_is_displayed (){
        Espresso.onView(ViewMatchers.withId(R.id. payment_tab_nested_scroll_view))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    // check if button is displayed
    @Test
    fun save_payment_button (){
        Espresso.onView(ViewMatchers.withId(R.id.  payment_method_fragment_save_changes_button))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

}