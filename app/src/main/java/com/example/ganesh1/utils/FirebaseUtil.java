package com.example.ganesh1.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FirebaseUtil {

    // ✅ Return current user UID

    // ✅ Return current user's phone number
    public static String currentUserPhone() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return (user != null) ? user.getPhoneNumber() : null;
    }

    // ✅ SAVE USER UNDER: Users/uid/
    public static DatabaseReference currentUserDatabaseRef() {

        // ✅ IMPORTANT: Your correct Realtime DB URL
        FirebaseDatabase db = FirebaseDatabase.getInstance(
                "https://ganesh-4f9b0-default-rtdb.asia-southeast1.firebasedatabase.app/"
        );

        return db.getReference("Users").child(currentUserId());
    }


    public static CollectionReference allUserCollectionReference() {
        return FirebaseFirestore.getInstance().collection("users");
    }

    public static String currentUserId() {
        return FirebaseAuth.getInstance().getUid();
    }
}
