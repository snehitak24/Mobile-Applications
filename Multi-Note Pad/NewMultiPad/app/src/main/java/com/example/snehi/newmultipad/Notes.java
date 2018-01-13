package com.example.snehi.newmultipad;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by snehi on 2017-02-25.
 */

public class Notes implements Parcelable{

    String title;
    String notes;
    String date;
    int id=-1;

    public Notes() {

    }

    public Notes(int id,String title, String notes, String date) {
        this.id=id;
        this.title = title;
        this.date = date;
        this.notes = notes;

    }

    protected Notes(Parcel in) {
       readFromParcel(in);
    }

    public static final Creator<Notes> CREATOR = new Creator<Notes>() {
        @Override
        public Notes createFromParcel(Parcel in) {
            return new Notes(in);
        }

        @Override
        public Notes[] newArray(int size) {
            return new Notes[size];
        }
    };

    public void setId(int id){
        this.id=id;
    }

    public int getId(){
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Notes{" +
                "title='" + title + '\'' +
                ", notes='" + notes + '\'' +
                ", date='" + date + '\'' +
                '}';
    }


    public int describeContents() {
        return 0;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(notes);
        dest.writeString(date);
        dest.writeInt(id);
    }

    private void readFromParcel(Parcel in) {
        title = in.readString();
        notes = in.readString();
        date=in.readString();
        id = in.readInt();
    }
}
