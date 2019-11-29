package com.cmput301t14.mooditude;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.cmput301t14.mooditude.activities.AddActivity;
import com.cmput301t14.mooditude.activities.HomeActivity;
import com.cmput301t14.mooditude.activities.MainActivity;
import com.cmput301t14.mooditude.activities.SignInActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class IntentTestPhotoCamera {
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
    public void addCamera(){
        solo.clickOnView(solo.getView(R.id.navigation_add));

        solo.waitForActivity(AddActivity.class);

        solo.clickOnView(solo.getView(R.id.camera_button));

        solo.waitForActivity(MediaStore.ACTION_IMAGE_CAPTURE);

        solo.sleep(500);

        solo.assertCurrentActivity("Wrong Activity", MediaStore.ACTION_IMAGE_CAPTURE);



    }
    @Test
    public void choosePhoto(){
        solo.clickOnView(solo.getView(R.id.navigation_add));

        solo.waitForActivity(AddActivity.class);

        solo.clickOnView(solo.getView(R.id.upload_photo_button));

        solo.waitForActivity(Intent.ACTION_GET_CONTENT);

        solo.sleep(500);

        solo.assertCurrentActivity("Wrong Activity", Intent.ACTION_GET_CONTENT);


    }



}
