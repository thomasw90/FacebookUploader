package util;

public interface IFileWorker extends Runnable {
	void setData(int checkInterval, String folderPath);
	void stop();
}
