package util;

import java.util.prefs.Preferences;

public class Storage {
	private static Storage instance = new Storage();
	
	private static final String PREF_NODE = "FacebookUploader";
	
	private static final String PREF_FB_KEY = "FB_Key";
	
	private Preferences prefs;

	private Storage() {
		prefs = Preferences.userRoot().node(PREF_NODE);
	}

	public static Storage getInstance() {
		return Storage.instance;
	}
	
	public String getAccessToken() {
		return prefs.get(PREF_FB_KEY, "");
	}
	
	public void setAccessToken(String accessToken) {
		prefs.put(PREF_FB_KEY, accessToken);
	}
	
}
