package com.decagonhq.clads.ui.profile
//
//import androidx.appcompat.app.AppCompatActivity
//import androidx.test.espresso.Espresso
//import androidx.test.espresso.action.ViewActions
//import androidx.test.espresso.assertion.ViewAssertions
//import androidx.test.espresso.matcher.ViewMatchers
//import androidx.test.ext.junit.rules.ActivityScenarioRule
//import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
//import com.decagonhq.clads.R
//import dagger.hilt.android.testing.HiltAndroidTest
//import org.junit.Before
//import org.junit.Test
//import org.junit.runner.RunWith
//
//@RunWith(AndroidJUnit4ClassRunner::class)
//class DashboardActivityTest {
//
//    @Before
//    fun setUp() {
//        ActivityScenarioRule(DashboardActivity::class.java)
//    }
//
//    @Test
//    fun verify_navigation_menu_isvisible() {
//        Espresso.onView(ViewMatchers.withId(R.id.dashboard_activity_bottom_navigation_view)).check(
//            ViewAssertions.matches(
//                ViewMatchers.isDisplayed()
//            )
//        )
//    }
//
//    @Test
//    fun test_bottom_navigation_nav_controller() {
//
////        // Create a TestNavHostController
////        val navController = Mockito.mock(NavController::class.java)
////        val scenario = launchFragmentInContainer(themeResId = R.style.Base_Theme_MaterialComponents) {
////                SignUpOptionsFragment().also { fragment ->
////                    fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
////                        if (viewLifecycleOwner != null) {
////                            // The fragment’s view has just been created
////                            Navigation.setViewNavController(fragment.requireView(), navController)
////                        }
////                    }
////                }
////            }
//
//        /* Verify that performing a click on home menu navigates to home fragment*/
//        Espresso.onView(ViewMatchers.withId(R.id.nav_home))
//            .perform(ViewActions.click())
////        Mockito.verify(navController).navigate(R.id.nav_home)
//
//        /* Verify that performing a click on media menu navigates to media fragment*/
//        Espresso.onView(ViewMatchers.withId(R.id.nav_media))
//            .perform(ViewActions.click())
////        Mockito.verify(navController).navigate(R.id.nav_media)
//
//        /* Verify that performing a click on chat menu navigates to chat fragment*/
//        Espresso.onView(ViewMatchers.withId(R.id.nav_messages))
//            .perform(ViewActions.click())
////        Mockito.verify(navController).navigate(R.id.nav_messages)
//    }
//}
