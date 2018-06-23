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

/**
 * Contains functionality for uploading pictures to Facebook in an own thread
 */
public class UploadWorker implements  Runnable {

	/** Object that is used for synchronizing stop of work. Shared between threads */
	private Object syncObj;
	
	/** Queue that contains pictures they have to be uploaded. Shared between threads */
	private Queue<Picture> queueNewFiles;
	
	/** Queue that contains pictures they have already been uploaded. Shared between threads */
	private Queue<Picture> queueUploadedFiles;
	
	/** Used to communicate with Facebook */
	private FacebookClient fbClient;
	
	/** Release date for the next picture */
	private ZonedDateTime nextPublishDate;
	
	/** Queue that contains repeating release times to calculate release DateTime */
	private Queue<LocalTime> publishLocalTimes;	
	
	/** Name of the Facebook-Page the pictures have to be uploaded */
	private String pageID;
	
	/** Seconds between checks for new pictures */
	private int interval;
	
	/** Flag to terminate thread */
	private boolean work;
		
	/** Amount of uploaded files since start */
	private final IntegerProperty numUploaded = new SimpleIntegerProperty(0);
	
	/** Flag if thread is working */
	private final BooleanProperty running = new SimpleBooleanProperty(false);
	
	/** Flag if thread is finishing upload of last file */
	private final BooleanProperty finishing = new SimpleBooleanProperty(false);
	
	/** 
	 * This is the constructor 
	 * @param queueNewFiles Queue that contains pictures they have to be uploaded. Shared between threads
	 * @param queueUploadedFiles Queue that contains pictures they have already been uploaded. Shared between threads
	 * @param syncObj Object that is used for synchronizing stop of work. Shared between threads
	 */
	public UploadWorker(Queue<Picture> queueNewFiles, Queue<Picture> queueUploadedFiles, Object syncObj) {
		this.queueNewFiles = queueNewFiles;
		this.queueUploadedFiles = queueUploadedFiles;
		this.syncObj = syncObj;
		publishLocalTimes = new LinkedList<>();
	}
	
	/** Job that runs inside the thread */
	@Override
	public void run() {
		running.set(true);
		numUploaded.set(0);		
		work = true;
		
		while(work) {	
			uploadPictures();
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// notify FileWorker that uploads are done
		synchronized(syncObj) {
			syncObj.notifyAll();
        }	
		
		running.set(false);
		finishing.set(false);
	}

	/** 
	 * Sets Facebook login data and validates them
	 * @param token Access-Token
	 * @param pageID Name of the Facebook-Page the pictures have to be uploaded
	 */
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

	/** Stops uploading. If file is currently uploading it will stop after this file */
	public void stop() {
		finishing.set(true);
		work = false;
	}

	/** 
	 * Sets data before thread is starting work
	 * @param interval Seconds between checks for new pictures
	 * @param startDate Day of the first picture release
	 * @param publishTimes Times of picture releases each day
	 */
	public void setData(int interval, LocalDate startDate, String publishTimes) {
		this.interval = interval;		
		if(startDate != null && !publishTimes.isEmpty()) {
			parseStringToLocalTimes(publishTimes);			
			nextPublishDate = startDate.atTime(getNextTime()).atZone(ZoneId.systemDefault());
		} else {
			nextPublishDate = null;
		}
	}
	
	/** Getter for property */
	public IntegerProperty getNumUploads() {
        return numUploaded;
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
	 * Parses a string with the pattern "16:00, 18:00, 19:30" to LocalTimes
	 * @param multipleTimes Times to parse
	 */
	private void parseStringToLocalTimes(String multipleTimes) {
		String[] times = multipleTimes.split(",");
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
	
	/** 
	 * Get next time of the queue
	 * @return Next release time
	 */
	private LocalTime getNextTime() {
		LocalTime nextPublishTime = publishLocalTimes.poll();
		publishLocalTimes.add(nextPublishTime);
		return nextPublishTime;
	}
	
	/** Calculates and sets the release date and time for the next picture */
	private void calculateNextPublishDate() {
		LocalTime lastPublishTime = nextPublishDate.toLocalTime();
		LocalTime nextPublishTime = getNextTime();
		
		if(!nextPublishTime.isAfter(lastPublishTime)) {
			nextPublishDate = nextPublishDate.plusDays(1);
			nextPublishDate = nextPublishDate.minusHours(lastPublishTime.getHour() - nextPublishTime.getHour());
		} else {
			nextPublishDate = nextPublishDate.plusHours(nextPublishTime.getHour() - lastPublishTime.getHour());
		}
	}
	
	/** Uploads all available pictures to Facebook */
	private void uploadPictures() {
		while(!queueNewFiles.isEmpty()) {
			Picture picture = queueNewFiles.poll();
			try {
				byte[] fileContent = Files.readAllBytes((new File(picture.getFilePath()).toPath()));
				if(nextPublishDate != null) {
					fbClient.publish(pageID + "/photos",
									 GraphResponse.class,
									 BinaryAttachment.with("Test.jpg", fileContent),
									 Parameter.with("published", "false"),
									 Parameter.with("scheduled_publish_time", String.valueOf(nextPublishDate.toEpochSecond())));
					calculateNextPublishDate();
				} else {
					fbClient.publish(pageID + "/photos",
									 GraphResponse.class,
									 BinaryAttachment.with("Test.jpg", fileContent));
				}			 
				queueUploadedFiles.add(picture);
				numUploaded.set(numUploaded.get() + 1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

