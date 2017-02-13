package com.example.hw7;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class ViewProfile extends AppCompatActivity {
    DatabaseReference mDatabase,childObj;
    String myfirstname,mylastname,gender,myimguri;
    EditText edfirstname,edlastname,edgender;
    ImageView edimageuri;
    Uri Selectedprofilepicture;
    static final int RESULT_LOAD_IMAGE=1;
    ImageView changeprofilepicture;
    FirebaseStorage storage=FirebaseStorage.getInstance();
    User me;
    Uri ppurl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        findViewByIds();

        User user = (User) getIntent().getExtras().getSerializable("User");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        childObj = mDatabase.child("users").child(user.getUserkey());
        childObj.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                me = dataSnapshot.getValue(User.class);
                myfirstname= me.getFirstlame();
                gender= me.getUsergender();
                mylastname= me.getLastname();
                myimguri=me.getUserimageuri();
                edfirstname.setText(myfirstname);
                edlastname.setText(mylastname);
                edgender.setText(gender);
                if(me.getUserimageuri().equals("android.resource://com.example.hw7/drawable/male")||me.getUserimageuri().equals("android.resource://com.example.hw7/drawable/female")){
                    Uri imageuri = Uri.parse(me.getUserimageuri());
                    edimageuri.setImageURI(imageuri);
                }
                else {
                    Picasso.with(ViewProfile.this).load(me.getUserimageuri()).fit().into(edimageuri);
                }

edfirstname.setEnabled(false);
                edlastname.setEnabled(false);
                edgender.setEnabled(false);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void findViewByIds() {
        edfirstname=(EditText) findViewById(R.id.txtboxeditfirstmame);
        edlastname=(EditText) findViewById(R.id.txtboxeditlastmame);
        edgender=(EditText) findViewById(R.id.txtboxeditgender);
        edimageuri=(ImageView) findViewById(R.id.imgeditprofilepicture);
        changeprofilepicture=(ImageView) findViewById(R.id.changeprofilepicture);


    }



    public void finishActivity(View view) {
        finish();
    }
}

