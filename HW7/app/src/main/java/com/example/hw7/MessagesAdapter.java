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
import android.widget.RelativeLayout;
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
 * Created by Rama Vamshi Krishna on 11/18/2016.
 */
public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {
    private List<Messages> msgslist;
    List<Messages> completelist;
    private Context mContext;
    Uri imageuri,imageuri1;
    static ChatActivity activity;
    public MessagesAdapter(Context context, List<Messages> itemslist, ChatActivity activity) {
        msgslist = itemslist;
        mContext = context;
        this.activity= activity;
    }

    @Override
    public MessagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.messages_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MessagesAdapter.ViewHolder viewHolder, final int position) {
        Date date1 = null;
        Messages msg = msgslist.get(position);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ImageView dimg = viewHolder.dimage;
        TextView textViewtemp = viewHolder.txtmsg;
        TextView sendername = viewHolder.sendernamee;
        ImageView msgimggg = viewHolder.msgimg;
        textViewtemp.setText(msg.message);
        imageuri1 = Uri.parse("android.resource://com.example.inclass11/drawable/delete");
        dimg.setImageURI(imageuri1);

        if(user.getUid().equals(msg.fromuserkey)) {
            sendername.setText("- You");
        }
        else{
            sendername.setText("- "+msg.getFromuserfirstname());
        }

        if(msg.getImageurl()!=null && msg.getImageurl().length()>5){
            msgimggg.setVisibility(View.VISIBLE);
            Picasso.with(activity).load(msg.getImageurl()).fit().into(msgimggg);
        }
        else{
            msgimggg.setVisibility(View.GONE);
        }
        dimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                activity.deleteMessage(position);
            }
        });
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return msgslist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtmsg,sendernamee;
        public ImageView dimage,msgimg;
        MaintainData2 mdata2;
        LinearLayout layout;
        public ViewHolder(View itemView) {
            super(itemView);
            txtmsg = (TextView) itemView.findViewById(R.id.txtnames);
            dimage = (ImageView) itemView.findViewById(R.id.deleteimage);
            sendernamee=(TextView) itemView.findViewById(R.id.sendername);
            msgimg=(ImageView) itemView.findViewById(R.id.imageView);

        }
        public interface MaintainData2{
            public void deleteMessage(int position);

        }

    }



}

