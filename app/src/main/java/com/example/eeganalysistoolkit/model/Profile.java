package com.example.eeganalysistoolkit.model;

public class Profile {
    private String id;
    private String firstName;
    private String lastName;
    private String age;
    private String city;
    private String email;
    private String mobilePhone;
    private String socialNumberId;
    private String gender;
    private String country;
    private String userType;
    private boolean approvedUser;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstaname(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getSocialNumberId() {
        return socialNumberId;
    }

    public void setSocialNumberId(String socialNumberId) {
        this.socialNumberId = socialNumberId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public boolean isApprovedUser() {
        return approvedUser;
    }

    public void setApprovedUser(boolean approvedUser) {
        this.approvedUser = approvedUser;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

}
