package app.rbzeta.contactmanager.AddNewContactView;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import app.rbzeta.contactmanager.R;
import app.rbzeta.contactmanager.activity.ContactAddNewActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Robyn on 05/11/2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class CreateContactTest {
    private String firstName,lastName,phone,email;

    @Rule
    public ActivityTestRule<ContactAddNewActivity> mActivityRule = new ActivityTestRule<ContactAddNewActivity>(ContactAddNewActivity.class);

    @Before
    public void initString(){
        firstName = "Brandon";
        lastName = "Star";
        phone = "+6287787878787";
        email = "bran.don@got.com";
    }

    @Test
    public void inputTextandSubmit_sameActivity(){

        onView(ViewMatchers.withId(R.id.edit_contact_first_name))
                .perform(typeText(firstName),closeSoftKeyboard());

        onView(withId(R.id.edit_contact_last_name))
                .perform(typeText(lastName),closeSoftKeyboard());

        onView(withId(R.id.edit_contact_phone_number))
                .perform(typeText(phone),closeSoftKeyboard());

        onView(withId(R.id.edit_contact_email_address))
                .perform(typeText(email),closeSoftKeyboard());

        onView(withId(R.id.button_save_new_contact))
                .perform(click());

    }

}
