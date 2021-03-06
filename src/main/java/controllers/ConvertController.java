package controllers;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import models.Document;
import models.Person;
import models.User;

public class ConvertController {

	InputStream inputStream;
	Document document;
	User user;
	
	XLSXController xlsxController;
	ArrayList<DOCXController> docxControllers;
	XWPFDocument finalDocument;
	
	ExecutorService executor = Executors.newCachedThreadPool();
	ArrayList<Future<DOCXController>> futures = new ArrayList<Future<DOCXController>>();
	
	public ConvertController(InputStream inputStream, User user, Document document) throws Exception {
		this.inputStream = inputStream;
		this.user = user;
		this.document = document;
	}
	
	public Thread createJob() {
		return new Thread() {
			@Override
			public void run() {
				
				try {
					try {					
						document.status = Document.RENDERING_STATUS;
						FirestoreController.updateDocument(user, document);
						
						docxControllers = new ArrayList<DOCXController>();
						xlsxController = new XLSXController(inputStream);
						
						for (Person person : xlsxController.persons) {
							Callable<DOCXController> worker = createWorker(person);
							futures.add(executor.submit(worker));
						}
						
						for (Future<DOCXController> future : futures)
							docxControllers.add(future.get());
						
						finalDocument = DOCXController.combine(docxControllers);
						Path documentPath = save();
						
						URL downloadLink = StorageController.uploadDocument(documentPath, user, document);
						document.downloadLink = downloadLink.toString();
						
						document.status = Document.DONE_STATUS;
						FirestoreController.updateDocument(user, document);
						
					} catch (Exception e) {
						e.printStackTrace();
						document.status = Document.ERROR_STATUS;
						FirestoreController.updateDocument(user, document);
					}
					
				} catch (Exception bigError) {
					bigError.printStackTrace();
				}
			}
		};
	}
	
//	public Thread createJob() {
//		return new Thread() {
//			@Override
//			public void run() {
//				try {					
//					Thread.sleep(5000);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		};
//	}
	
	Callable<DOCXController> createWorker(Person person) {
		Callable<DOCXController> worker = new Callable<DOCXController>() {
			@Override
			public DOCXController call() throws Exception {
				return new DOCXController(person);
			}
		};
		
		return worker;
	}
	
	public Path save() throws Exception {
		Path userFolder = AssetsController.getAsset("/" + user.token.getUid());
		Files.createDirectories(userFolder);
		Path documentPath = Paths.get(userFolder.toString(), document.documentId + ".docx");
		
		FileOutputStream outputStream = new FileOutputStream(documentPath.toFile());
		finalDocument.write(outputStream);
		
		return documentPath;
	}
}
