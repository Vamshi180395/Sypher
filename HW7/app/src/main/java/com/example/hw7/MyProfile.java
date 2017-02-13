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
import java.util.UUID;

public class MyProfile extends AppCompatActivity {
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
        setContentView(R.layout.activity_my_profile);
        findViewByIds();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        childObj = mDatabase.child("users").child(user.getUid());
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
                    Picasso.with(MyProfile.this).load(me.getUserimageuri()).fit().into(edimageuri);
                }



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

    public void setProfilePicture(View view) {
        Intent i = new Intent(
                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data!=null) {
            Selectedprofilepicture = data.getData();
            edimageuri.setImageURI(Selectedprofilepicture);
            edimageuri.setDrawingCacheEnabled(true);
            edimageuri.buildDrawingCache();
            Bitmap bitmap = edimageuri.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imagedata = baos.toByteArray();
            String path = "ProfilePictures/"+ me.getUserkey()+".png";
            StorageReference myImageRef = storage.getReference(path);
            UploadTask uploadTask = myImageRef.putBytes(imagedata);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                     ppurl = taskSnapshot.getDownloadUrl();
                    Picasso.with(MyProfile.this).load(ppurl.toString()).fit().into(edimageuri);
                    changeprofilepicture.setImageURI(Uri.parse("android.resource://com.example.hw7/drawable/uploadimage"));

                }
            });



        }

    }

    public void saveChanges(View view) {
        if(edfirstname.getText().toString().length()==0 || edlastname.getText().toString().length()==0 || edgender.getText().toString().length()==0) {
            Toast.makeText(MyProfile.this, "Please enter all the fields to proceed further.",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            mDatabase = FirebaseDatabase.getInstance().getReference();
            DatabaseReference aexpref = mDatabase.child("users").child(me.getUserkey());
            me.setFirstlame(edfirstname.getText().toString());
            me.setLastname(edlastname.getText().toString());
            me.setUsergender(edgender.getText().toString());
            if(ppurl!=null) {
                me.setUserimageuri(ppurl.toString());
            }
            aexpref.setValue(me);
            Toast.makeText(MyProfile.this, "Changes saved successfully.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void finishActivity(View view) {
        finish();
    }
}
