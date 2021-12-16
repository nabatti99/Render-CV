package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyles;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocument1;

import models.Person;
import models.User;

public class DOCXController {
	XWPFDocument document;
	XWPFStyles styles;
	Person person;
	
	public DOCXController(Person person) throws Exception {
		this.person = person;

		document = getTemplateDocument();
		styles = document.getStyles();
		
		renderTable();
		renderDetails();
		
		addBreakPage(document);
	}
	
	void renderDetails() {
		List<XWPFParagraph> paragraphs = document.getParagraphs();
		for (int index = 0; index < paragraphs.size(); ++index) {
			XWPFParagraph paragraph = paragraphs.get(index);
			switch (paragraph.getText()) {
			case "${Sections}":
				document.removeBodyElement(document.getPosOfParagraph(paragraph));
				renderSection(Person.ABOUT_INFO, person.data.get(Person.ABOUT_INFO));
				renderSection(Person.QUESTIONS, person.data.get(Person.QUESTIONS));
			}
		}
	}
	
	void renderTable() {
		List<XWPFTable> tables = document.getTables();
		
		tables.forEach(table -> {
			List<XWPFTableRow> rows = table.getRows();
			rows.forEach(row -> {
				List<XWPFTableCell> cells = row.getTableCells();
				cells.forEach(cell -> {
					List<XWPFParagraph> paragraphs = cell.getParagraphs();
					for (int index = 0; index < paragraphs.size(); ++index) {
						
						XWPFParagraph paragraph = paragraphs.get(index);
						switch (paragraph.getText()) {
						case "${Name}":
							cell.removeParagraph(index);
							XWPFParagraph newParagraph = cell.addParagraph();
							addTitleText(newParagraph, person.data.get(Person.PERSONAL).get("Name"));
							break;
							
						case "${PersonalInformation}":
							cell.removeParagraph(index);
							renderInfoList(cell, person.data.get(Person.PERSONAL));
							break;
							
						case "${AboutEnglish}":
							cell.removeParagraph(index);
							renderEnglishList(cell, person.data.get(Person.ENGLISH));
							break;
						}
					}
				});
			});
		});
	}
	
	void renderInfoList(XWPFTableCell cell, HashMap<String, String> infoList) {
		
		for (String key : infoList.keySet()) {
			if (hasRequiredFields(key))
				continue;
			
			XWPFParagraph newParagraph = cell.addParagraph();
			addNormalText(newParagraph, String.format("%s: %s", key, infoList.get(key)));
		}
	}
	
	void renderEnglishList(XWPFTableCell cell, HashMap<String, String> infoList) {
		
		for (String key : infoList.keySet()) {
			if (hasRequiredFields(key))
				continue;
			
			XWPFParagraph newParagraph = cell.addParagraph();
			addNormalText(newParagraph, String.format("%s: %s", key, infoList.get(key)));
			newParagraph.setIndentationLeft((int) (0.4 * 567)); // 1 cm = 567 twips
		}
	}
	
	void renderSection(String title, HashMap<String, String> content) {
		XWPFParagraph paragraph = document.createParagraph();
		addHeading1Text(paragraph, title);
		
		content.forEach((header, text) -> {
			XWPFParagraph headerParagraph = document.createParagraph();
			addHeading2Text(headerParagraph, header);
			
			XWPFParagraph textParagraph = document.createParagraph();
			addNormalText(textParagraph, text);
		});
	}
	
	void addNormalText(XWPFParagraph paragraph, String text) {
		XWPFRun run = paragraph.createRun();
		paragraph.setStyle("Normal");
		run.setText(text);
	}
	
	void addTitleText(XWPFParagraph paragraph, String text) {
		XWPFRun run = paragraph.createRun();
		paragraph.setStyle("Title");
		run.setText(text);
	}
	
	void addHeading1Text(XWPFParagraph paragraph, String text) {
		XWPFRun run = paragraph.createRun();
		paragraph.setStyle("Heading1");
		run.setText(text);
	}
	
	void addHeading2Text(XWPFParagraph paragraph, String text) {
		XWPFRun run = paragraph.createRun();
		paragraph.setStyle("Heading2");
		run.setText(text);
	}

	void addBreakPage(XWPFDocument document) {
		document.createParagraph().setPageBreak(true);
	}
	
	boolean hasRequiredFields(String field) {
		for (int i = 0; i < Person.REQUIRED_FIELDS.size(); ++i)
			if (Person.REQUIRED_FIELDS.get(i).equals(field))
				return true;
		
		return false;
	}
	
	static XWPFDocument getTemplateDocument() throws Exception {
		Path templateFilePath = AssetsController.getAsset("/Template.docx");
		File templateFile = templateFilePath.toFile();
		FileInputStream inputStream = new FileInputStream(templateFile);
		
		return new XWPFDocument(inputStream);
	}
	
	public static XWPFDocument combine(ArrayList<DOCXController> documentControllers) throws Exception {
		XWPFDocument finalDocument = getTemplateDocument();
		CTDocument1 finalCTDocument = finalDocument.getDocument();
		finalCTDocument.unsetBody();
		
		documentControllers.forEach(controller -> {
			CTDocument1 srcCTDocument = controller.document.getDocument();
			
			if (srcCTDocument != null) {
				CTBody srcCTBody = srcCTDocument.getBody();
				if (srcCTBody != null) {
					CTBody finalCTBody = finalCTDocument.addNewBody();
					finalCTBody.set(srcCTBody);
				}
			}
		});
		
		return finalDocument;
	}
}
