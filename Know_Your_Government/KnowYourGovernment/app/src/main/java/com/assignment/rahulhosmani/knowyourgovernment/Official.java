package com.assignment.rahulhosmani.knowyourgovernment;

import java.io.Serializable;
import java.util.Map;

public class Official  implements Serializable {
    private String name;
    private String address, city, state, zip;
    private String partyName;
    private String phoneNo;
    private String urls;
    private String email;
    private String photoUrl;
    private String officeName;
    private Map<String, String> channels;

    public Official(){}

    public Official(String name, String address, String city, String state, String zip, String partyName, String phoneNo, String urls, String email, String photoUrl, String officeName, Map<String, String> channels) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.partyName = partyName;
        this.phoneNo = phoneNo;
        this.urls = urls;
        this.email = email;
        this.photoUrl = photoUrl;
        this.officeName = officeName;
        this.channels = channels;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public Map<String, String> getChannels() {
        return channels;
    }

    public void setChannels(Map<String, String> channels) {
        this.channels = channels;
    }
}
