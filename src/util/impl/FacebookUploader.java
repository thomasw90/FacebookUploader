package util.impl;

import java.util.LinkedList;
import java.util.Queue;

import entities.IPicture;
import util.IFacebookUploader;
import util.IFileWorker;
import util.IUPloadWorker;

public class FacebookUploader implements IFacebookUploader {

	private Thread threadUpload;
	private IUPloadWorker uploadWorker;
	
	private Thread threadFilesearch;
	private IFileWorker fileWorker;
	
	public FacebookUploader() {
		Queue<IPicture> queueNewFiles = new LinkedList<>();
		Queue<IPicture> queueUploadedFiles = new LinkedList<>();
		uploadWorker = new UploadWorker(queueNewFiles, queueUploadedFiles);
		fileWorker = new FileWorker(queueNewFiles, queueUploadedFiles);
	}
	
	@Override
	public boolean login(String token, String page) {
		return uploadWorker.login(token, page);
	}

	@Override
	public void start(int checkInterval, String path) {
		if((threadUpload == null && threadFilesearch == null) 
			|| (!threadUpload.isAlive() && !threadFilesearch.isAlive())) {
			
			uploadWorker.setInterval(checkInterval);
			threadUpload = new Thread(uploadWorker);
			threadUpload.start();
			
			fileWorker.setInterval(checkInterval);
			fileWorker.setPath(path);
			threadFilesearch = new Thread(fileWorker);
			threadFilesearch.start();
		}
	}

	@Override
	public void stop() {
		uploadWorker.stop();
		fileWorker.stop();
	}

	@Override
	public boolean isActive() {
		// TODO Auto-generated method stub
		return false;
	}

}
