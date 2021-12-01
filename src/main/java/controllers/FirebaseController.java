package controllers;

import java.io.FileInputStream;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.cloud.StorageClient;

public class FirebaseController {
	
	private static FirebaseApp app;
	private static FirebaseAuth auth;
	private static Firestore firestore;
	private static Bucket storage;
	
	private static FirebaseApp initiateFirebaseApp() throws Exception {
		
		FileInputStream serviceAccount = new FileInputStream("E:\\Study\\IT\\NetworkDeveloping\\Render CV\\src\\main\\serviceAccountKey.json");
		
		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.setStorageBucket("convert-cv.appspot.com")
				.build();
		
		app = FirebaseApp.initializeApp(options);
		return app;
	}
	
	public static FirebaseApp getFirebaseApp() throws Exception {
		if (app != null)
			return app;
		
		return initiateFirebaseApp();
	}

	private static FirebaseAuth initiateAuth() throws Exception {
		FirebaseApp app = FirebaseController.getFirebaseApp();
		auth = FirebaseAuth.getInstance(app);
		return auth;
	}
	
	public static FirebaseAuth getAuth() throws Exception {
		if (auth != null) return auth;
		
		return initiateAuth();
	}
	
	private static Firestore initiateFirestore() throws Exception {
		FirebaseApp app = FirebaseController.getFirebaseApp();
		firestore = FirestoreClient.getFirestore(app);
		return firestore;
	}
	
	public static Firestore getFirestore() throws Exception {
		if (firestore != null) return firestore;
		
		return initiateFirestore();
	}
	
	private static Bucket initiateStorage() throws Exception {
		FirebaseApp app = FirebaseController.getFirebaseApp();
		storage = StorageClient.getInstance(app).bucket();
		
		return storage;
	}
	
	public static Bucket getStorage() throws Exception {
		if (storage != null) return storage;
		
		return initiateStorage();
	}
}
