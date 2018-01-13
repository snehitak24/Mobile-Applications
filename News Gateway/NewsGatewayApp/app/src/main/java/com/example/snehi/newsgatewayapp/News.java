package com.example.snehi.newsgatewayapp;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;

/**
 * Created by snehi on 2017-05-04.
 */

public class News implements Serializable,Parcelable{

    String author;
    String title;
    String description;
    String date;
    String url;
    String extraURL;

    protected News(Parcel in) {
        author = in.readString();
        title = in.readString();
        description = in.readString();
        url = in.readString();
        date=in.readString();
        extraURL=in.readString();
    }

    public News(){}

    public News(String author, String title, String description, String url, String date, String extraURL) {
        this.author = author;
        this.title = title;
        this.description = description;
        this.url=url;
        this.date = date;
        this.extraURL=extraURL;
    }

    public String getExtraURL() {
        return extraURL;
    }

    public void setExtraURL(String extraURL) {
        this.extraURL = extraURL;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public static final Creator<News> CREATOR = new Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(url);
        dest.writeString(date);
        dest.writeString(extraURL);
    }
}
