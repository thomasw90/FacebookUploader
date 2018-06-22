package util;

import java.time.LocalDate;

import javafx.beans.property.IntegerProperty;

public interface IFacebookUploader {
	boolean login(String token, String page);
	void start(int checkInterval, String path, LocalDate startDate, String publishTimes);
	void stop();
	boolean isActive();
	IntegerProperty getNumToUpload();
	IntegerProperty getNumUploads();
}
