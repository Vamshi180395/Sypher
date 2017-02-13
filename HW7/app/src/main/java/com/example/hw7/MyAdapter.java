package com.example.hw7;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Rama Vamshi Krishna on 10/19/2016.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<User> msgslist;
    List<User> completelist;
    private Context mContext;
    Uri imageuri,imageuri1;


    static HomeActivity activity;
    public MyAdapter(Context context, List<User> itemslist, HomeActivity activity) {
        msgslist = itemslist;
        mContext = context;
        this.activity= activity;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.row_layout, parent, false);
        contactView.setBackgroundColor(Color.parseColor("#F0F8FF"));
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder viewHolder, final int position) {
        Date date1 = null;
        User user = msgslist.get(position);
        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        ImageView userimg = viewHolder.userimg;
        TextView userfirstname = viewHolder.username;
        userfirstname.setText(user.getFirstlame());
        if(user.getUserimageuri().equals("android.resource://com.example.hw7/drawable/male")||user.getUserimageuri().equals("android.resource://com.example.hw7/drawable/female")){
            imageuri = Uri.parse(user.getUserimageuri());
            userimg.setImageURI(imageuri);
        }
        else {
            Picasso.with(activity).load(user.getUserimageuri()).fit().into(userimg);
        }
        userimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.goToSelectedUsersProfile(position);
            }
        });
    }
    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return msgslist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public ImageView mimg,userimg;
        MaintainData2 mdata2;
        LinearLayout layout;
        public ViewHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.userfirstname);
            userimg = (ImageView) itemView.findViewById(R.id.userimg);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    activity.openChatWindow(position);
                }
            });
        }
        public interface MaintainData2{
            public void openChatWindow(int position);
            public void commentMessage(int position);
            public void goToSelectedUsersProfile(int position);
        }

    }



}

