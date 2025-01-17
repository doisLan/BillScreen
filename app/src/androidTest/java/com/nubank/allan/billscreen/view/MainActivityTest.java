package com.nubank.allan.billscreen.view;

import android.support.test.rule.ActivityTestRule;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.LargeTest;

import com.nubank.allan.billscreen.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isSelected;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Created by doisl_000 on 1/22/2016.
 */
@RunWith(JUnit4.class)
@LargeTest
public class MainActivityTest extends InstrumentationTestCase {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testSwipeView() {
        onView(allOf(withId(R.id.viewpager), isDisplayed())).perform(swipeLeft());
        onView(allOf(withId(R.id.tabTitle), isSelected())).check(matches(withText("ABR")));
        onView(allOf(withId(R.id.viewpager), isDisplayed())).perform(swipeLeft());
        onView(allOf(withId(R.id.tabTitle), isSelected())).check(matches(withText("MAI")));
        onView(allOf(withId(R.id.viewpager), isDisplayed())).perform(swipeLeft());
        onView(allOf(withId(R.id.tabTitle), isSelected())).check(matches(withText("JUN")));
        onView(allOf(withId(R.id.viewpager), isDisplayed())).perform(swipeRight());
        onView(allOf(withId(R.id.tabTitle), isSelected())).check(matches(withText("MAI")));
        onView(allOf(withId(R.id.viewpager), isDisplayed())).perform(swipeRight());
        onView(allOf(withId(R.id.tabTitle), isSelected())).check(matches(withText("ABR")));
        onView(allOf(withId(R.id.viewpager), isDisplayed())).perform(swipeRight());
        onView(allOf(withId(R.id.tabTitle), isSelected())).check(matches(withText("MAR")));
    }

    @Test
    public void testScroll() throws InterruptedException {
        onView(allOf(withId(R.id.viewpager), isDisplayed())).perform(swipeLeft());
        onView(withText("Pagamento recebido")).perform(scrollTo()).check(matches(allOf(withText("Pagamento recebido"), isDisplayed())));
    }

    @Test
    public void testClickTabs(){
        onView(allOf(withId(R.id.tabTitle), withText("ABR"))).perform(click());
        onView(allOf(withId(R.id.totalAmount), isDisplayed())).check(matches(withText("R$1.743,26")));
        onView(allOf(withId(R.id.DateRangeText), isDisplayed())).check(matches(withText("07 ABR ATÉ 07 MAI")));
        onView(allOf(withId(R.id.tabTitle), withText("MAI"))).perform(click());
        onView(allOf(withId(R.id.totalAmount), isDisplayed())).check(matches(withText("R$396,27")));
        onView(allOf(withId(R.id.DateRangeText), isDisplayed())).check(matches(withText("07 MAI ATÉ 05 JUN")));
        onView(allOf(withId(R.id.tabTitle), withText("JUN"))).perform(click());
        onView(allOf(withId(R.id.totalAmount), isDisplayed())).check(matches(withText("R$19,54")));
        onView(allOf(withId(R.id.DateRangeText), isDisplayed())).check(matches(withText("05 JUN ATÉ 07 JUL")));
        onView(allOf(withId(R.id.tabTitle), withText("MAR"))).perform(click());
        onView(allOf(withId(R.id.totalAmount), isDisplayed())).check(matches(withText("R$389,33")));
        onView(allOf(withId(R.id.DateRangeText), isDisplayed())).check(matches(withText("17 MAR ATÉ 07 ABR")));
    }
}
