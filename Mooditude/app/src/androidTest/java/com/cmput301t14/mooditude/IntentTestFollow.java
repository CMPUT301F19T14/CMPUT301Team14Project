package com.cmput301t14.mooditude;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.cmput301t14.mooditude.activities.DisplayFollow;
import com.cmput301t14.mooditude.activities.HomeActivity;
import com.cmput301t14.mooditude.activities.MainActivity;
import com.cmput301t14.mooditude.activities.SelfActivity;
import com.cmput301t14.mooditude.activities.SignInActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class IntentTestFollow {
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
    public void getFollower(){
        solo.clickOnView(solo.getView(R.id.navigation_self));

        solo.waitForActivity(SelfActivity.class);
        solo.waitForText("2");


        solo.clickOnView(solo.getView(R.id.follower));

        solo.waitForActivity(DisplayFollow.class);

        solo.waitForText("ui2@test.com");
        solo.waitForText("wangye@warning.com");

    }

    @Test
    public void getFollowing(){
        solo.clickOnView(solo.getView(R.id.navigation_self));

        solo.waitForActivity(SelfActivity.class);
        solo.waitForText("2");


        solo.clickOnView(solo.getView(R.id.following));

        solo.waitForActivity(DisplayFollow.class);

        solo.waitForText("ui2@test.com");
        solo.waitForText("wangye@warning.com");



    }

}
