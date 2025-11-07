package com.example.ganesh1.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseUtil {

    // ✅ Return current user UID
    public static String currentUserId() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            return auth.getCurrentUser().getUid();
        } else {
            return "";
        }
    }

    // ✅ Return current user's phone number
    public static String currentUserPhone() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            return auth.getCurrentUser().getPhoneNumber();
        } else {
            return "";
        }
    }

    // ✅ SAVE USER UNDER: Users/uid/ (Realtime Database)
    public static DatabaseReference currentUserDatabaseRef() {
        FirebaseDatabase db = FirebaseDatabase.getInstance(
                "https://ganesh-4f9b0-default-rtdb.asia-southeast1.firebasedatabase.app/"
        );
        return db.getReference("Users").child(currentUserId());
    }

    // ✅ Get all users collection (Firestore)
    public static CollectionReference allUserCollectionReference() {
        return FirebaseFirestore.getInstance().collection("users");
    }
}
