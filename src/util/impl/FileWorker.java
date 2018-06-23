package util.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Queue;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class FileWorker implements Runnable {

	private Queue<Picture> queueNewFiles;
	private Queue<Picture> queueUploadedFiles;
	private List<String> alreadyFoundFiles;
	
	private boolean work;
	private int checkInterval;
	private String folderPath;
	private String subFolderPath;
	
	private Object syncObj;
	
	private final IntegerProperty numToUpload = new SimpleIntegerProperty(0);
	private final BooleanProperty running = new SimpleBooleanProperty(false);
	private final BooleanProperty finishing = new SimpleBooleanProperty(false);
	
	private BooleanProperty uploadWorkerRunning;
	
	public FileWorker(Queue<Picture> queueNewFiles, Queue<Picture> queueUploadedFiles, Object syncObj, BooleanProperty uploadWorkerRunning) {
		this.queueNewFiles = queueNewFiles;
		this.queueUploadedFiles = queueUploadedFiles;
		this.syncObj = syncObj;
		this.uploadWorkerRunning = uploadWorkerRunning;
		alreadyFoundFiles = new ArrayList<>();
	}
	
	@Override
	public void run() {
		running.set(true);
		
		numToUpload.set(0);		
		work = true;		
		while(work) {
			checkForNewPictures();
			removeUploadedPictures();			
			try {
				Thread.sleep(checkInterval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		alreadyFoundFiles.clear();
		queueNewFiles.clear();
		
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
		
		running.set(false);
		finishing.set(false);
	}

	public void stop() {
		finishing.set(true);
		work = false;
		queueNewFiles.clear();
	}

	public void setData(int checkInterval, String folderPath) {
			this.folderPath = folderPath;
			this.checkInterval = checkInterval;
			
			File subfolder = new File(folderPath.toString() + File.separator + "uploaded");
			if(!subfolder.exists()) {
				subfolder.mkdir();
			}
			subFolderPath = subfolder.toString();
	}
	
	public IntegerProperty getNumToUpload() {
        return numToUpload;
    }

	public BooleanProperty isRunning() {
		return running;
	}
	
	public BooleanProperty isFinishing() {
		return finishing;
	}
	
	private void checkForNewPictures() {
		
		File folder = new File(folderPath);
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
		    if (file.isFile() && !alreadyFoundFiles.contains(file.getAbsolutePath())) {
		    	alreadyFoundFiles.add(file.getAbsolutePath());
		    	Picture newPicture = new Picture(file.getAbsolutePath(), new Date());
				queueNewFiles.add(newPicture);
				numToUpload.set(numToUpload.get() + 1);
		    }
		}
	}
	
	private void removeUploadedPictures() {
		Picture uploadedPicture = queueUploadedFiles.poll();
		if(uploadedPicture != null) {
			File oldPath = new File(uploadedPicture.getFilePath());	
			File newPath = new File(subFolderPath + File.separator + oldPath.getName());
			oldPath.renameTo(newPath);
			alreadyFoundFiles.remove(uploadedPicture.getFilePath());
		}
	}
}
