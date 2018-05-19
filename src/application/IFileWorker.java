package application;

public interface IFileWorker extends Runnable {
	void setPath(String path);
	void setInterval(int interval);
	void stop();
}
