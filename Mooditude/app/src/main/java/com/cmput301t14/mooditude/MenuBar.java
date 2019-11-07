package com.cmput301t14.mooditude;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import static com.cmput301t14.mooditude.SelfActivity.EXTRA_MESSAGE_Email;


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
                        intent0.putExtra(EXTRA_MESSAGE_Email, messageEmail);
                        intent0.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        activity.startActivity(intent0);
                        break;
                    case R.id.navigation_search:
                        Intent intent1 = new Intent(activity, SearchActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent1.putExtra(EXTRA_MESSAGE_Email, messageEmail);
                        activity.startActivity(intent1);
                        break;
                    case R.id.navigation_add:
                        Intent intent2 = new Intent(activity, AddActivity.class);
                        intent2.putExtra(EXTRA_MESSAGE_Email, messageEmail);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        activity.startActivity(intent2);
                        break;
                    case R.id.navigation_notification:
                        Intent intent3 = new Intent(activity, NotificationActivity.class);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent3.putExtra(EXTRA_MESSAGE_Email, messageEmail);
                        activity.startActivity(intent3);
                        break;
                    case R.id.navigation_self:
                        Intent intent4 = new Intent(activity, SelfActivity.class);
                        intent4.putExtra(EXTRA_MESSAGE_Email, messageEmail);
                        intent4.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        activity.startActivity(intent4);
                        break;
                }
                return false;

            }
        });
    }
}
