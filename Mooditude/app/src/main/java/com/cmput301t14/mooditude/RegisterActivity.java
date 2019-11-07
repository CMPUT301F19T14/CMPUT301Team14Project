package com.cmput301t14.mooditude;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText emaiEditText,userNameEditText, passwordEditText,passwordConfirmEditText;
    private FirebaseAuth mFirebaseAuth;

    private String email, userName, password, confrimPassword;
    boolean userNameExist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        userNameEditText = findViewById(R.id.register_username);
        emaiEditText = findViewById(R.id.register_email);
        passwordEditText = findViewById(R.id.register_password);
        passwordConfirmEditText = findViewById(R.id.register_password_2);

        Button joinNowBtn = findViewById(R.id.register_join_now_button);

        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Users");


        joinNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emaiEditText.getText().toString();
                userName = userNameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                confrimPassword = passwordConfirmEditText.getText().toString();
                userNameExist = false;



                if (email.isEmpty()){
                    emaiEditText.setError("Please enter email!");
                    emaiEditText.requestFocus();
                }
                else if(userName.isEmpty()){
                    userNameEditText.setError("Please enter username!");
                    userNameEditText.requestFocus();
                }
                else if (password.isEmpty()){
                    passwordEditText.setError("Please enter password!");
                    passwordEditText.requestFocus();
                }
                else if (confrimPassword.isEmpty()){
                    passwordConfirmEditText.setError("Please enter password again!");
                    passwordConfirmEditText.requestFocus();
                }
                else if (!password.equals(confrimPassword)){
                    Toast.makeText(getApplicationContext(),"Passwords not equal!",Toast.LENGTH_SHORT).show();
                    passwordEditText.requestFocus();
                }
                else if (password.equals(confrimPassword)){

                    collectionReference
                            .whereEqualTo("user_name",userName)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            userNameExist = true;
                                            String dbUserName = String.valueOf(document.get("user_name"));
                                            Toast.makeText(getApplicationContext(),"User Name:"+dbUserName +", Exist: "+ userNameExist +" Success",Toast.LENGTH_SHORT).show();
                                        }
                                        if (!userNameExist){
                                            mFirebaseAuth.createUserWithEmailAndPassword(email,password)
                                                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                                            if (!task.isSuccessful()){
                                                                Toast.makeText(getApplicationContext(),"Sign Up Failed!",Toast.LENGTH_SHORT).show();
                                                            }
                                                            else {

                                                                HashMap<String,Object> data  = new HashMap<>();
                                                                List<String> empty = new ArrayList<>();
                                                                data.put("user_name",userName);
                                                                data.put("followers",empty);
                                                                data.put("following",empty);

                                                                collectionReference
                                                                        .document(email)
                                                                        .set(data)
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                Toast.makeText(getApplicationContext(),"Sign Up Success!",Toast.LENGTH_SHORT).show();
                                                                                startActivity(new Intent(RegisterActivity.this,SignInActivity.class));
                                                                            }
                                                                        })
                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Toast.makeText(getApplicationContext(),"Sign Up Failure!",Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),"Error!"+ userNameExist,Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else {
                    Toast.makeText(getApplicationContext(),"Error Occured!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
