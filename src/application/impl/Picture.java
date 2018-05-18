package application.impl;

import java.util.Date;

import application.IPicture;

public class Picture implements IPicture {

	private String filePath;
	private Date releaseDate;
	
	public Picture(String filePath, Date releaseDate) {
		this.filePath = filePath;
		this.releaseDate = releaseDate;
	}
	
	@Override
	public String getFilePath() {
		return filePath;
	}

	@Override
	public Date getReleaseDate() {
		return releaseDate;
	}

}
