package com.example.hw7;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirstTimeGoogle extends AppCompatActivity {
Spinner spinner;
    DatabaseReference mDatabase;
    String defaultuserprofileimg;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_google);
        spinner = (Spinner) findViewById(R.id.spinner);
        if(getIntent().getExtras()!=null) {
            user=(User) getIntent().getExtras().getSerializable("User");
        }
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void goToHomeActivity(View view) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if(spinner.getSelectedItem().toString().equals("Male")){
            defaultuserprofileimg="android.resource://com.example.hw7/drawable/male";
            user.setUsergender(spinner.getSelectedItem().toString());
            user.setUserimageuri(defaultuserprofileimg);
            mDatabase.child("users").child(user.getUserkey()).setValue(user);
            Intent i = new Intent(FirstTimeGoogle.this, HomeActivity.class);
            i.putExtra("User",user);
            startActivity(i);
            finish();
        }
        else if(spinner.getSelectedItem().toString().equals("Select a Gender")){
            Toast.makeText(FirstTimeGoogle.this, "Please select a valid gender.", Toast.LENGTH_LONG).show();
        }
        else if(spinner.getSelectedItem().toString().equals("Female")){
            defaultuserprofileimg="android.resource://com.example.hw7/drawable/female";
            user.setUsergender(spinner.getSelectedItem().toString());
            user.setUserimageuri(defaultuserprofileimg);
            mDatabase.child("users").child(user.getUserkey()).setValue(user);
            Intent i = new Intent(FirstTimeGoogle.this, HomeActivity.class);
            i.putExtra("User",user);
            startActivity(i);
            finish();
        }


    }
}
