package application;

public interface IFacebookUploader {
	boolean login(String token, String page);
	void start(int checkInterval);
	void stop();
	boolean isActive();
}
