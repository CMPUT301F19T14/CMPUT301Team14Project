package com.cmput301t14.mooditude;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class IntentTestWithSignIn {

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


        String userName="testC";
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
    public void 

//    @Test

//    public void check(){
//        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
//
//        solo.clickOnButton("Join Now");
//
//        solo.waitForText("Welcome to Moodtter",1,2000);
//
//        solo.assertCurrentActivity("Wrong Activity", RegisterActivity.class);
//        String userName="testC";
//        solo.enterText((EditText) solo.getView(R.id.register_email),userName+"@test.com");
//        solo.enterText((EditText) solo.getView(R.id.register_username),userName);
//        solo.enterText((EditText) solo.getView(R.id.register_password),"123456");
//        solo.enterText((EditText) solo.getView(R.id.register_password_2),"123456");
//        solo.clickOnButton("Join Now");
//
//        solo.waitForText("Mooditude",1,2000);
//        solo.assertCurrentActivity("Wrong Activity", SignInActivity.class);
//
//    }


}