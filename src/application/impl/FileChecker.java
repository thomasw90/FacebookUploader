package application.impl;

import java.util.concurrent.BlockingQueue;

import application.IFileChecker;
import application.IFileWorker;
import application.IPicture;

public class FileChecker implements IFileChecker {

	private IFileWorker fileWorker;
	
	public FileChecker(BlockingQueue<IPicture> queue) {
		fileWorker = new FileWorker(queue);
	}
	
	@Override
	public void start(int checkInterval, String path) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isActive() {
		// TODO Auto-generated method stub
		return false;
	}

}
