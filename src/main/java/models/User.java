package models;

import java.util.ArrayList;

import com.google.firebase.auth.FirebaseToken;

import controllers.AuthenticationController;

public class User {
	public FirebaseToken token;
	public ArrayList<Document> documents;
	public String email;
	
	public User(String idToken) throws Exception {
		token = AuthenticationController.verifyIdToken(idToken);
		email = token.getEmail();
	}
}
