package com.cmput301t14.mooditude.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301t14.mooditude.services.MenuBar;
import com.cmput301t14.mooditude.R;
import com.cmput301t14.mooditude.adapters.SearchAdapter;
import com.cmput301t14.mooditude.services.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


/**
 * User can search other users in the Search Activity
 * by entering others' user names or user e-mails.
 * And user can follow or unfollow other users.
 */
public class SearchActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private ArrayList<String> userNameList;
    private ArrayList<String> userEmailList;
    private SearchAdapter searchAdapter;

    private User user;
    private CollectionReference usersCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        user = new User();
        MenuBar menuBar = new MenuBar(SearchActivity.this, user.getEmail(), 1);
        userEmailList = new ArrayList<>();
        userNameList = new ArrayList<>();

        usersCollection = FirebaseFirestore.getInstance().collection("Users");


        user.listenFollower(User.followerList);
        user.listenFollowing(User.followingList);

        EditText searchEditTextView = findViewById(R.id.search_edit_text);
        recyclerView = findViewById(R.id.search_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


        searchEditTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            /**
             * check the search text field whether is changed,
             * and clear the lists after finishing every search.
             * @param editable - check whether the mood event can be edited
             */
            @Override
            public void afterTextChanged(Editable editable) {
                userNameList.clear();
                userEmailList.clear();
                recyclerView.removeAllViews();
                if (!editable.toString().isEmpty()) {
                    setAdapter(editable.toString(), usersCollection);
                }
                if (!User.followerList.isEmpty())
                    Log.i("TAGBB", User.followerList.toString());
            }
        });


    }

    /**
     * Get the user names and user emails from the firestore firebase.
     * Check those user names or user emails whether contain the
     * searched string. And the maximum size for the adapter is
     * 15.
     *
     * @param searchedString - the string user wants to search
     * @param collectionReference -
     */
    private void setAdapter(final String searchedString, CollectionReference collectionReference) {
        userNameList.clear();
        userEmailList.clear();
        recyclerView.removeAllViews();

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                int counter = 0;
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    String user_name = String.valueOf(doc.getData().get("user_name"));
                    String user_email = doc.getId();
                    if (user_name.contains(searchedString)) {
                        userNameList.add(user_name);
                        userEmailList.add(user_email);
                        counter++;
                    } else if (user_email.contains(searchedString)) {
                        userNameList.add(user_name);
                        userEmailList.add(user_email);
                        counter++;
                    }
                    if (counter == 15) {
                        break;
                    }
                }
                searchAdapter = new SearchAdapter(SearchActivity.this, userNameList, userEmailList,User.followerList,User.followingList);
                user.notifyFollowFollowingDateChange(searchAdapter);
                recyclerView.setAdapter(searchAdapter);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

}
