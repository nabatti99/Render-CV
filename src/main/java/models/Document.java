package models;

import com.google.cloud.Timestamp;

public class Document {
	public static final String INITIATING_STATUS = "Initiating";
	public static final String RENDERING_STATUS = "Rendering";
	public static final String UPLOADING_STATUS = "Uploading";
	public static final String DONE_STATUS = "Done";
	public static final String ERROR_STATUS = "Error";
	
	public String documentId;
	public String name;
	public String status = INITIATING_STATUS;
	public String downloadLink;
	public Timestamp timestamp;
	
	public Document(String name) {
		this.name = name;
	}
}
