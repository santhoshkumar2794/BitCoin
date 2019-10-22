package com.zestworks.bitcoin


import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import com.jakewharton.espresso.OkHttp3IdlingResource
import com.zestworks.bitcoin.data.BitCoinApi.Companion.okHttpClient
import com.zestworks.bitcoin.ui.MainActivity
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class BitCoinInstrumentedTest {

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    private val idlingResource = OkHttp3IdlingResource.create("okHttp", okHttpClient)

    @Before
    fun setup() {
        IdlingRegistry.getInstance().register(idlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(idlingResource)
    }

    @Test
    fun check_whether_market_price_loader_for_default_duration() {
        onView(withId(R.id.currentPrice))
            .check(ViewAssertions.matches(isDisplayed()))

        onView(withId(R.id.lineChart))
            .check(ViewAssertions.matches(isDisplayed()))

        onView(withId(R.id.durationToggleGroup))
            .check(ViewAssertions.matches(isDisplayed()))

        onView(withId(R.id.currentPrice)).check(
            ViewAssertions.matches(Matchers.not(withText("")))
        )

        //Checking whether all the button are unchecked except the one week
        onView(withId(R.id.oneWeek)).check(
            ViewAssertions.matches(isChecked())
        )

        onView(withId(R.id.oneMonth)).check(
            ViewAssertions.matches(isNotChecked())
        )

        onView(withId(R.id.sixMonths)).check(
            ViewAssertions.matches(isNotChecked())
        )

        onView(withId(R.id.oneYear)).check(
            ViewAssertions.matches(isNotChecked())
        )
    }

    @Test
    fun check_whether_changing_the_duration_fetches_market_price() {

        onView(withId(R.id.oneYear))
            .perform(click())

        onView(withId(R.id.currentPrice))
            .check(ViewAssertions.matches(isDisplayed()))

        onView(withId(R.id.lineChart))
            .check(ViewAssertions.matches(isDisplayed()))

        onView(withId(R.id.durationToggleGroup))
            .check(ViewAssertions.matches(isDisplayed()))

        onView(withId(R.id.currentPrice)).check(
            ViewAssertions.matches(Matchers.not(withText("")))
        )

        //Checking whether all the button are unchecked except the one year
        onView(withId(R.id.oneWeek)).check(
            ViewAssertions.matches(isNotChecked())
        )

        onView(withId(R.id.oneMonth)).check(
            ViewAssertions.matches(isNotChecked())
        )

        onView(withId(R.id.sixMonths)).check(
            ViewAssertions.matches(isNotChecked())
        )

        onView(withId(R.id.oneYear)).check(
            ViewAssertions.matches(isChecked())
        )
    }

    @Test
    fun check_whether_on_changing_the_orientation_the_data_persists() {
        onView(withId(R.id.currentPrice))
            .check(ViewAssertions.matches(isDisplayed()))

        onView(withId(R.id.lineChart))
            .check(ViewAssertions.matches(isDisplayed()))

        onView(withId(R.id.durationToggleGroup))
            .check(ViewAssertions.matches(isDisplayed()))

        onView(withId(R.id.currentPrice)).check(
            ViewAssertions.matches(Matchers.not(withText("")))
        )

        //Checking whether all the button are unchecked except the one week
        onView(withId(R.id.oneWeek)).check(
            ViewAssertions.matches(isChecked())
        )

        onView(withId(R.id.oneMonth)).check(
            ViewAssertions.matches(isNotChecked())
        )

        onView(withId(R.id.sixMonths)).check(
            ViewAssertions.matches(isNotChecked())
        )

        onView(withId(R.id.oneYear)).check(
            ViewAssertions.matches(isNotChecked())
        )

        rotateScreen()

        //Checking again whether all the button are unchecked except the one week
        onView(withId(R.id.oneWeek)).check(
            ViewAssertions.matches(isChecked())
        )

        onView(withId(R.id.oneMonth)).check(
            ViewAssertions.matches(isNotChecked())
        )

        onView(withId(R.id.sixMonths)).check(
            ViewAssertions.matches(isNotChecked())
        )

        onView(withId(R.id.oneYear)).check(
            ViewAssertions.matches(isNotChecked())
        )

    }

    private fun rotateScreen() {
        val context = getInstrumentation().targetContext
        val orientation = context.resources.configuration.orientation

        val activity = activityRule.activity
        activity.requestedOrientation = if (orientation == Configuration.ORIENTATION_PORTRAIT)
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        else
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
}