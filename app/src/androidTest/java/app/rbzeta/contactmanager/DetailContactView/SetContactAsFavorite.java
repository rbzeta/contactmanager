package app.rbzeta.contactmanager.DetailContactView;

import android.content.Intent;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import app.rbzeta.contactmanager.R;
import app.rbzeta.contactmanager.activity.ContactDetailActivity;
import app.rbzeta.contactmanager.activity.ContactListActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Robyn on 05/11/2016.
 */

@RunWith(JUnit4.class)
@LargeTest
public class SetContactAsFavorite {

    @Rule
    public ActivityTestRule<ContactDetailActivity> mActivityRule =
            new ActivityTestRule<>(ContactDetailActivity.class,true,false);

    @Before
    public void setupIntentActivity(){
        Intent i = new Intent();
        i.putExtra(ContactListActivity.EXTRA_CONTACT_ID,86);
        mActivityRule.launchActivity(i);
    }

    @Test
    public void clickTextFavorite_sameActivity(){

        onView(withId(R.id.text_contact_detail_name))
                .perform(click());
    }

}
