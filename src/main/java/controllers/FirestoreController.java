package controllers;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;

import models.Document;
import models.User;

public class FirestoreController {
	
	public static void getAllDocuments(User user) throws Exception {
		CollectionReference documentCollection = FirebaseController.getFirestore()
				.collection("users")
				.document(user.token.getUid())
				.collection("documents");
		
		ArrayList<Document> documents = new ArrayList<Document>();
		for (DocumentReference documentReference : documentCollection.listDocuments()) {
			
			Document document = getDocument(user, documentReference.getId());
			documents.add(document);
		}
		
		user.documents = documents;
	}
	
	public static Document getDocument(User user, String documentId) throws Exception {
		DocumentReference documentReference = FirebaseController.getFirestore()
				.collection("users")
				.document(user.token.getUid())
				.collection("documents")
				.document(documentId);
		
		ApiFuture<DocumentSnapshot> snapshot = documentReference.get();
		DocumentSnapshot documentSnapshot = snapshot.get();
		
		Document document = new Document(documentSnapshot.getString("name"));
		document.documentId = documentSnapshot.getId();
		document.status = documentSnapshot.getString("status");
		document.timestamp = documentSnapshot.getCreateTime();
		
		try {
			document.downloadLink = StorageController.getDownloadLink(user, document).toString();			
		} catch (Exception e) {
			e.printStackTrace();
			
			document.downloadLink = null;
			document.status = Document.ERROR_STATUS;			
			FirestoreController.updateDocument(user, document);
		}
		
		return document;
	}
	
	public static Document addNewDocument(User user, String documentName) throws Exception {
		CollectionReference documentCollection = FirebaseController.getFirestore()
				.collection("users")
				.document(user.token.getUid())
				.collection("documents");
		
		HashMap<String, String> documentData = new HashMap<String, String>();
		documentData.put("name", documentName);
		documentData.put("status", Document.INITIATING_STATUS);
		
		ApiFuture<DocumentReference> snapshot = documentCollection.add(documentData);
		DocumentReference documentReference = snapshot.get();
		
		Document document = new Document(documentName);
		document.documentId = documentReference.getId();
		return document;
	}
	
	public static void updateDocument(User user, Document document) throws Exception {
		DocumentReference documentReference = FirebaseController.getFirestore()
				.collection("users")
				.document(user.token.getUid())
				.collection("documents")
				.document(document.documentId);
		
		HashMap<String, Object> updateInfo = new HashMap<String, Object>();
		updateInfo.put("status", document.status);
		documentReference.update(updateInfo);
	}
}
