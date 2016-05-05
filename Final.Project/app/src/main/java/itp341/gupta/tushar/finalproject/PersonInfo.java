package itp341.gupta.tushar.finalproject;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Tushar on 5/5/2016.
 */
public class PersonInfo implements Serializable {
    private int id;
    private String name;
    private Date dob;
    private String email;
    private String mentorfor;
    private String learn;
    private String about;
    private String []pictures;
    private boolean imageset;

    public PersonInfo(){
        setImageset(false);
        pictures = new String[4];
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMentorfor() {
        return mentorfor;
    }

    public void setMentorfor(String mentorfor) {
        this.mentorfor = mentorfor;
    }

    public String getLearn() {
        return learn;
    }

    public void setLearn(String learn) {
        this.learn = learn;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String[] getPictures() {
        return pictures;
    }

    public void setPictures(String[] pictures) {
        this.pictures = pictures;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isImageset() {
        return imageset;
    }

    public void setImageset(boolean imageset) {
        this.imageset = imageset;
    }
}
