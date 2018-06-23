package util.impl;

import java.util.Date;

/**
 * Contains data for a picture that has to be uploaded
 */
public class Picture {

	/** Path of the file */
	private String filePath;
	
	/** Date the picture has to be released */
	private Date releaseDate;
	
	/** This is the constructor */
	public Picture(String filePath, Date releaseDate) {
		this.filePath = filePath;
		this.releaseDate = releaseDate;
	}
	
	/** 
	 * Get the file path
	 * @return file path
	 */
	public String getFilePath() {
		return filePath;
	}

	/** 
	 * Get the release date
	 * @return release date 
	 */
	public Date getReleaseDate() {
		return releaseDate;
	}
}
