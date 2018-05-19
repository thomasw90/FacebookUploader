package entities.impl;

import java.util.Date;

import entities.IPicture;

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
