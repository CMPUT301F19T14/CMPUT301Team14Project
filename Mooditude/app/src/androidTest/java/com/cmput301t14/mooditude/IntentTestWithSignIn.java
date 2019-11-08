package com.cmput301t14.mooditude;

import android.app.Activity;
import android.app.Fragment;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class IntentTestWithSignIn {

    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);


    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnButton("Sign In");

        solo.waitForText("Mooditude", 1, 2000);
        solo.assertCurrentActivity("Wrong Activity", SignInActivity.class);


        String userName = "testc";
        solo.enterText((EditText) solo.getView(R.id.signin_email_edit_text), userName + "@test.com");
        solo.enterText((EditText) solo.getView(R.id.signin_password_edit_text), "123456");

        solo.clickOnButton("Sign In");

//        solo.waitForText("Welcome to Moodtter",1,2000);
        solo.waitForActivity(HomeActivity.class);

        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

    }

    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void checkAdd(){
//        solo.clickOnMenuItem("add");
//        solo.clickOnMenuItem("add");
//        solo.clickOnMenuItem("add",true);
//        solo.clickOnButton(R.id.navigation_add);
        solo.clickOnView(solo.getView(R.id.navigation_add));

        solo.enterText((EditText) solo.getView(R.id.comment_edittext),"testing comment");




//        View view1 =
        solo.clickOnView(solo.getView(Spinner.class, 0));
        solo.scrollToTop();
        solo.clickOnView(solo.getView(TextView.class, 2));

        solo.clickOnView(solo.getView(Spinner.class, 1));
        solo.scrollToTop();
        solo.clickOnView(solo.getView(TextView.class, 2));

        solo.clickOnView(solo.getView(R.id.submit_button));

        solo.waitForActivity(SelfActivity.class,1000);

        solo.clickInList(0);


//        Fragment fragment = solo.getCurrentActivity().getFragmentManager().findFragmentById(R.id.frag_frame_add);

        solo.waitForFragmentById(R.id.frag_frame_add,1000);

        solo.waitForText("SAD");

        solo.waitForText("ALONE");
        solo.waitForText("testing comment");

        solo.clickOnView(solo.getView(Spinner.class, 0));
        solo.scrollToTop();
        solo.clickOnText("HAPPY");

        solo.clickOnView(solo.getView(Spinner.class, 1));
        solo.scrollToTop();
        solo.clickOnText("CROWD");

        solo.clickOnText("OK");
    }

    @Test
    public void checkFind() {

        solo.clickOnView(solo.getView(R.id.navigation_search));

        solo.enterText((EditText) solo.getView(R.id.search_edit_text), "ye");

        solo.waitForText("wangye");

    }
}