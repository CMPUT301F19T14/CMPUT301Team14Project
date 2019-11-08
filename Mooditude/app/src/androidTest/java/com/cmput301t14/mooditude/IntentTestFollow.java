package com.cmput301t14.mooditude;

import android.app.Activity;
import android.widget.EditText;
import android.widget.ListView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

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


        String userName="xianda";
        solo.enterText((EditText) solo.getView(R.id.signin_email_edit_text),userName+"@gmail.com");
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
        SelfActivity activity = (SelfActivity) solo.getCurrentActivity();


        String numFollower = solo.getString(R.id.number_of_follower);
        assertEquals(2, numFollower);

        solo.clickOnView(solo.getView(R.id.follower));

        solo.waitForActivity(DisplayFollow.class);

    }

    @Test
    public void getFollowing(){
        solo.clickOnView(solo.getView(R.id.navigation_self));

        solo.waitForActivity(SelfActivity.class);
        SelfActivity activity = (SelfActivity) solo.getCurrentActivity();


        String numFollowing = solo.getString(R.id.number_of_following);
        assertEquals(3, numFollowing);


        solo.clickOnView(solo.getView(R.id.following));

        solo.waitForActivity(DisplayFollow.class);



    }

}
