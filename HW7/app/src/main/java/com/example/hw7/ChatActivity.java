package com.example.hw7;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity implements MessagesAdapter.ViewHolder.MaintainData2{
    private static final int GALLERY_CAPTURE = 100;
    int commentedposition = 0;
    ImageView uploadimage;
    static final int REQ_ADD = 1;
    final static String VALUE_KEY = "message";
    FirebaseStorage storage = FirebaseStorage.getInstance();
    String fromuserid, fromusername,fromuserkey;
    TextView  messagebox;
    DatabaseReference mDatabase, childObj;
    String message;
    Uri selectedImg;
    String touserkey,tousername;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseAuth mAuth;
    ArrayList<Messages> myiinboxfromspecificuser=new ArrayList<Messages>();
    String comment;
    ImageView edimageuri,dummyimg;
    Uri selectedtedmessageimg;
    static final int RESULT_LOAD_IMAGE=1;
    Uri ppurl;
    DatabaseReference aexpref1,aexpref2;
    User googleuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        findViewByIDs();
        if (getIntent().getExtras() != null) {
            User cuser = (User) getIntent().getExtras().getSerializable("User");
            googleuser = (User) getIntent().getExtras().getSerializable("GoogleUser");
           int isgoogleuser =  getIntent().getExtras().getInt("IsGoogleUser");
            touserkey=cuser.getUserkey();
            tousername=cuser.getFirstlame();
            getSupportActionBar().setTitle(tousername);
            mDatabase = FirebaseDatabase.getInstance().getReference();
            if(isgoogleuser==1 && googleuser!=null){
                childObj = mDatabase.child("users").child(googleuser.getUserkey()).child("firstlame");
            }
            else{
                childObj = mDatabase.child("users").child(user.getUid()).child("firstlame");

            }
            childObj.addValueEventListener(new ValueEventListener() {
                                                   @Override
                                                   public void onDataChange(DataSnapshot dataSnapshot) {
                                                       fromusername = dataSnapshot.getValue(String.class);
                                                   }

                                                   @Override
                                                   public void onCancelled(DatabaseError databaseError) {

                                                   }
            });

            mDatabase = FirebaseDatabase.getInstance().getReference();
            if(googleuser!=null){
                 aexpref1= mDatabase.child("inbox").child(googleuser.getUserkey()).child(touserkey);
                 aexpref2= mDatabase.child("ChatSession").child(touserkey).child(googleuser.getUserkey());
            }else{
                aexpref1= mDatabase.child("inbox").child(user.getUid()).child(touserkey);
                 aexpref2= mDatabase.child("ChatSession").child(touserkey).child(user.getUid());
            }

            aexpref2.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Messages msg = dataSnapshot.getValue(Messages.class);
                    myiinboxfromspecificuser.add(msg);
                    setRecycleView();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Messages msg = dataSnapshot.getValue(Messages.class);
                    myiinboxfromspecificuser.remove(msg);
                    setRecycleView();
                    Toast.makeText(ChatActivity.this,"Message Deleted",Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            aexpref1.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                    Messages msg = dataSnapshot.getValue(Messages.class);
                    msg.setMsgreadornot(1);
                   mDatabase.child("inbox").child(user.getUid()).child(touserkey).child(msg.getMsgkey()).setValue(msg);
                    myiinboxfromspecificuser.add(msg);
                    setRecycleView();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                    Messages msg = dataSnapshot.getValue(Messages.class);
                    myiinboxfromspecificuser.remove(msg);
                    setRecycleView();
                    Toast.makeText(ChatActivity.this,"Message Deleted",Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });

        }

        setRecycleView();
    }

    private void setRecycleView() {

        if(myiinboxfromspecificuser!=null && myiinboxfromspecificuser.size()!=0){
            Collections.sort(myiinboxfromspecificuser);
            RecyclerView messagesrecycleview=(RecyclerView) findViewById(R.id.down_recycler_view);
            messagesrecycleview.setVisibility(View.VISIBLE);
            MessagesAdapter adapter = new MessagesAdapter(this, myiinboxfromspecificuser, ChatActivity.this);
            messagesrecycleview.setAdapter(adapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            layoutManager.setSmoothScrollbarEnabled(true);
            messagesrecycleview.setLayoutManager(layoutManager);
        }
        else {
            RecyclerView messagesrecycleview=(RecyclerView) findViewById(R.id.down_recycler_view);
            messagesrecycleview.setVisibility(View.INVISIBLE);
        }
    }

    private void findViewByIDs() {
        messagebox = (TextView) findViewById(R.id.messagetosend);
        uploadimage = (ImageView) findViewById(R.id.imageView2);
        dummyimg = (ImageView) findViewById(R.id. imageView6);

    }

    public void sendMessage(View view) throws ParseException {
        String msgkey;
        DatabaseReference aexpref,chatsession;
        message = messagebox.getText().toString();
        if (message.length() == 0) {
            if (message.length() == 0) {
                setResult(RESULT_CANCELED);
                Toast.makeText(this, "Please enter a message to proceed further", Toast.LENGTH_SHORT).show();
            }
        } else {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
            String dateatwhichmessageisbeingsent= sdf.format(cal.getTime()).toString();
            String delegate = "hh:mm aaa";
          String timeatwhichmessageisbeingsent=(String) DateFormat.format(delegate,Calendar.getInstance().getTime());
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
            String date= sdf1.format(cal.getTime()).toString();
            mDatabase = FirebaseDatabase.getInstance().getReference();
            if(googleuser!=null){
                aexpref = mDatabase.child("inbox").child(touserkey).child(googleuser.getUserkey()).push();
                 msgkey = aexpref.getKey();
            }
            else{
                 aexpref = mDatabase.child("inbox").child(touserkey).child(user.getUid()).push();
                 msgkey = aexpref.getKey();
            }
            if(googleuser!=null){
               chatsession = mDatabase.child("ChatSession").child(touserkey).child(googleuser.getUserkey()).child(msgkey);
            }else{
                 chatsession = mDatabase.child("ChatSession").child(touserkey).child(user.getUid()).child(msgkey);
            }

            Messages msg=new Messages();
            msg.setFromuserfirstname(fromusername);
            msg.setFromuserid(user.getEmail());
            msg.setFromuserkey(user.getUid());
            msg.setImageurl("");
            msg.setMessage(message);
            msg.setMsgkey(msgkey);
            msg.setMessagedate(dateatwhichmessageisbeingsent);
            msg.setMessagetime(timeatwhichmessageisbeingsent);
            msg.setStrDate(date);
            msg.setMsgreadornot(0);
            aexpref.setValue(msg);

            chatsession.setValue(msg);
            messagebox.setText("");
            setRecycleView();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.chat_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DatabaseReference aexpref1,aexpref2;
        int id = item.getItemId();
        if(id == R.id.Clear_Chat){
            mDatabase = FirebaseDatabase.getInstance().getReference();
            if(googleuser!=null){
                 aexpref1= mDatabase.child("inbox").child(googleuser.getUserkey()).child(touserkey);
                 aexpref2= mDatabase.child("ChatSession").child(touserkey).child(googleuser.getUserkey());
            }
            else{
                aexpref1= mDatabase.child("inbox").child(user.getUid()).child(touserkey);
                aexpref2= mDatabase.child("ChatSession").child(touserkey).child(user.getUid());
            }
            if(aexpref1!=null){
                aexpref1.removeValue();
            }
            if(aexpref2!=null){
                aexpref2.removeValue();

            }
            finish();
            Intent i=new Intent(ChatActivity.this,ChatActivity.class);
            startActivity(i);
        }

        return true;
    }
    @Override
    public void deleteMessage(int position) {
        Messages deletedmessage =myiinboxfromspecificuser.get(position);
        myiinboxfromspecificuser.remove(deletedmessage);
        String keyformessagetobedeleted  = deletedmessage.msgkey;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference aexpref=mDatabase.child("inbox").child(user.getUid()).child(touserkey).child(keyformessagetobedeleted);
        DatabaseReference aexpref1=mDatabase.child("ChatSession").child(touserkey).child(user.getUid()).child(keyformessagetobedeleted);
        if(aexpref!=null) {
            aexpref.removeValue();
        }
        if(aexpref1!=null){
          aexpref1.removeValue();
        }
        setRecycleView();
    }

    public void selectImagetoSend(View view) {
        Intent i = new Intent(
        Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data!=null) {
            selectedtedmessageimg = data.getData();
            uploadimage.setImageURI(selectedtedmessageimg);
            uploadimage.setDrawingCacheEnabled(true);
            uploadimage.buildDrawingCache();
            Bitmap bitmap = uploadimage.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imagedata = baos.toByteArray();
            String path = "ImageMessages/"+UUID.randomUUID()+".png";
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
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                    String dateatwhichmessageisbeingsent= sdf.format(cal.getTime()).toString();
                    String delegate = "hh:mm aaa";
                    String timeatwhichmessageisbeingsent=(String) DateFormat.format(delegate,Calendar.getInstance().getTime());
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                    String date= sdf1.format(cal.getTime()).toString();
                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference aexpref = mDatabase.child("inbox").child(touserkey).child(user.getUid()).push();
                    String msgkey = aexpref.getKey();
                    DatabaseReference chatsession = mDatabase.child("ChatSession").child(touserkey).child(user.getUid()).child(msgkey);
                    Messages msg=new Messages();
                    msg.setFromuserfirstname(fromusername);
                    msg.setFromuserid(user.getEmail());
                    msg.setFromuserkey(user.getUid());
                    if(ppurl!=null) {
                        msg.setImageurl(ppurl.toString());
                    }
                    msg.setMessage(message);
                    msg.setMsgreadornot(0);
                    msg.setMsgkey(msgkey);
                    msg.setMessagedate(dateatwhichmessageisbeingsent);
                    msg.setMessagetime(timeatwhichmessageisbeingsent);
                    try {
                        msg.setStrDate(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    aexpref.setValue(msg);
                    chatsession.setValue(msg);
                    messagebox.setText("");
                    setRecycleView();
                    uploadimage.setImageURI(Uri.parse("android.resource://com.example.hw7/drawable/gallery"));
                }
            });

        }

    }
}