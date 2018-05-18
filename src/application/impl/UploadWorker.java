package application.impl;

import java.util.concurrent.BlockingQueue;

import application.IPicture;
import application.IUPloadWorker;

public class UploadWorker implements IUPloadWorker {

	BlockingQueue<IPicture> queue;
	
	public UploadWorker(BlockingQueue<IPicture> queue) {
		this.queue = queue;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean uploadPicture(IPicture picture) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean login(String token, String pageID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setInterval(int interval) {
		// TODO Auto-generated method stub

	}

}
