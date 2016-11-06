package app.rbzeta.contactmanager.ContactListView;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import app.rbzeta.contactmanager.R;
import app.rbzeta.contactmanager.activity.ContactListActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Robyn on 05/11/2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecyclerViewTest {

    @Rule
    public ActivityTestRule<ContactListActivity> mRuleActivity =
            new ActivityTestRule<>(ContactListActivity.class);

    @Test
    public void selectRecyclerViewAtGivenPosition_sameActivity(){

        onView(withId(R.id.recycler_view_contact_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(25,click()));

    }

}
