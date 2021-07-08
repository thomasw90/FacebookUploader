package backend;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Queue;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import backend.IFacebookUploader;

public class FacebookUploader implements IFacebookUploader {

	/** Worker for uploading pictures to Facebook */
	private UploadWorker uploadWorker;
	
	/** Thread where the UploadWorker is running */
	private Thread threadUpload;
	
	/** Worker for searching in a directory */
	private FileWorker fileWorker;
	
	/** Thread where the FileWorker is running */
	private Thread threadFilesearch;
	
	/** This is the constructor */
	public FacebookUploader() {
		Queue<String> queueNewFiles = new LinkedList<>();
		Queue<String> queueUploadedFiles = new LinkedList<>();
		Object syncObj = new Object();
		uploadWorker = new UploadWorker(queueNewFiles, queueUploadedFiles, syncObj);
		fileWorker = new FileWorker(queueNewFiles, queueUploadedFiles, syncObj, uploadWorker.isRunning());
	}
	
	@Override
	public boolean login(String token) {
		return uploadWorker.login(token);
	}

	@Override
	public void start(String pageName, int interval, String folderPath, LocalDate startDate, String publishTimes) {
		if((threadUpload == null && threadFilesearch == null)
			|| (!threadUpload.isAlive() && !threadFilesearch.isAlive())) {
			
			uploadWorker.setData(pageName, interval, startDate, publishTimes);
			threadUpload = new Thread(uploadWorker);
			threadUpload.start();
			
			fileWorker.setData(interval, folderPath);
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

	@Override
	public ListProperty<String> getPageNames() {
		return uploadWorker.getPageNames();
	}
}
