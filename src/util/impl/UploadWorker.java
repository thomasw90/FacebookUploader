package util.impl;

import java.util.Queue;

import entities.IPicture;
import util.IUPloadWorker;

public class UploadWorker implements IUPloadWorker {

	private Queue<IPicture> queueNewFiles;
	private Queue<IPicture> queueUploadedFiles;
	
	private boolean isWorking;
	private int interval;
	
	public UploadWorker(Queue<IPicture> queueNewFiles, Queue<IPicture> queueUploadedFiles) {
		this.queueNewFiles = queueNewFiles;
		this.queueUploadedFiles = queueUploadedFiles;
	}
	
	private IPicture takePicture() {		
		return queueNewFiles.poll();
	}
	
	private boolean uploadPicture(IPicture picture) {
		if(picture != null) {
			System.out.println("Datei hochgeladen: " + picture.getFilePath() + " " + picture.getReleaseDate().toString());
			queueUploadedFiles.add(picture);
			return true;
		}
		return false;
	}
	
	@Override
	public void run() {
		isWorking = true;
		
		while(isWorking) {
			
			uploadPicture(takePicture());
			
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean login(String token, String pageID) {
		// login zu facebook
		// überprüfen ob erfolgreich
		
		// erfolgreich
		return true;
		
		// nicht erfolgreich
		// return false;
	}

	@Override
	public void setInterval(int interval) {
		this.interval = interval;
	}

	@Override
	public void stop() {
		isWorking = false;
		queueUploadedFiles.clear();
	}

}
