package util;

import java.time.LocalDate;

public interface IUPloadWorker extends Runnable {
	boolean login(String token, String pageID);
	void setData(int interval, LocalDate startDate, String publishTimes);
	void stop();
}
