package util.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Queue;

import entities.IPicture;
import entities.impl.Picture;
import util.IFileWorker;

public class FileWorker implements IFileWorker {

	private Queue<IPicture> queueNewFiles;
	private Queue<IPicture> queueUploadedFiles;
	private List<String> alreadyFoundFiles;
	
	private boolean isWorking;
	private int checkInterval;
	private String folderPath;
	private String subFolderPath;
	
	public FileWorker(Queue<IPicture> queueNewFiles, Queue<IPicture> queueUploadedFiles) {
		this.queueNewFiles = queueNewFiles;
		this.queueUploadedFiles = queueUploadedFiles;
		alreadyFoundFiles = new ArrayList<>();
	}
	
	private void checkForNewPictures() {
		
		File folder = new File(folderPath);
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
		    if (file.isFile() && !alreadyFoundFiles.contains(file.getAbsolutePath())) {
		    	alreadyFoundFiles.add(file.getAbsolutePath());
				IPicture newPicture = new Picture(file.getAbsolutePath(), new Date());
				queueNewFiles.add(newPicture);
		    }
		}
	}
	
	private void removeUploadedPictures() {
		IPicture uploadedPicture = queueUploadedFiles.poll();
		if(uploadedPicture != null) {
			File oldPath = new File(uploadedPicture.getFilePath());	
			File newPath = new File(subFolderPath + File.separator + oldPath.getName());
			oldPath.renameTo(newPath);
			alreadyFoundFiles.remove(uploadedPicture.getFilePath());
		}
	}
	
	@Override
	public void run() {
		isWorking = true;
		
		while(isWorking) {

			checkForNewPictures();
			removeUploadedPictures();
			
			try {
				Thread.sleep(checkInterval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void stop() {
		isWorking = false;
		queueNewFiles.clear();
	}

	@Override
	public void setData(int checkInterval, String folderPath) {
			this.folderPath = folderPath;
			this.checkInterval = checkInterval;
			
			File subfolder = new File(folderPath.toString() + File.separator + "uploaded");
			if(!subfolder.exists()) {
				subfolder.mkdir();
			}
			subFolderPath = subfolder.toString();
	}
}
