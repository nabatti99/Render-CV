package controllers;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.HttpMethod;
import com.google.cloud.storage.Storage;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.StorageClient;

import models.Document;
import models.User;

public class StorageController {
	
	public static URL uploadDocument(Path documentPath, User user, Document document) throws Exception {
		
		document.status = Document.UPLOADING_STATUS;
		FirestoreController.updateDocument(user, document);
		
		Bucket storage = FirebaseController.getStorage();
		
		try {
			Blob blob = storage.create(String.format("%s/%s/%s", user.token.getUid(), document.documentId, document.name), Files.readAllBytes(documentPath));
			return blob.signUrl(3, TimeUnit.HOURS, Storage.SignUrlOption.httpMethod(HttpMethod.GET));			
		} catch (Exception e) {
			e.printStackTrace();
			document.status = Document.ERROR_STATUS;
			return null;
		}
	}
	
	public static URL getDownloadLink(User user, Document document) throws Exception {
		Bucket storage = FirebaseController.getStorage();
		
		try {
			Blob blob = storage.get(String.format("%s/%s/%s", user.token.getUid(), document.documentId, document.name));
			return blob.signUrl(3, TimeUnit.HOURS, Storage.SignUrlOption.httpMethod(HttpMethod.GET));			
		} catch(Exception e) {
			e.printStackTrace();
			document.status = Document.ERROR_STATUS;
			return null;
		}
	}
}
