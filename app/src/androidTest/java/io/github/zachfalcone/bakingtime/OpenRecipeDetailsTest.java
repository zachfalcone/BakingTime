package io.github.zachfalcone.bakingtime;

import android.graphics.Rect;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.espresso.util.HumanReadables;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.ScrollView;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.zachfalcone.bakingtime.ui.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.AnyOf.anyOf;

@RunWith(AndroidJUnit4.class)
public class OpenRecipeDetailsTest {
    private static final String RECIPE = "Nutella Pie";
    private static final String INGREDIENT = "Graham Cracker crumbs";
    private static final String STEP = "1. Starting prep";
    private static final String DESCRIPTION = "1. Preheat the oven to 350°F. Butter a 9\" deep dish pie pan.";

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

        // check if ingredient matches
        onView(withText(INGREDIENT)).check(matches(isDisplayed()));

        // check if steps recyclerview contains step
        onView(withId(R.id.recycle_steps)).check(matches(hasDescendant(withText(STEP))));

        // click the first step (after the introduction step)
        onView(withId(R.id.recycle_steps)).perform(
                new ScrollToAction(),
                RecyclerViewActions.actionOnItemAtPosition(1, click())
        );

        // check if correct details are displayed
        onView(withText(DESCRIPTION)).check(matches(isDisplayed()));
    }

    // ScrollToAction based off of android.support.test.espresso.action.ScrollToAction (to include NestedScrollView):
    public final class ScrollToAction implements ViewAction {
        private final String TAG = ScrollToAction.class.getSimpleName();
        @SuppressWarnings("unchecked")
        @Override
        public Matcher<View> getConstraints() {
            return allOf(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE), isDescendantOfA(anyOf(
                    isAssignableFrom(ScrollView.class),
                    isAssignableFrom(HorizontalScrollView.class),
                    isAssignableFrom(ListView.class),
                    isAssignableFrom(NestedScrollView.class)
            )));
        }
        @Override
        public void perform(UiController uiController, View view) {
            if (isDisplayingAtLeast(90).matches(view)) {
                Log.i(TAG, "View is already displayed. Returning.");
                return;
            }
            Rect rect = new Rect();
            view.getDrawingRect(rect);
            if (!view.requestRectangleOnScreen(rect, true /* immediate */)) {
                Log.w(TAG, "Scrolling to view was requested, but none of the parents scrolled.");
            }
            uiController.loopMainThreadUntilIdle();
            if (!isDisplayingAtLeast(90).matches(view)) {
                throw new PerformException.Builder()
                        .withActionDescription(this.getDescription())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new RuntimeException(
                                "Scrolling to view was attempted, but the view is not displayed"))
                        .build();
            }
        }
        @Override
        public String getDescription() {
            return "scroll to";
        }
    }
}
