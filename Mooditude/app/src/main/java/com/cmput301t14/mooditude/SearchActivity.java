package com.cmput301t14.mooditude;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.cmput301t14.mooditude.SelfActivity.EXTRA_MESSAGE_Email;


/**
 * User can search other users in the Search Activity
 * by entering others' user names or user e-mails.
 */
public class SearchActivity extends AppCompatActivity {

    private EditText searchField;
    private RecyclerView resultList;
    private FirebaseAuth mFirebaseAuth;
    ArrayList<String> userNameList;
    ArrayList<String> userEmailList;
    SearchAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        searchField = findViewById(R.id.search_edit_text);
        resultList = findViewById(R.id.search_list);
        final CollectionReference collectionReference = db.collection("Users");

        resultList.setHasFixedSize(true);
        resultList.setLayoutManager(new LinearLayoutManager(this));
        resultList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


        userEmailList = new ArrayList<>();
        userNameList = new ArrayList<>();

        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            /**
             * check the search text field whether is changed,
             * and clear the lists after finishing every search.
             * @param editable
             */
            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().isEmpty()){
                    setAdapter(editable.toString(), collectionReference);
                } else {
                    userNameList.clear();
                    userEmailList.clear();
                    resultList.removeAllViews();
                }
            }
        });

        Intent intent = getIntent();
        final String messageEmail = intent.getStringExtra(SelfActivity.EXTRA_MESSAGE_Email);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            /**
             * switch among home, search, self, add and notification activities
             */
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navigation_home:
                        Intent intent0 = new Intent(SearchActivity.this, HomeActivity.class);
                        intent0.putExtra(EXTRA_MESSAGE_Email, messageEmail);
                        intent0.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent0);
                        break;
                    case R.id.navigation_search:

                        break;
                    case R.id.navigation_add:
                        Intent intent2 = new Intent(SearchActivity.this, AddActivity.class);
                        intent2.putExtra(EXTRA_MESSAGE_Email, messageEmail);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent2);
                        break;
                    case R.id.navigation_notification:
                        Intent intent3 = new Intent(SearchActivity.this, NotificationActivity.class);
                        intent3.putExtra(EXTRA_MESSAGE_Email, messageEmail);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent3);
                        break;
                    case R.id.navigation_self:
                        Intent intent4 = new Intent(SearchActivity.this, SelfActivity.class);
                        intent4.putExtra(EXTRA_MESSAGE_Email, messageEmail);
                        intent4.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent4);
                        break;
                }
                return false;

            }
        });
    }

    /**
     * Get the user names and user emails from the firestore firebase.
     * Check those user names or user emails whether contain the
     * searched string. And the maximum size for the adapter is
     * 15.
     * @param searchedString
     * @param collectionReference
     */
    private void setAdapter(final String searchedString, CollectionReference collectionReference){
        userNameList.clear();
        userEmailList.clear();
        resultList.removeAllViews();
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                int counter = 0;
                for (QueryDocumentSnapshot doc: queryDocumentSnapshots){
                    String user_name = String.valueOf(doc.getData().get("user_name"));
                    String user_email = doc.getId();
                    if(user_name.contains(searchedString)){
                        userNameList.add(user_name);
                        userEmailList.add(user_email);
                        counter++;
                    } else if (user_email.contains(searchedString)){
                        userNameList.add(user_name);
                        userEmailList.add(user_email);
                        counter++;
                    }

                    if(counter == 15) {
                        break;
                    }
                }
                searchAdapter = new SearchAdapter(SearchActivity.this, userNameList, userEmailList);
                resultList.setAdapter(searchAdapter);
            }
        });

    }

}
