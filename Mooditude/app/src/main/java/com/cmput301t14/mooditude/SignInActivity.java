package com.cmput301t14.mooditude;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.cmput301t14.mooditude.SelfActivity.EXTRA_MESSAGE_Email;


/**
 * If user already has an account, he/she can
 * go to the SignIn Activity directly to log in.
 * There are two edit text fields that requires
 * user to enter the user email and password.
 */
public class SignInActivity extends AppCompatActivity {

    private EditText emaiEditText,passwordEditText;
    private Button signInBtn;
    private TextView signUpLink;
    private FirebaseAuth mFirebaseAuth;
//    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);





        mFirebaseAuth = FirebaseAuth.getInstance();
        emaiEditText = findViewById(R.id.signin_email_edit_text);
        passwordEditText = findViewById(R.id.signin_password_edit_text);
        signInBtn = findViewById(R.id.signin_sign_in_button);




// Bypass Sign in for testing
        mFirebaseAuth.signInWithEmailAndPassword("test@test.com","tester")
//        mFirebaseAuth.signInWithEmailAndPassword("test2@test.com","tester")

                .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Please Login Failed!",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Please Login Success!", Toast.LENGTH_SHORT).show();
                            Intent intentHomeActivity = new Intent(getApplicationContext(),HomeActivity.class);
                            startActivity(intentHomeActivity);
                        }
                    }
                });

// End bypass sign in for testing

        signInBtn.setOnClickListener(new View.OnClickListener() {
            /**
             * Validate the user input information. If the input
             * information is matched to the requirement, user
             * will be allowed to log in.
             * @param view
             */
            @Override
            public void onClick(View view) {
                final String email = emaiEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if (email.isEmpty()){
                    emaiEditText.setError("Please enter email!");
                    emaiEditText.requestFocus();
                }
                else if (password.isEmpty()){
                    passwordEditText.setError("Please enter password!");
                    passwordEditText.requestFocus();
                }
                else if (!(email.isEmpty() && password.isEmpty())){
                    mFirebaseAuth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(),"Login Failed!",Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), "Login Success!", Toast.LENGTH_SHORT).show();
                                        Intent intentHomeActivity = new Intent(getApplicationContext(),HomeActivity.class);
                                        intentHomeActivity.putExtra(EXTRA_MESSAGE_Email, email);
                                        startActivity(intentHomeActivity);
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
