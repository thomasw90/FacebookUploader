package util;

public interface IUPloadWorker extends Runnable {
	boolean login(String token, String pageID);
	void setInterval(int interval);
	void stop();
}
