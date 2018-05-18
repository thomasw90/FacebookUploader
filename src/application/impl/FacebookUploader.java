package application.impl;

import java.util.concurrent.BlockingQueue;

import application.IFacebookUploader;
import application.IPicture;
import application.IUPloadWorker;

public class FacebookUploader implements IFacebookUploader {

	private IUPloadWorker uploadWorker;
	
	public FacebookUploader(BlockingQueue<IPicture> queue) {
		uploadWorker = new UploadWorker(queue);
	}
	
	@Override
	public boolean login(String token, String page) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void start(int checkInterval) {
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
