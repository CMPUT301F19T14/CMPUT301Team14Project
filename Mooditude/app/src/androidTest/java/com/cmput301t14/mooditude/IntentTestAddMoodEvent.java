package com.cmput301t14.mooditude;

import android.app.Activity;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.cmput301t14.mooditude.activities.AddActivity;
import com.cmput301t14.mooditude.activities.HomeActivity;
import com.cmput301t14.mooditude.activities.MainActivity;
import com.cmput301t14.mooditude.activities.SelfActivity;
import com.cmput301t14.mooditude.activities.SignInActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

public class IntentTestAddMoodEvent {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class,true,true);


    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnButton("Sign In");

        solo.waitForText("Mooditude",1,2000);
        solo.assertCurrentActivity("Wrong Activity", SignInActivity.class);


        String userName="ui";
        solo.enterText((EditText) solo.getView(R.id.signin_email_edit_text),userName+"@test.com");
        solo.enterText((EditText) solo.getView(R.id.signin_password_edit_text),"123456");

        solo.clickOnButton("Sign In");

//        solo.waitForText("Welcome to Moodtter",1,2000);
        solo.waitForActivity(HomeActivity.class);

        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

    }

    @Test
    public void start() throws Exception{
        Activity activity= rule.getActivity();

    }

    @Test
    public void addMoodEventWithCheck(){

        solo.clickOnView(solo.getView(R.id.navigation_add));
        solo.waitForActivity(AddActivity.class);

        solo.clickOnView(solo.getView(Spinner.class, 0));
        solo.scrollToTop();
        solo.clickOnText("ANGRY");

        solo.clickOnView(solo.getView(Spinner.class, 1));
        solo.scrollToTop();
        solo.clickOnText("WITH_ANOTHER_PERSON");

        solo.enterText((EditText) solo.getView(R.id.comment_edittext),"ui@test.com ADD");


        solo.clickOnView(solo.getView(Spinner.class, 2));
        solo.scrollToTop();
        solo.clickOnText("INCLUDE LOCATION");


        solo.clickOnView(solo.getView(R.id.submit_button));
        solo.sleep(100);

        solo.clickOnView(solo.getView(R.id.navigation_self));
        solo.waitForActivity(SelfActivity.class);

        solo.waitForActivity(SelfActivity.class);
        solo.clickInList(0);
        solo.waitForFragmentById(R.id.frag_frame_add,1000);

        solo.waitForText("ANGRY");
        solo.waitForText("WITH_ANOTHER_PERSON");
        solo.waitForText("ui@test.com ADD");
    }




}
