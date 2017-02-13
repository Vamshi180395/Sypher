package com.example.hw7;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Rama Vamshi Krishna on 11/18/2016.
 */
public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.ViewHolder> {
    private List<Messages> msgslist;
    List<Messages> completelist;
    private Context mContext;
    Uri imageuri,imageuri1;
    View contactView1;
    static MyInbox activity;
    public InboxAdapter(Context context, List<Messages> itemslist, MyInbox activity) {
        msgslist = itemslist;
        mContext = context;
        this.activity= activity;
    }

    @Override
    public InboxAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.inbox_layout, parent, false);
        contactView1=contactView;
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(InboxAdapter.ViewHolder viewHolder, final int position) {
        Messages msg = msgslist.get(position);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ImageView dimg = viewHolder.dimage;
        ImageView rimg = viewHolder.rimage;
        TextView textViewtemp = viewHolder.txtmsg;
        TextView datetime = viewHolder.date;
        TextView sendername = viewHolder.sendernamee;
        ImageView msgimggg= viewHolder.msgimg;
        if(msg.message!=null){
            if(msg.message.length()<30) {
                textViewtemp.setText(msg.message);
            }
            else{
                textViewtemp.setText(msg.message.substring(0,26)+"...");
            }
        }else{
            textViewtemp.setText("");
        }
        datetime.setText(msg.getMessagedate()+"  "+msg.getMessagetime());
        imageuri = Uri.parse("android.resource://com.example.hw7/drawable/reply");
        rimg.setImageURI(imageuri);
        imageuri1 = Uri.parse("android.resource://com.example.hw7/drawable/delete");
        dimg.setImageURI(imageuri1);
        if(msg.getImageurl()!=null && msg.getImageurl().length()>5){
            msgimggg.setVisibility(View.VISIBLE);
            Picasso.with(activity).load(msg.getImageurl()).fit().into(msgimggg);
        }
        else{
            msgimggg.setVisibility(View.GONE);
        }
        sendername.setText(msg.getFromuserfirstname());
        if(msg.getMsgreadornot()==1){
         contactView1.setBackgroundColor(Color.GRAY);
        }
        dimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
activity.deleteMessage(position);
            }
        });
        rimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.replyToMessage(position);
            }
        });
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return msgslist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtmsg,sendernamee,date;
        public ImageView dimage,rimage,msgimg;
        MaintainData2 mdata2;
        public ViewHolder(View itemView) {
            super(itemView);
            txtmsg = (TextView) itemView.findViewById(R.id.itxtmsg);
            date = (TextView) itemView.findViewById(R.id.dateandtime);
            dimage = (ImageView) itemView.findViewById(R.id.ideleteimage);
            rimage = (ImageView) itemView.findViewById(R.id.ireplyimage);
            sendernamee=(TextView) itemView.findViewById(R.id.isendername);
            msgimg=(ImageView) itemView.findViewById(R.id.imageView5);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    activity.showEntireMessage(position);
                }
            });
        }
        public interface MaintainData2{
            public void deleteMessage(int position);
            public void replyToMessage(int position);
            public void  showEntireMessage(int position);
        }




    }



}

