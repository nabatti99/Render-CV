package models;

import java.util.ArrayList;
import java.util.HashMap;

public class Person {
	public static final String PERSONAL = "Personal";
	public static final String ENGLISH = "English";
	public static final String ABOUT_INFO = "About The Interviewee";
	public static final String QUESTIONS = "Some Answered Questions";
	
	public static final ArrayList<String> REQUIRED_FIELDS = new ArrayList<String>();
	
	static {
		REQUIRED_FIELDS.add("Name");
	}
	
	public HashMap<String, HashMap<String, String>> data;
	
	public Person() {
		data = new HashMap<String, HashMap<String, String>>();
	}
	
	@Override
	public String toString() {
		String result = "";
		
		for (String sheetName : data.keySet()) {
			result += sheetName + "\n";
			
			HashMap<String, String> sheet = data.get(sheetName);
			for (String key : sheet.keySet())
				result += String.format("%s: %s", key, sheet.get(key)) + ", ";

			result += "\n\n";
		}
		
		return result;
	}
}
