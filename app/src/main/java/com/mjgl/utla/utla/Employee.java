package com.mjgl.utla.utla;

import com.google.gson.annotations.SerializedName;

public class Employee {
    //private String firstName;
    @SerializedName("first_name")
    private String mFirstName;
    //private int age;
    @SerializedName("age")
    private int mAge;
    //private String mail;
    @SerializedName("mail")
    private String mMail;

    public Employee(String firstName, int age, String mail){
        this.mFirstName = firstName;
        this.mAge = age;
        this.mMail = mail;
    }
}
