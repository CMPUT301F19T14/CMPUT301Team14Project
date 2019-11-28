package com.cmput301t14.mooditude.services;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.cmput301t14.mooditude.R;
import com.cmput301t14.mooditude.activities.AddActivity;
import com.cmput301t14.mooditude.activities.HomeActivity;
import com.cmput301t14.mooditude.activities.NotificationActivity;
import com.cmput301t14.mooditude.activities.SearchActivity;
import com.cmput301t14.mooditude.activities.SelfActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static com.cmput301t14.mooditude.activities.SelfActivity.EXTRA_MESSAGE_Email;


/**
 * A single class for MenuBar shared by many Activities
 */
public class MenuBar {

    private Activity activity;
    private String messageEmail;
    private int menuBarIndex;

    /**
     * constructor called by other Activities
     * @param activity - the caller activity
     * @param messageEmail - email passed in
     * @param menuBarIndex - the index of the item in the menubar passed in
     */
    public MenuBar(Activity activity, String messageEmail, int menuBarIndex){
        this.activity = activity;
        this.messageEmail = messageEmail;
        this.menuBarIndex = menuBarIndex;
        setUpMenuBar();
    }

    /**
     * setup the MenuBar to navigate between Activities
     */
    private void setUpMenuBar(){
        // set navigation menu bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView) activity.findViewById(R.id.navigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(menuBarIndex);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navigation_home:
                        Intent intent0 = new Intent(activity, HomeActivity.class);
                        finishActivityWithoutAnimation(intent0);
                        break;
                    case R.id.navigation_search:
                        Intent intent1 = new Intent(activity, SearchActivity.class);
                        finishActivityWithoutAnimation(intent1);
                        break;
                    case R.id.navigation_add:
                        Intent intent2 = new Intent(activity, AddActivity.class);
                        finishActivityWithoutAnimation(intent2);
                        break;
                    case R.id.navigation_notification:
                        Intent intent3 = new Intent(activity, NotificationActivity.class);
                        finishActivityWithoutAnimation(intent3);
                        break;
                    case R.id.navigation_self:
                        Intent intent4 = new Intent(activity, SelfActivity.class);
                        finishActivityWithoutAnimation(intent4);
                        break;
                }
                return false;

            }
        });
    }

    /**
     * Switch to new activity and closes current activity without animation
     * @param intent
     */
    private void finishActivityWithoutAnimation(Intent intent){
        Intent i = intent;
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        i.putExtra(EXTRA_MESSAGE_Email, messageEmail);
        activity.startActivity(i);
        activity.finish();
        activity.overridePendingTransition(0,0);
    }
}
