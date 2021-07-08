package backend;

import java.time.LocalDate;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;

public interface IFacebookUploader {
	
	/** 
	 * Sets Facebook login data
	 * @param token Access-Token
	 */
	boolean login(String token);
	
	/** 
	 * Starts searching and uploading in separated threads
	 * @param pageName Name of Facebook fanepage
	 * @param interval Seconds between checks for new pictures
	 * @param folderPath Path of the directory of new pictures
	 * @param startDate Day of the first picture release
	 * @param publishTimes Times of picture releases each day
	 */
	void start(String pageName, int interval, String folderPath, LocalDate startDate, String publishTimes);
	
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
	
	/** Getter for property */
	ListProperty<String> getPageNames();
}
