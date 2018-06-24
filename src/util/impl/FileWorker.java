package util.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Contains functionality for searching in an own thread.
 * Searches a directory, puts the file information in a queue to shared with 
 * UploadWorker and moves uploaded files in a subfolder called "uploaded"
 */
public class FileWorker implements Runnable {

	/** Object that is used for synchronizing stop of work. Shared between threads */
	private Object syncObj;
	
	/** Queue that contains pictures they have to be uploaded. Shared between threads */
	private Queue<String> queueNewFiles;
	
	/** Queue that contains pictures they have already been uploaded. Shared between threads */
	private Queue<String> queueUploadedFiles;
	
	/** Property for running state of UploadWorker. Shared between threads */
	private BooleanProperty uploadWorkerRunning;
	
	/** List that contains pictures they are in progress but not finished */
	private List<String> alreadyFoundFiles;
	
	/** Seconds between checks for new pictures */
	private int interval;
	
	/** Path of the directory of new pictures */
	private String folderPath;
	
	/** Path of the directory of uploaded pictures */
	private String subFolderPath;
	
	/** Flag to terminate thread */
	private boolean work;
	
	/** Amount of files that have to be uploaded */
	private final IntegerProperty numToUpload = new SimpleIntegerProperty(0);
	
	/** Flag if thread is working */
	private final BooleanProperty running = new SimpleBooleanProperty(false);
	
	/** Flag if thread is finishing upload of last file */
	private final BooleanProperty finishing = new SimpleBooleanProperty(false);
	
	/** 
	 * This is the constructor 
	 * @param queueNewFiles Queue that contains pictures they have to be uploaded. Shared between threads
	 * @param queueUploadedFiles Queue that contains pictures they have already been uploaded. Shared between threads
	 * @param syncObj Object that is used for synchronizing stop of work. Shared between threads
	 * @param uploadWorkerRunning Property for running state of UploadWorker. Shared between threads
	 */
	public FileWorker(Queue<String> queueNewFiles, Queue<String> queueUploadedFiles, Object syncObj, BooleanProperty uploadWorkerRunning) {
		this.queueNewFiles = queueNewFiles;
		this.queueUploadedFiles = queueUploadedFiles;
		this.syncObj = syncObj;
		this.uploadWorkerRunning = uploadWorkerRunning;
		alreadyFoundFiles = new ArrayList<>();
	}
	
	/** Job that runs inside the thread */
	@Override
	public void run() {
		running.set(true);
		numToUpload.set(0);		
		work = true;
		
		while(work) {
			checkForNewPictures();
			removeUploadedPictures();			
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// If UploadWorker is still uploading a picture, wait finishing and remove last uploaded picture
		if(uploadWorkerRunning.get()) {
			synchronized (syncObj) {
				try{
					syncObj.wait();
	            } catch(InterruptedException e){
	                e.printStackTrace();
	            }
				removeUploadedPictures();
			}
		}
		
		alreadyFoundFiles.clear();
		running.set(false);
		finishing.set(false);
	}

	/** Stops searching for new files */
	public void stop() {
		finishing.set(true);
		work = false;
		queueNewFiles.clear();
	}

	/** 
	 * Sets data before thread is starting work and creates subfolder if not exists
	 * @param interval Seconds between checks for new pictures
	 * @param folderPath Path of the directory of new pictures
	 */
	public void setData(int interval, String folderPath) {
			this.folderPath = folderPath;
			this.interval = interval;
			
			File subfolder = new File(folderPath.toString() + File.separator + "uploaded");
			if(!subfolder.exists()) {
				subfolder.mkdir();
			}
			subFolderPath = subfolder.toString();
	}
	
	/** Getter for property */
	public IntegerProperty getNumToUpload() {
        return numToUpload;
    }

	/** Getter for property */
	public BooleanProperty isRunning() {
		return running;
	}
	
	/** Getter for property */
	public BooleanProperty isFinishing() {
		return finishing;
	}
	
	/** 
	 * Checks directory if new pictures are ready to upload and adds them.
	 * Increments noToUpload for new files
	 */
	private void checkForNewPictures() {
		File folder = new File(folderPath);
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
	    	String newPicture = file.getAbsolutePath();
		    if (file.isFile() && !alreadyFoundFiles.contains(newPicture)) {
				queueNewFiles.add(newPicture);
		    	alreadyFoundFiles.add(newPicture);
				numToUpload.set(numToUpload.get() + 1);
		    }
		}
	}
	
	/** Moves uploaded files into subfolder */
	private void removeUploadedPictures() {
		String uploadedPicture = queueUploadedFiles.poll();
		if(uploadedPicture != null) {
			File oldPath = new File(uploadedPicture);	
			File newPath = new File(subFolderPath + File.separator + oldPath.getName());
			oldPath.renameTo(newPath);
			alreadyFoundFiles.remove(uploadedPicture);
		}
	}
}
