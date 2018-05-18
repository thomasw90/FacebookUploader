package application;

public interface IFileWorker extends Runnable {
	void checkFolderPath();
	void setPath(String path);
	void setInterval(int interval);
}
