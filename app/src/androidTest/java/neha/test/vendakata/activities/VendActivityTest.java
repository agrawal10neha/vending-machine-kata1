package firetiger.net.vendakata.activities;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import firetiger.net.vendakata.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class VendActivityTest {

    @Rule
    public ActivityTestRule<VendActivity> mActivityTestRule = new ActivityTestRule<>(VendActivity.class);

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    @Test
    public void smokeTestVendActivity() throws Exception {
        onView(withId(R.id.vend_btn_return))
                .perform(click());
    }

    @Test
    public void insertCoinsDialogTest() {
        final ViewInteraction display = onView(
                allOf(withId(R.id.vend_display), isDisplayed()));
        final ViewInteraction btnCollect = onView(allOf(withId(R.id.vend_btn_collect), isDisplayed()));

        // insert coins dialog
        onView(allOf(withId(R.id.vend_btn_insert), withText("Insert Coins"), isDisplayed()))
                .perform(click());
        onView(allOf(childAtPosition(withId(R.id.custom), 0), isDisplayed()))
                .perform(replaceText(".25"), closeSoftKeyboard());
        onView(allOf(withId(android.R.id.button1), withText("OK")))
                .perform(scrollTo(), click());

        // verify display updates
        display.check(matches(withText("$0.25")));

        // return coins
        onView(allOf(withId(R.id.vend_btn_return), withText("Return"), isDisplayed()))
                .perform(click());
        display.check(matches(withText("INSERT COIN")));
        btnCollect.check(matches(withText("Change: $0.25")));

        // collect change
        btnCollect.perform(click());
        btnCollect.check(matches(withText("Change: $0.00")));
    }

    @Test
    public void insufficientFundsTest() {
        ViewInteraction display = onView(allOf(withId(R.id.vend_display), isDisplayed()));

        // assert initial text
        display.check(matches(withText("INSERT COIN")));

        // fail to purchase a product
        onView(allOf(withId(R.id.vend_btn_purchase_1), withText("Cola"), isDisplayed()))
                .perform(click());
        display.check(matches(withText("PRICE $1.00")));

        // fail to buy some chips
        onView(allOf(withId(R.id.vend_btn_purchase_2), withText("Chips"), isDisplayed()))
                .perform(click());
        display.check(matches(withText("PRICE $0.50")));

        onView(allOf(withId(R.id.vend_btn_return), isDisplayed()))
                .perform(click());

        // assert display reset
        display.check(matches(withText("INSERT COIN")));
    }
}
