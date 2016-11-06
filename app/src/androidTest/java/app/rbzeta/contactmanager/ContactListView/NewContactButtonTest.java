package app.rbzeta.contactmanager.ContactListView;

import android.support.test.espresso.matcher.ViewMatchers;
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

/**
 * Created by Robyn on 05/11/2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class NewContactButtonTest {

    @Rule
    public ActivityTestRule<ContactListActivity> mActivityTestRule = new ActivityTestRule<ContactListActivity>(ContactListActivity.class);

    @Test
    public void fabButtonPressed_newActivity(){
        onView(ViewMatchers.withId(R.id.fab)).perform(click());
    }

    @Test
    public void menuItemButtonPressed_newActivity(){
        onView(ViewMatchers.withId(R.id.action_add_contact)).perform(click());
    }
}
