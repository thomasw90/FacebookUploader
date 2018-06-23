package util.impl;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.Queue;

import com.restfb.BinaryAttachment;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.exception.FacebookException;
import com.restfb.types.GraphResponse;
import com.restfb.types.Page;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class UploadWorker implements  Runnable {

	private Queue<Picture> queueNewFiles;
	private Queue<Picture> queueUploadedFiles;
	
	private FacebookClient fbClient;
	private String pageID;
	
	private boolean work;
	private int interval;
	
	private Object syncObj;
	
	private Queue<LocalTime> publishLocalTimes;	
	private ZonedDateTime nextPublishDate;
	
	private final IntegerProperty numUploaded = new SimpleIntegerProperty(0);
	private final BooleanProperty running = new SimpleBooleanProperty(false);
	private final BooleanProperty finishing = new SimpleBooleanProperty(false);
	
	public UploadWorker(Queue<Picture> queueNewFiles, Queue<Picture> queueUploadedFiles, Object syncObj) {
		this.queueNewFiles = queueNewFiles;
		this.queueUploadedFiles = queueUploadedFiles;
		this.syncObj = syncObj;
		publishLocalTimes = new LinkedList<>();
	}
	
	private Picture takePicture() {		
		return queueNewFiles.poll();
	}
	
	private void parseStringToLocalTimes(String s) {
		String[] times = s.split(",");
		for(String time : times) {
			String[] hoursAndMinutes = time.split(":");
			if(hoursAndMinutes.length == 2) {
				try {
					LocalTime newTime = LocalTime.of(Integer.parseInt(hoursAndMinutes[0].replace(" ", "")), Integer.parseInt(hoursAndMinutes[1].replace(" ", "")));
					publishLocalTimes.add(newTime);
				} catch(Exception e) {}
			}
		}
	}
	
	private LocalTime getNextTime() {
		LocalTime nextPublishTime = publishLocalTimes.poll();
		publishLocalTimes.add(nextPublishTime);
		return nextPublishTime;
	}
	
	private void setNextPublishDate() {
		LocalTime lastPublishTime = nextPublishDate.toLocalTime();
		LocalTime nextPublishTime = getNextTime();
		
		if(!nextPublishTime.isAfter(lastPublishTime)) {
			nextPublishDate = nextPublishDate.plusDays(1);
			nextPublishDate = nextPublishDate.minusHours(lastPublishTime.getHour() - nextPublishTime.getHour());
		} else {
			nextPublishDate = nextPublishDate.plusHours(nextPublishTime.getHour() - lastPublishTime.getHour());
		}
	}
	
	private boolean uploadPicture(Picture picture) {
		if(picture != null) {
			try {
				byte[] fileContent = Files.readAllBytes((new File(picture.getFilePath()).toPath()));
				if(nextPublishDate != null) {
					fbClient.publish(pageID + "/photos",
									 GraphResponse.class,
									 BinaryAttachment.with("Test.jpg", fileContent),
									 Parameter.with("published", "false"),
									 Parameter.with("scheduled_publish_time", String.valueOf(nextPublishDate.toEpochSecond())));
					setNextPublishDate();
				} else {
					fbClient.publish(pageID + "/photos",
									 GraphResponse.class,
									 BinaryAttachment.with("Test.jpg", fileContent));
				}			 
				queueUploadedFiles.add(picture);
				numUploaded.set(numUploaded.get() + 1);
				return true;
			} catch (Exception e) {}
		}
		return false;
	}
	
	@Override
	public void run() {
		running.set(true);
		
		numUploaded.set(0);		
		work = true;		
		while(work) {	
			uploadPicture(takePicture());	
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		synchronized(syncObj) {
			syncObj.notifyAll();
        }		
		running.set(false);
		finishing.set(false);
	}

	public boolean login(String token, String pageID) {
		this.pageID = pageID;		
		fbClient = new DefaultFacebookClient(token, Version.VERSION_2_6);
		try {
			fbClient.fetchObject(pageID, Page.class);
			return true;
		} catch(FacebookException e) {
			return false;
		}
	}

	public void stop() {
		finishing.set(true);
		work = false;
		queueUploadedFiles.clear();
	}

	public void setData(int interval, LocalDate startDate, String publishTimes) {
		this.interval = interval;
				
		if(startDate != null && !publishTimes.isEmpty()) {
			parseStringToLocalTimes(publishTimes);			
			nextPublishDate = startDate.atTime(getNextTime()).atZone(ZoneId.systemDefault());
		} else {
			nextPublishDate = null;
		}
	}
	
	public IntegerProperty getNumUploads() {
        return numUploaded;
    }

	public BooleanProperty isRunning() {
		return running;
	}
	
	public BooleanProperty isFinishing() {
		return finishing;
	}
}

