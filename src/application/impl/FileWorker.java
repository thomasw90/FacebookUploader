package application.impl;

import java.util.concurrent.BlockingQueue;

import application.IFileWorker;
import application.IPicture;

public class FileWorker implements IFileWorker {

	BlockingQueue<IPicture> queue;
	
	public FileWorker(BlockingQueue<IPicture> queue) {
		this.queue = queue;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	@Override
	public void checkFolderPath() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPath(String path) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setInterval(int interval) {
		// TODO Auto-generated method stub

	}

}
