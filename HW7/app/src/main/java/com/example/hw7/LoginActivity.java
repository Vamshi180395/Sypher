package com.example.hw7;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements  GoogleApiClient.OnConnectionFailedListener{
    EditText useremail, userpassword;
    private FirebaseAuth mAuth;
    GoogleApiClient mgoogleclientapi;
    public static final int RC_SIGN_IN=9001;
    DatabaseReference mDatabase;
    SignInButton googlesigninbutton;
    String check1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login");
        googlesigninbutton= (SignInButton) findViewById(R.id.googlesignin);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mgoogleclientapi=new GoogleApiClient.Builder(this).enableAutoManage(this, LoginActivity.this).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();
        if (user != null) {
            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
            i.putExtra("UserID", user.getUid().toString());
            startActivity(i);
            finish();
        } else {
            findViewByIds();
        }
        googlesigninbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signinintent = Auth.GoogleSignInApi.getSignInIntent(mgoogleclientapi);
                startActivityForResult(signinintent, RC_SIGN_IN);
            }
        });
    }

    private void findViewByIds() {
        useremail = (EditText) findViewById(R.id.txtboxemail);
        userpassword = (EditText) findViewById(R.id.txtboxpassword);
    }

    public void goToRegisterActivity(View view) {
        Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(i);
        finish();
    }

    public void authenticateUserandgoToExpenseActivity(View view) {
        if (useremail.getText().toString().length() == 0 || userpassword.getText().toString().length() == 0) {
            Toast.makeText(LoginActivity.this, "Please enter all the fields to proceed further.",
                    Toast.LENGTH_SHORT).show();
        } else {
            mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword(useremail.getText().toString(), userpassword.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("Demo", "signInWithEmail:onComplete:" + task.isSuccessful());
                            if (!task.isSuccessful()) {
                                Log.w("Warning", "signInWithEmail:failed", task.getException());
                                Toast.makeText(LoginActivity.this, "Invalid Credentials. Login unsuccessfull!!!",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                i.putExtra("UserID", mAuth.getCurrentUser().getUid().toString());
                                startActivity(i);
                                finish();
                            }

                        }
                    });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSIgnInResult(result);
        }
    }

    private void handleSIgnInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            mDatabase = FirebaseDatabase.getInstance().getReference();
            DatabaseReference ref1= mDatabase.child("users").child(account.getId());
            ref1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User usert = dataSnapshot.getValue(User.class);
                    check1=usert.getUsergender();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            if(check1!=null){
                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                i.putExtra("UserID", account.getId());
                startActivity(i);
            }
            else{
                User user = new User(account.getDisplayName(),account.getFamilyName().toString(),account.getEmail().toString(),"",account.getId(),account.getId(),"");
                mDatabase.child("users").child(account.getId()).setValue(user);
                Intent i = new Intent(LoginActivity.this, FirstTimeGoogle.class);
                i.putExtra("User",user);
                startActivity(i);
            }

        } else {

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
