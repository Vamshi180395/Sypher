package com.example.hw7;

import java.io.Serializable;

/**
 * Created by Rama Vamshi Krishna on 10/31/2016.
 */
public class User implements Serializable {
    String firstlame,lastname,useremail,usergender,userpassword,userkey,userimageuri;

    public String getFirstlame() {
        return firstlame;
    }

    public void setFirstlame(String firstlame) {
        this.firstlame = firstlame;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getUsergender() {
        return usergender;
    }

    public void setUsergender(String usergender) {
        this.usergender = usergender;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    public String getUserkey() {
        return userkey;
    }

    public void setUserkey(String userkey) {
        this.userkey = userkey;
    }

    public String getUserimageuri() {
        return userimageuri;
    }

    public void setUserimageuri(String userimageuri) {
        this.userimageuri = userimageuri;
    }

    public User(String firstlame, String lastname, String useremail, String usergender, String userpassword, String userkey, String userimageuri) {

        this.firstlame = firstlame;
        this.lastname = lastname;
        this.useremail = useremail;
        this.usergender = usergender;
        this.userpassword = userpassword;
        this.userkey=userkey;
        this.userimageuri=userimageuri;
    }

    public User(){

    }

}

