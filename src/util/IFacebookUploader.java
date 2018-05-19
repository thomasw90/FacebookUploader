package util;

public interface IFacebookUploader {
	boolean login(String token, String page);
	void start(int checkInterval, String path);
	void stop();
	boolean isActive();
}
