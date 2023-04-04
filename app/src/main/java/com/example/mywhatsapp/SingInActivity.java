package com.example.mywhatsapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mywhatsapp.databinding.ActivitySignUpBinding;
import com.example.mywhatsapp.databinding.ActivitySingInBinding;
import com.example.mywhatsapp.model.Users;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class SingInActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    ActivitySingInBinding binding;
    ProgressDialog progressDialog;
    GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivitySingInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Log in");
        progressDialog.setMessage("Logging you in");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(SingInActivity.this, gso);
        if (mAuth.getCurrentUser()!=null){ //if some user is logged in
            Intent intent = new Intent(SingInActivity.this, MainActivity.class);
            startActivity(intent);
        }
        binding.btnSignUpSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String email = binding.etEmailSignIn.getText().toString();
                String password = binding.etPasswordSignIn.getText().toString();
                mAuth.signInWithEmailAndPassword(email, password) //email and password are stored in Firebase through this. Ab store hone ke baad kya karna hai vo "addOnCompleteListener()" me likhna hai
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(SingInActivity.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(SingInActivity.this, task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        binding.tvSignInSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SingInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        binding.btnGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }
    private void signIn() { //call this function through a button
        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent, 65); //This function is used when we have to get some data(email and password in this case) from another activity and not tranfer the data to it. Passing 65 cuz it's used in onActivityResult().
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        //Result returned from launching the Intent from GoogleSinInApi.getSingInIntent(...);
        if (requestCode==65){//we can write any number between 10 and 99 here
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data); //"task" is an object of "GoogleSignInAccount" type, which represents the activity(we got new info of an account in this case) that just happened.
            try {
                //Google sign in was successful, authenticate with firebase
                GoogleSignInAccount account = task.getResult(ApiException.class); //Generally object of "Task" is usually used in combination of '.getResult()'
                Log.d(TAG, "firebaseAuthWithGoogle"+ account.getId());
                firebaseAuthWithGoogle(account.getIdToken()); //it's user defined function, through which we'll authenticate ki jo user mila hai vo sahi hai ki nai
            } catch (ApiException e) {
                Log.w(TAG,"Google sign in failed", e);
            }
        }
    }
    public void firebaseAuthWithGoogle(String idToken){ //defining this function is very similar to "signInWithEmailAndPassword()" above. If
    AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
    mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()){
                //Sign-in is success, update UI with the signed-in user's information
                Log.d(TAG, "signInWithCredential:Success");
                FirebaseUser user= mAuth.getCurrentUser();
                Users users = new Users();
                users.setUserId(user.getUid());
                users.setUserName(user.getDisplayName());
                users.setProfilePic(user.getPhotoUrl().toString());
                database.getReference().child("Users").child(user.getUid()).setValue(users);
                Intent intent = new Intent(SingInActivity.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(SingInActivity.this, "Credentials stored and signed in with Google",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(SingInActivity.this, task.getException().getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    });
    }
}