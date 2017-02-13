package com.example.hw7;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;
import java.util.Set;

public class HomeActivity extends AppCompatActivity implements MyAdapter.ViewHolder.MaintainData2 {
FirebaseAuth mAuth;
    RecyclerView userrecycleview;
    TextView welcomemsg;
    String currentusername;
    String currentuserid;
    DatabaseReference mDatabase,childObj;
    ImageView inboximage;
    ArrayList<User> alluserslist=new ArrayList<User>();
User googleuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findViewByIds();
        getSupportActionBar().setTitle("Home");
        if (getIntent().getExtras() != null) {
            googleuser= (User) getIntent().getExtras().getSerializable("User");
            currentuserid = getIntent().getExtras().getString("UserID");
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            mDatabase = FirebaseDatabase.getInstance().getReference();
            if(googleuser!=null){
                childObj= mDatabase.child("users").child(googleuser.getUserkey()).child("firstlame");
            }
            else{
                childObj = mDatabase.child("users").child(user.getUid()).child("firstlame");
            }
if(currentuserid==null){
    currentuserid= googleuser.getUserkey();
}
            childObj.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    currentusername = dataSnapshot.getValue(String.class);
                    welcomemsg.setText("Hi " + currentusername);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            DatabaseReference aexpref1= mDatabase.child("users");
            aexpref1.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                    User user = dataSnapshot.getValue(User.class);
                    alluserslist.add(user);
                    setRecycleView();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
setRecycleView();
        }
        inboximage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(HomeActivity.this,MyInbox.class);
                i.putExtra("AllUsers",alluserslist);
                startActivity(i);
            }
        });
    }

    private void setRecycleView() {
if(alluserslist.size()!=0 && alluserslist!=null){
    for(int i=0; i<alluserslist.size();i++){
        if(alluserslist.get(i).userkey.equals(currentuserid)){
            alluserslist.remove(i);
        }
    }
    MyAdapter adapter = new MyAdapter(this, alluserslist,HomeActivity.this);
    userrecycleview.setAdapter(adapter);
    LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    layoutManager.setSmoothScrollbarEnabled(true);
    userrecycleview.setLayoutManager(layoutManager);
}

    }

    private void findViewByIds() {
        userrecycleview=(RecyclerView) findViewById(R.id.recycle_view);
        welcomemsg=(TextView) findViewById(R.id.userwelcomemsg);
        inboximage=(ImageView) findViewById(R.id.inboximage);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
      if(id == R.id.Edit_Profile){
            Intent i=new Intent(HomeActivity.this,MyProfile.class);
            startActivity(i);
        }
        else if(id == R.id.Log_Out){
          mAuth = FirebaseAuth.getInstance();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Intent i=new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
            else if(mAuth!=null){
                {
                    mAuth.signOut();
                    finish();
                }
            }else {
                // No user is signed in
            }
        }

        return true;
    }

    @Override
    public void openChatWindow(int position) {
        Intent i=new Intent(HomeActivity.this,ChatActivity.class);
        if(googleuser!=null){
            i.putExtra("User",alluserslist.get(position));
            i.putExtra("GoogleUser",googleuser);
            i.putExtra("IsGoogleUser",1);
        }else{
            i.putExtra("User",alluserslist.get(position));
        }

        startActivity(i);
    }

    @Override
    public void commentMessage(int position) {

    }

    @Override
    public void goToSelectedUsersProfile(int position) {
        Intent i=new Intent(HomeActivity.this,ViewProfile.class);
    i.putExtra("User",alluserslist.get(position));
    startActivity(i);
}

}
