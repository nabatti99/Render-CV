package controllers;

import javax.servlet.http.Cookie;

import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;

public class AuthenticationController {
	
	public static FirebaseToken verifyIdToken (String idToken) throws Exception {
		FirebaseToken token = FirebaseController.getAuth().verifyIdToken(idToken);
		
		if (token == null)
			throw new Exception("Token is invalid");
		
		return token;
	}
	
	public static String getIdToken (Cookie[] cookies) throws Exception {
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("idToken"))
				return cookie.getValue();
		}
		
		throw new Exception("Not found idToken");
	}
	
	public static UserRecord createNewAccount(String email, String password) throws Exception {
		CreateRequest request = new CreateRequest()
				.setEmail(email)
				.setPassword(password);
				
		return FirebaseController.getAuth().createUser(request);
	}
}
