package util;

import java.time.LocalDate;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;

public interface IUPloadWorker extends Runnable {
	boolean login(String token, String pageID);
	void setData(int interval, LocalDate startDate, String publishTimes);
	void stop();
	IntegerProperty getNumUploads();
	BooleanProperty isRunning();
	BooleanProperty isFinishing();
}
