package css.cis3334.firebaseauthentication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView textViewStatus;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonGoogleLogin;
    private Button buttonCreateLogin;
    private Button buttonSignOut;

    @Override
    //Oncreate method basically builds the entire application and the content with which the user is presented with
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewStatus = (TextView) findViewById(R.id.textViewStatus);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonGoogleLogin = (Button) findViewById(R.id.buttonGoogleLogin);
        buttonCreateLogin = (Button) findViewById(R.id.buttonCreateLogin);
        buttonSignOut = (Button) findViewById(R.id.buttonSignOut);

        // When button Login is Clicked the app receives that input that user entered. Then it calls a SignIn method.
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Log.d("CIS3334", "normal login ");
                signIn(editTextEmail.getText().toString(), editTextPassword.getText().toString());
            }
        });
        // When button Create is Clicked the app receives that input that user entered. The createAccount method is called.
        buttonCreateLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               // Log.d("CIS3334", "Create Account ");
                createAccount(editTextEmail.getText().toString(), editTextPassword.getText().toString());
            }
        });
        // When button Goggle Login the method googleSignIn is called.
        buttonGoogleLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Log.d("CIS3334", "Google login ");
                googleSignIn();
            }
        });

        //When button Sign out is Clicked the app calls the SignOut method
        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Log.d("CIS3334", "Logging out - signOut ");
                signOut();
            }
        });

        mAuth = FirebaseAuth.getInstance(); //FirebaseAuth Instance initialized
        mAuthListener = new FirebaseAuth.AuthStateListener() {  //FirebaseAuth Listener initialized


            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser(); //creates firebase object that checks the status of the user
                //the if statement checks whether the user is signed in or signed out
                //If it returns not null, the user is signed it, if the it return null then there is no user signed in
                if (user != null) {
                    // User is signed in
                    //Log.d("cis3334", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    //Log.d("cis3334", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }
    @Override
    //when user signed in
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener); //adds authorization using FirebaseAuth.AuthStateListener for the specific instance.
    }

    @Override
    //when user signed out
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener); //removes authorization using FirebaseAuth.AuthStateListener for the specific instance.
        }
    }

    //method called when a new login is being created
    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password) //the FirebaseAuth object calls method to create new login, passing the parameters.
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Log.d("cis3334", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        //if the task fails, textviewStatus updates
                        if (!task.isSuccessful()) {
                            textViewStatus.setText("Status: Authentication failed.");

                            //Toast.makeText(MainActivity.this, "Authentication failed.",
                            //Toast.LENGTH_SHORT).show();

                            //if the task succeeded, textviewStatus updates and new credentials have been created successfully
                        } else {
                            // ...
                            textViewStatus.setText("Status: Successfully Created User.");

                        }
                    }
                });
    }

    //method called when the user clicked the Login button
    private void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password) //the FirebaseAuth object calls method to retrieve existing credentials with passing the input user entered as parameters
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Log.d("cis3334", "signInWithEmail:onComplete:" + task.isSuccessful());

                        //if the task fails, textviewStatus updates
                        if (!task.isSuccessful()) {
                            textViewStatus.setText("Status: Authentication failed.");
                            // / Log.w("cis3334", "signInWithEmail", task.getException());
                            //Toast.makeText(MainActivity.this, "Authentication failed.",
                            //Toast.LENGTH_SHORT).show();

                            //if the task succeeded, textviewStatus updates and new user has been logged in successfully
                        } else {
                            textViewStatus.setText("Status: User Signed in.");
                        }
                        // ...
                    }

                });
    }

    private void signOut () {
        mAuth.signOut(); //FirebaseAuth object calls the sign out method
        textViewStatus.setText("Status: Successfully Signed out."); //updated textViewStatus

    }

    private void googleSignIn() {

    }




}
