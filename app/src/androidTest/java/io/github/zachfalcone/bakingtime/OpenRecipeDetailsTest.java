package io.github.zachfalcone.bakingtime;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.zachfalcone.bakingtime.ui.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollTo;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class OpenRecipeDetailsTest {
    private static final String RECIPE = "Nutella Pie";
    private static final String INGREDIENT = "Graham Cracker crumbs";
    private static final String STEP = "1. Starting prep";
    private static final String DESCRIPTION = "Preheat the oven";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testRecipeDetails() {
        // check if a recipe is shown
        onView(withText(RECIPE)).check(matches(isDisplayed()));

        // click the first recipe
        onView(withId(R.id.recycle_recipes))
                .perform(actionOnItemAtPosition(0, click()));

        // check if an ingredient is shown
        onView(withText(INGREDIENT)).check(matches(isDisplayed()));

        // check if steps recyclerview contains a step
        onView(withId(R.id.recycle_steps)).check(matches(hasDescendant(withText(STEP))));

        // click the first step (after the introduction step)
        // onView(withId(R.id.recycle_steps)).perform(ViewActions.scrollTo());
    }
}
