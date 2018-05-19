package util;

public interface IFileWorker extends Runnable {
	void setPath(String path);
	void setInterval(int interval);
	void stop();
}
