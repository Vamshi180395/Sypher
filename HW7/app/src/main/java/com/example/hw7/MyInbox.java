package com.example.hw7;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyInbox extends AppCompatActivity implements  InboxAdapter.ViewHolder.MaintainData2{
RecyclerView inboxrecycleview;
    DatabaseReference mDatabase;
    ArrayList<Messages> inboxmessages=new ArrayList<Messages>();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    TextView emptyinbox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_inbox);
        findViewByIds();
        getSupportActionBar().setTitle("Inbox");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference aexpref1 = mDatabase.child("inbox").child(user.getUid()).getRef();
        aexpref1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        Messages msg = postSnapshot.getValue(Messages.class);
                        inboxmessages.add(msg);
                }
                setRecycleView();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        setRecycleView();
    }

    private void findViewByIds() {
        inboxrecycleview=(RecyclerView) findViewById(R.id.recycle1_view);
        emptyinbox=(TextView)  findViewById(R.id.emptyinbox);
    }
    private void setRecycleView() {
        if (inboxmessages.size() != 0 && inboxmessages != null) {
            inboxrecycleview.setVisibility(View.VISIBLE);
            emptyinbox.setVisibility(View.GONE);
            InboxAdapter adapter = new InboxAdapter(MyInbox.this, inboxmessages, MyInbox.this);
            inboxrecycleview.setAdapter(adapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            layoutManager.setSmoothScrollbarEnabled(true);
            inboxrecycleview.setLayoutManager(layoutManager);
        }
        else{
            inboxrecycleview.setVisibility(View.GONE);
            emptyinbox.setVisibility(View.VISIBLE);
            emptyinbox.setText("     No messages !! Inbox is empty..");
        }

    }

    @Override
    public void deleteMessage(int position) {
        Messages deletedmessage =inboxmessages.get(position);
        String keyformessagetobedeleted  = deletedmessage.msgkey;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        for(int i=0;i<inboxmessages.size();i++) {
            DatabaseReference aexpref = mDatabase.child("inbox").child(user.getUid()).child(inboxmessages.get(i).getFromuserkey()).child(keyformessagetobedeleted);
            if (aexpref != null) {
                aexpref.removeValue();
                inboxmessages.remove(deletedmessage);
            }

        }
        setRecycleView();
    }

    @Override
    public void replyToMessage(int position) {
        Intent i=new Intent(MyInbox.this,ChatActivity.class);
        User tuser=new User();
        tuser.setFirstlame(inboxmessages.get(position).fromuserfirstname);
        tuser.setUserkey(inboxmessages.get(position).fromuserkey);
        i.putExtra("User",tuser);
        startActivity(i);
    }

    @Override
    public void showEntireMessage(int position) {
        Messages selectedmessage=inboxmessages.get(position);
        selectedmessage.setMsgreadornot(1);
        mDatabase.child("inbox").child(user.getUid()).child(selectedmessage.fromuserkey).child(selectedmessage.getMsgkey()).setValue(selectedmessage);
        Intent i=new Intent(MyInbox.this,ShowEntireMessage.class);
        i.putExtra("SelectedMessage", selectedmessage);
        startActivity(i);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.inbox_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.Delete_All) {
            mDatabase = FirebaseDatabase.getInstance().getReference();
            DatabaseReference aexpref1 = mDatabase.child("inbox").child(user.getUid());
            if (aexpref1 != null) {
                aexpref1.removeValue();
                finish();
                Intent i = new Intent(MyInbox.this, MyInbox.class);
                startActivity(i);
            }

        }
        return true;
    }
    }
