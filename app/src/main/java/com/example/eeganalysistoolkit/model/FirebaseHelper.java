package com.example.eeganalysistoolkit.model;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

public class FirebaseHelper {



    public Boolean sendMessageChat(Chat chat, CollectionReference dataRef){
        Map<String, Object> message = new HashMap<>();
        message.put("message", chat.getMessage());
        message.put("date", FieldValue.serverTimestamp());
        message.put("senderId", chat.getSenderId());
        message.put("receiverId", chat.getReceiverId());
        return dataRef.document().set(message).isSuccessful() ;
    }

}
