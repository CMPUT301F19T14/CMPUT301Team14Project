package com.cmput301t14.mooditude;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.cmput301t14.mooditude.activities.DisplayFollow;
import com.cmput301t14.mooditude.activities.HomeActivity;
import com.cmput301t14.mooditude.activities.MainActivity;
import com.cmput301t14.mooditude.activities.MapsActivity;
import com.cmput301t14.mooditude.activities.SelfActivity;
import com.cmput301t14.mooditude.activities.SignInActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class IntentTestMap {

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


        String email="ui2@test.com";
        solo.enterText((EditText) solo.getView(R.id.signin_email_edit_text),email);
        solo.enterText((EditText) solo.getView(R.id.signin_password_edit_text),"123456");

        solo.clickOnButton("Sign In");

        solo.waitForActivity(HomeActivity.class);

        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

    }

    @Test
    public void start() throws Exception{
        Activity activity= rule.getActivity();

    }

    @Test
    public void testOpenHomeMapActivity(){
        solo.clickOnView(solo.getView(R.id.home_map_button));

        solo.waitForActivity(MapsActivity.class);

        solo.assertCurrentActivity("Not Maps Activity",MapsActivity.class);

    }

    @Test
    public void testOpenSelfMapActivity(){

        solo.clickOnView(solo.getView(R.id.navigation_self));

        solo.waitForActivity(SelfActivity.class);

        solo.assertCurrentActivity("Not Maps Activity",SelfActivity.class);

        solo.clickOnView(solo.getView(R.id.googleMapsImageButton));

        solo.waitForActivity(MapsActivity.class);

        solo.assertCurrentActivity("Not Maps Activity",MapsActivity.class);

    }

}
