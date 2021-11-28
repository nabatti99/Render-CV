package controllers;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import models.Person;

public class XLSXController {

	public Workbook workbook;
	public ArrayList<Person> persons = new ArrayList<Person>();
	
	ArrayList<HashMap<String, String>> personalSheet;
	ArrayList<HashMap<String, String>> englishSheet;
	ArrayList<HashMap<String, String>> aboutInfoSheet;
	ArrayList<HashMap<String, String>> questionsSheet;
	
	public XLSXController(InputStream XLSXInputStream) throws Exception {
		workbook = new XSSFWorkbook(XLSXInputStream);
		
		personalSheet = getSheetData(workbook.getSheet(Person.PERSONAL));
		englishSheet = getSheetData(workbook.getSheet(Person.ENGLISH));
		aboutInfoSheet = getSheetData(workbook.getSheet(Person.ABOUT_INFO));
		questionsSheet = getSheetData(workbook.getSheet(Person.QUESTIONS));
		
		mapData2Person();
	}
	
	void mapData2Person() throws Exception {
		int size = personalSheet.size();
		
		if (size != englishSheet.size() || size != aboutInfoSheet.size() || size != questionsSheet.size())
			throw new Exception(String.format("Size must be equal in all sheets. (Current sizes: Personal[%s], English[%s], About The Interviewee[%s], Some Answered Questions[%s])", size, englishSheet.size(), aboutInfoSheet.size(), questionsSheet.size()));
		
		for (int index = 0; index < size; ++index) {
			final Person newPerson = new Person();
			newPerson.data.put(Person.PERSONAL, personalSheet.get(index));
			newPerson.data.put(Person.ENGLISH, englishSheet.get(index));
			newPerson.data.put(Person.ABOUT_INFO, aboutInfoSheet.get(index));
			newPerson.data.put(Person.QUESTIONS, questionsSheet.get(index));
			
			persons.add(newPerson);
		}
	}
	
	ArrayList<HashMap<String, String>> getSheetData(Sheet sheet) {
		final ArrayList<String> headers = new ArrayList<String>();
		
		Iterator<Row> sheetIterator = sheet.iterator();

		Row headerRow = sheetIterator.next();
		for (Cell cell : headerRow) {
			headers.add(getCellData(cell));
		}
		
		final ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		while (sheetIterator.hasNext()) {
			Row row = sheetIterator.next();
			result.add(getPersonData(row, headers));
		}
		
		return result;
	}
	
	HashMap<String, String> getPersonData(Row row, ArrayList<String> headers) {
		
		final HashMap<String, String> result = new HashMap<String, String>();
		for (Cell cell : row) {
			int columnIndex = cell.getColumnIndex();
			result.put(headers.get(columnIndex), getCellData(cell));
		}
		
		return result;
	}
	
	String getCellData(Cell cell) {
		switch (cell.getCellType()) {
		case STRING:
			return cell.getStringCellValue();
		case BLANK:
			return CellType.BLANK.toString();
		case BOOLEAN:
			return cell.getBooleanCellValue() + "";
		case ERROR:
			return CellType.ERROR.toString();
		case FORMULA:
			return cell.getCellFormula();
		case NUMERIC:
			return String.format("%.0f", cell.getNumericCellValue());
		case _NONE:
			return CellType._NONE.toString();
		default:
			return "OTHER";
		}
	}
}
