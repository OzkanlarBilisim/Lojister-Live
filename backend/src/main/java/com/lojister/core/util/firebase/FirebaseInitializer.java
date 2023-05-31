package com.lojister.core.util.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FirebaseInitializer {

    @PostConstruct
    public void initDB() throws IOException {

        InputStream serviceAccount = this.getClass().getClassLoader()
                .getResourceAsStream("lojister-app-firebase-adminsdk-ykqp0-1f657a8db1.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://lojister-10df5-default-rtdb.firebaseio.com")
                .build();

        if (FirebaseApp.getApps().isEmpty()) {

            FirebaseApp.initializeApp(options);
        }

    }

    public Firestore getFirebase() {
        return FirestoreClient.getFirestore();
    }
}


