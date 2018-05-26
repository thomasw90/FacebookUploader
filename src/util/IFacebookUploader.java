package util;

import java.time.LocalDate;

public interface IFacebookUploader {
	boolean login(String token, String page);
	void start(int checkInterval, String path, LocalDate startDate, String publishTimes);
	void stop();
	boolean isActive();
}
