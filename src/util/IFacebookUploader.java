package util;

import java.time.LocalDate;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.IntegerProperty;

public interface IFacebookUploader {
	
	/** 
	 * Sets Facebook login data
	 * @param token Access-Token
	 * @param pageID Name of the Facebook-Page the pictures have to be uploaded
	 */
	boolean login(String token, String pageID);
	
	/** 
	 * Starts searching and uploading in separated threads
	 * @param interval Seconds between checks for new pictures
	 * @param folderPath Path of the directory of new pictures
	 * @param startDate Day of the first picture release
	 * @param publishTimes Times of picture releases each day
	 */
	void start(int interval, String folderPath, LocalDate startDate, String publishTimes);
	
	/** Stops searching and uploading asynchronous */
	void stop();
	
	/** Getter for property */
	BooleanBinding isActive();
	
	/** Getter for property */
	BooleanBinding isFinishing();
	
	/** Getter for property */
	IntegerProperty getNumToUpload();
	
	/** Getter for property */
	IntegerProperty getNumUploads();
}
