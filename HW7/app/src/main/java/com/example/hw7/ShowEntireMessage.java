package com.example.hw7;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ShowEntireMessage extends AppCompatActivity {
TextView sendername,message;
    ImageView replyimg,deleteimg;
    Messages selectedmessage;
    DatabaseReference mDatabase;
    ImageView msgimggg;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_entire_message);
        findViewByIds();
        if (getIntent().getExtras() != null) {
            selectedmessage=(Messages) getIntent().getExtras().getSerializable("SelectedMessage");
            if(selectedmessage.getMessage()==null){
                message.setText("");
            }
            else{
                message.setText(selectedmessage.getMessage());
            }
            if(selectedmessage.getImageurl()!=null && selectedmessage.getImageurl().length()>5){
                msgimggg.setVisibility(View.VISIBLE);
                Picasso.with(ShowEntireMessage.this).load(selectedmessage.getImageurl()).fit().into(msgimggg);
            }
            else{
                msgimggg.setVisibility(View.GONE);
            }
            sendername.setText(selectedmessage.getFromuserfirstname());

        }
        replyimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ShowEntireMessage.this,ChatActivity.class);
                User tuser=new User();
                tuser.setFirstlame(selectedmessage.fromuserfirstname);
                tuser.setUserkey(selectedmessage.fromuserkey);
                i.putExtra("User",tuser);
                startActivity(i);
            }
        });
        deleteimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyformessagetobedeleted  = selectedmessage.msgkey;
                mDatabase = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference aexpref = mDatabase.child("inbox").child(user.getUid()).child(selectedmessage.getFromuserkey()).child(keyformessagetobedeleted);
                    if (aexpref != null) {
                        aexpref.removeValue();
                        finish();
                        Intent i=new Intent(ShowEntireMessage.this,MyInbox.class);
                        startActivity(i);
                    }
                }
        });
    }

    private void findViewByIds() {
        sendername=(TextView) findViewById(R.id.textView8);
        message=(TextView) findViewById(R.id.textView7);
        replyimg=(ImageView)findViewById(R.id.imageView4);
        deleteimg=(ImageView)findViewById(R.id.imageView3);
        msgimggg=(ImageView)findViewById(R.id.bigimage);
    }
}
