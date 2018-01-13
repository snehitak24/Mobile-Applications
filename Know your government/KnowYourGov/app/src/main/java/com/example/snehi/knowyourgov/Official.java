package com.example.snehi.knowyourgov;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by snehi on 2017-04-06.
 */

public class Official implements Serializable {

    String officialName;
    String address;
    String party;
    String phone[];
    String url[];
    String email[];
    String photoUrl;
    String channels[][];

    public Official(String officialName, String official) {
        this.officialName = officialName;       
    }

    public Official(String officialName, String address, String party, String phone[], String url[], String email[], String photoUrl, String[][] channels) {
        this.officialName = officialName;
        this.address = address;
        this.party = party;
        this.phone = phone;
        this.url = url;
        this.email = email;
        this.photoUrl = photoUrl;
        this.channels = channels;
    }

    public Official() {

    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String[] getPhone() {
        return phone;
    }

    public void setPhone(String phone[]) {
        this.phone = phone;
    }

    public String[] getUrl() {
        return url;
    }

    public void setUrl(String url[]) {
        this.url = url;
    }

    public String[] getEmail() {
        return email;
    }

    public void setEmail(String email[]) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String[][] getChannels() {
        return channels;
    }

    public void setChannels(String[][] channels) {
        this.channels = channels;
    }



    public String getOfficialName() {
        return officialName;
    }

    public void setOfficialName(String officialName) {
        this.officialName = officialName;
    }

    @Override
    public String toString() {
        return "Official{" +
                "officialName='" + officialName + '\'' +
                ", address='" + address + '\'' +
                ", party='" + party + '\'' +
                ", phone='" + Arrays.toString(phone) + '\'' +
                ", url='" + Arrays.toString(url) + '\'' +
                ", email='" + Arrays.toString(email) + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", channels=" + Arrays.toString(channels) +
                '}';
    }
}
