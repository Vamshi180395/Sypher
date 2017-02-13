package com.example.hw7;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    EditText userfirstname,userlastname, useremail,userpassword,userconfirmpassword;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    Spinner spinner;
    String defaultuserprofileimg;
// ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("Sign Up");
        findViewByIds();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void signUpUser(View view) {
        if(useremail.getText().toString().length()==0 || userpassword.getText().toString().length()==0||userfirstname.getText().length()==0 ||userconfirmpassword.getText().length()==0 ||userlastname.getText().length()==0){
            Toast.makeText(RegisterActivity.this, "Please enter all the fields to proceed further.",
                    Toast.LENGTH_SHORT).show();
        }else {
            if(spinner.getSelectedItem().toString().equals("Select a Gender")){
                Toast.makeText(RegisterActivity.this, "Please select a valid gender.", Toast.LENGTH_LONG).show();
            }
           else {
            mAuth = FirebaseAuth.getInstance();
            mAuth.createUserWithEmailAndPassword(useremail.getText().toString(), userpassword.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("demo", "createUserWithEmail:onComplete:" + task.isSuccessful());

                            if (!task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, task.getException().toString(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                mDatabase = FirebaseDatabase.getInstance().getReference();
                                if (spinner.getSelectedItem().toString().equals("Male")) {
                                    defaultuserprofileimg = "android.resource://com.example.hw7/drawable/male";
                                } else if (spinner.getSelectedItem().toString().equals("Female")) {
                                    defaultuserprofileimg = "android.resource://com.example.hw7/drawable/female";
                                }
                                User user = new User(userfirstname.getText().toString(), userlastname.getText().toString(), useremail.getText().toString(), spinner.getSelectedItem().toString(), userpassword.getText().toString(), task.getResult().getUser().getUid().toString(), defaultuserprofileimg);
                                mDatabase.child("users").child(task.getResult().getUser().getUid().toString()).setValue(user);
                                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(i);
                                Toast.makeText(RegisterActivity.this, "Succesfully registered. Please login with the created credentials",
                                        Toast.LENGTH_LONG).show();
                                mAuth = FirebaseAuth.getInstance();
                                mAuth.signOut();
                                finish();
                            }

                        }

                    });
        }
        }

    }

    public void goToLogin(View view) {
        Intent i=new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(i);
        finish();

    }
    private void findViewByIds() {
        userfirstname= (EditText)findViewById(R.id.txtboxuserfirstname);
        userlastname= (EditText) findViewById(R.id.txtboxuserlastname);
        userpassword= (EditText) findViewById(R.id.txtboxuserpassword);
        userconfirmpassword= (EditText)findViewById(R.id.txtboxuserconfirmpassword);
        useremail= (EditText) findViewById(R.id.txtboxuseremail);
        spinner = (Spinner) findViewById(R.id.spinner);

    }
}
