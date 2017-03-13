package testing.magnum.io.testingexample;


import android.support.test.espresso.assertion.ViewAssertions.*;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.action.ViewActions.*;
import static org.hamcrest.Matchers.*;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
            LoginActivity.class);


    @Before
    public void setUp() {
        mActivityRule.getActivity();
    }

    @Test
    public void testThatErrorMsgIsNotInitiallyDisplayed() {
        onView(withId(R.id.errorMessage))
                .check(matches(not(isDisplayed())));
    }

    @Test
    public void testPasswordLengthRuleTriggersErrorMsg() {
        onView(withId(R.id.passwordEditText)).perform(typeText("abc"));
        onView(withId(R.id.loginButton)).perform(click());

        onView(withId(R.id.errorMessage))
                .check(matches(isDisplayed()))
                .check(matches(withText("Bad password")));
    }

    @Test
    public void testValidPasswordDoesNotDisplayErrorMsg() {
        onView(withId(R.id.passwordEditText)).perform(typeText("a long valid password 23423423 asdf__sadfasd"));
        onView(withId(R.id.loginButton)).perform(click());

        onView(withId(R.id.errorMessage)).check(matches(not(isDisplayed())));
    }
}

