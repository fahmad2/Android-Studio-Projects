package com.example.urfi.know_your_government;

import java.io.Serializable;

/**
 * Created by urfi on 4/16/17.
 */

public class Official implements Serializable {
    protected String name;
//    protected String designation;
    protected PoliticalParty party;
    protected String office;
    protected String address;
    protected String phone;
    protected String email;
    protected String website;
    protected String gPlus;
    protected String faceb;
    protected String twit;
    protected String yout;
    protected String photo;

    public Official(String n, String o){
        this.name = n;
        this.office = o;
//        this.designation = d;
//        this.party = p;
//        this.address = a;
//        this.phone = ph;
//        this.email = em;
//        this.website = web;
    }

    //accessors

    public String getName(){ return this.name; }

//    public String getDesignation(){ return this.designation; }

    public PoliticalParty getParty (){ return this.party; }

    public String getOffice (){ return this.office; }

    public String getAddress (){ return this.address; }

    public String getPhone (){ return this.phone; }

    public String getEmail (){ return this.email; }

    public String getWebsite (){ return this.website; }

    public String getgPlus(){ return this.gPlus; }

    public String getFaceb(){ return this.faceb; }

    public String getTwit(){ return this.twit; }

    public String getYout(){ return this.yout; }

    public String getPhoto(){ return this.photo; }

    //modifiers

    public void setName(String n){ this.name = n; }

//    public void setDesignation(String d){ this.designation = d; }

    public void setParty(PoliticalParty p){ this.party = p; }

    public void setOffice(String x){ this.office = x; }

    public void setAddress(String x){ this.address = x; }

    public void setPhone(String x){ this.phone = x; }

    public void setEmail(String x){ this.email = x; }

    public void setWebsite(String x){ this.website = x; }

    public void setgPlus(String x){ this.gPlus = x; }

    public void setFaceb(String x){ this.faceb = x; }

    public void setTwit(String x){ this.twit = x; }

    public void setYout(String x){ this.yout = x; }

    public void setPhoto(String x){ this.photo = x; }

    public enum PoliticalParty {
        DEMOCRATIC, REPUBLICAN, NEITHER
    }
}