package com.example.eeganalysistoolkit.model;

import com.google.firebase.firestore.FieldValue;

import java.util.Date;

public class Conversation {
    private String convId;
    private String userOn;
    private String userTwo;
    private Date lastUpdate;

    public Conversation() {
    }

    public Conversation(String convId, String userOn, String userTwo) {
        this.convId = convId;
        this.userOn = userOn;
        this.userTwo = userTwo;
        this.lastUpdate = new Date();
    }
}
