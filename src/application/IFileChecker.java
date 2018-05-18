package application;

public interface IFileChecker {
	void start(int checkInterval, String path);
	void stop();
	boolean isActive();
}
