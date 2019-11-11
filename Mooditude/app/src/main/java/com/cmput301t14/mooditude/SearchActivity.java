package com.cmput301t14.mooditude;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
 */
public class SearchActivity extends AppCompatActivity {

    private EditText searchEditTextView;
    private RecyclerView recyclerView;
    //    private FirebaseAuth mFirebaseAuth;
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

        searchEditTextView = findViewById(R.id.search_edit_text);
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
             * @param editable
             */
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()) {
                    userNameList.clear();
                    userEmailList.clear();
                    recyclerView.removeAllViews();
                } else {
                    setAdapter(editable.toString(), usersCollection);
                }
            }
        });


    }

    /**
     * Get the user names and user emails from the firestore firebase.
     * Check those user names or user emails whether contain the
     * searched string. And the maximum size for the adapter is
     * 15.
     *
     * @param searchedString
     * @param collectionReference
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
                    // Why?   -- Enson
                    if (counter == 15) {
                        break;
                    }
                }

                searchAdapter = new SearchAdapter(SearchActivity.this, userNameList, userEmailList);
                recyclerView.setAdapter(searchAdapter);
            }
        });

    }

}
