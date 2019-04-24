package com.example.aileen.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignInActivity extends AppCompatActivity {
    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth firebaseAuth;
    private SignInButton signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //get the shared instance of the FirebaseAuth object
        firebaseAuth = FirebaseAuth.getInstance();

        //configure Google sign-in
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();

        //creates a new instance of GoogleSignInClient
        googleSignInClient = GoogleSignIn.getClient(this, options);

        signInButton = findViewById(R.id.login_with_google);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        //check to see if the user is already logged in
//        if(firebaseAuth.getCurrentUser()!=null){
//            startActivity(new Intent(getApplicationContext(),RecipeMainActivity.class));
//        }
//    }

    private void signIn() {
        //gets an Intent to start the Google sign-in flow
        Intent signInIntent = googleSignInClient.getSignInIntent();
        //start Google sign-in intent. onActivityResult() will be called with the requestCode when it's finished
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //called when Google sign-in finishes
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //result returned from launching the Intent from GoogleSignInApi.getSignInIntent()
        if (requestCode == RC_SIGN_IN) {
            //returns a completed Task containing an error or GoogleSignInAccount object
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google Sign In was successful
                //returns Google account
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed
                Log.e("LOGIN", "Google sign in failed", e);
                Toast.makeText(this, "Login Unsuccessful", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account){
        String idToken = account.getIdToken();
        final String name = account.getDisplayName();

        //get Firebase credential
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        //Firebase authentication
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("SIGNIN", "signInWithCredential:onComplete:" + task.isSuccessful());
                if (task.isSuccessful()) {
                    Toast.makeText(SignInActivity.this, "Login successful " + name, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignInActivity.this, RecipeMainActivity.class);
                    startActivity(intent);
                }else {
                    Log.e("SIGNIN", "signInWithCredential" + task.getException());
                    Toast.makeText(SignInActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
