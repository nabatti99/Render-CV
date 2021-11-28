package controllers;

import java.io.InputStream;
import java.util.ArrayList;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import models.Person;

public class ConvertController extends Thread {
	
	InputStream inputStream;
	
	public ConvertController(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	@Override
	public void run() {
		try {
			ArrayList<DOCXController> docxControllers = new ArrayList<DOCXController>();
			
			XLSXController xlsxController = new XLSXController(inputStream);
			for (Person person : xlsxController.persons) {				
				DOCXController docxController = new DOCXController(person);
				docxControllers.add(docxController);
			}
			
			XWPFDocument document = DOCXController.combine(docxControllers);
			DOCXController.save(document, "Test");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
