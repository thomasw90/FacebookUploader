package util;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;

public interface IFileWorker extends Runnable {
	void setData(int checkInterval, String folderPath);
	void stop();
	IntegerProperty getNumToUpload();
	BooleanProperty isRunning();
}
