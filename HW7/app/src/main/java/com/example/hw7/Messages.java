package com.example.hw7;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Rama Vamshi Krishna on 11/14/2016.
 */
public class Messages implements Comparable<Messages>,Serializable {
    String msgkey,message,fromuserid,imageurl,fromuserfirstname,fromuserkey,messagedate,messagetime;
    String strDate;
    Date strDate1;
    int msgreadornot;

    public int getMsgreadornot() {
        return msgreadornot;
    }

    public void setMsgreadornot(int msgreadornot) {
        this.msgreadornot = msgreadornot;
    }

    public String getStrDate() {
        //SimpleDateFormat format=new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        //return format.format(strDate);
        return this.strDate;

    }
    public void setStrDate(String strDated) throws ParseException {
        this.strDate=strDated;
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        this.strDate1=format.parse(strDated);

    }

    @Override
    public int compareTo(Messages newsItems) {
        return this.strDate1.compareTo(newsItems.strDate1);
    }

    public String getMessagedate() {
        return messagedate;
    }

    public void setMessagedate(String messagedate) {
        this.messagedate = messagedate;
    }

    public String getMessagetime() {
        return messagetime;
    }

    public void setMessagetime(String messagetime) {
        this.messagetime = messagetime;
    }

    public String getMsgkey() {
        return msgkey;
    }

    public void setMsgkey(String msgkey) {
        this.msgkey = msgkey;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFromuserid() {
        return fromuserid;
    }

    public void setFromuserid(String fromuserid) {
        this.fromuserid = fromuserid;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getFromuserfirstname() {
        return fromuserfirstname;
    }

    public void setFromuserfirstname(String fromuserfirstname) {
        this.fromuserfirstname = fromuserfirstname;
    }

    public String getFromuserkey() {
        return fromuserkey;
    }

    public void setFromuserkey(String fromuserkey) {
        this.fromuserkey = fromuserkey;
    }

    public Messages(){

    }


}
