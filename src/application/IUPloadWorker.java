package application;

public interface IUPloadWorker extends Runnable {
	boolean uploadPicture(IPicture picture);
	boolean login(String token, String pageID);
	void setInterval(int interval);
}
