package util.impl;

import java.util.Date;

public class Picture {

	private String filePath;
	private Date releaseDate;
	
	public Picture(String filePath, Date releaseDate) {
		this.filePath = filePath;
		this.releaseDate = releaseDate;
	}
	
	public String getFilePath() {
		return filePath;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}
}
