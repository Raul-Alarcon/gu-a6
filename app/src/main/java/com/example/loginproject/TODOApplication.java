package com.example.loginproject;
import com.google.firebase.firestore.FirebaseFirestore;
import android.app.Application;

public class TODOApplication extends Application{
    public static FirebaseFirestore firebasestore;
    @Override
    public void onCreate(){
        super.onCreate();
        firebasestore = FirebaseFirestore.getInstance();
    }
}
