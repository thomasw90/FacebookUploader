package util.impl;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Queue;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.IntegerProperty;
import util.IFacebookUploader;

public class FacebookUploader implements IFacebookUploader {

	private Thread threadUpload;
	private UploadWorker uploadWorker;
	
	private Thread threadFilesearch;
	private FileWorker fileWorker;
	
	private Object syncObj;
	
	public FacebookUploader() {
		Queue<Picture> queueNewFiles = new LinkedList<>();
		Queue<Picture> queueUploadedFiles = new LinkedList<>();
		syncObj = new Object();
		uploadWorker = new UploadWorker(queueNewFiles, queueUploadedFiles, syncObj);
		fileWorker = new FileWorker(queueNewFiles, queueUploadedFiles, syncObj, uploadWorker.isRunning());
	}
	
	@Override
	public boolean login(String token, String page) {
		return uploadWorker.login(token, page);
	}

	@Override
	public void start(int checkInterval, String path, LocalDate startDate, String publishTimes) {
		if((threadUpload == null && threadFilesearch == null)
			|| (!threadUpload.isAlive() && !threadFilesearch.isAlive())) {
			
			uploadWorker.setData(checkInterval, startDate, publishTimes);
			threadUpload = new Thread(uploadWorker);
			threadUpload.start();
			
			fileWorker.setData(checkInterval, path);
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
	public BooleanBinding isActive() {
		return uploadWorker.isRunning().or(fileWorker.isRunning());
	}
	
	@Override
	public BooleanBinding isFinishing() {
		return uploadWorker.isFinishing().or(fileWorker.isFinishing());
	}
	
	@Override
	public IntegerProperty getNumUploads() {
        return uploadWorker.getNumUploads();
    }

	@Override
	public IntegerProperty getNumToUpload() {
        return fileWorker.getNumToUpload();
    }
}
