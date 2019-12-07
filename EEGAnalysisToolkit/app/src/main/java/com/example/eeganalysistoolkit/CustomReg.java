package com.example.eeganalysistoolkit;

public class CustomReg {
    private String name, gender, emailAddress;
    private int age;

    public CustomReg(String name, String gender, String emailAddress, int age) {
        this.name = name;
        this.gender = gender;
        this.emailAddress = emailAddress;
        this.age = age;
    }

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getGender() {return gender;}
    public void setGender(String gender) {this.gender = gender;}

    public String getEmailAddress() {return emailAddress;}
    public void setEmailAddress(String emailAddress) {this.emailAddress = emailAddress;}

    public int getAge() {return age;}
    public void setAge(int age) {this.age = age;}
}
