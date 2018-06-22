package main;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.converter.NumberStringConverter;
import util.IFacebookUploader;
import util.impl.FacebookUploader;

public class Controller implements Initializable {

	private final static int CHECKINTERVALL = 2000;
	
	private IFacebookUploader uploader;
	
	@FXML
	private TextField accesstoken;
	@FXML
	private TextField pageID;
	@FXML
	private Text loginNotification;
	@FXML
	private TextField path;	
	@FXML
	private Text searchNotification;
	@FXML
	private DatePicker datePicker;
	@FXML
	private TextField publishTimes;
	@FXML
	Text numUploads;
	@FXML
	Text numToUpload;
	@FXML
	ProgressBar progressBarActive;
	@FXML
	ProgressBar progressBar;
		
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		uploader = new FacebookUploader();
		numUploads.textProperty().bindBidirectional(uploader.getNumUploads(), new NumberStringConverter());
		numToUpload.textProperty().bindBidirectional(uploader.getNumToUpload(), new NumberStringConverter());
		setRunning(false);
	}
	
	public void login() {
		if(uploader.login(accesstoken.getText(), pageID.getText())) {
			loginNotification.setText("login erfolgreich");
			loginNotification.setFill(Color.GREEN);
		} else {
			loginNotification.setText("login fehlgeschlagen");
			loginNotification.setFill(Color.RED);
		}
	}
	
	public void start() {	
		try {
			uploader.start(CHECKINTERVALL, path.getText(), datePicker.getValue(), publishTimes.getText());
			setRunning(true);
		} catch(NumberFormatException e){
		}
	}
	
	public void stop() {
		uploader.stop();
		setRunning(false);
	}
	
	public void setRunning(boolean running) {
		if(running) {
			progressBarActive.setVisible(true);
			progressBar.setVisible(false);
		} else {
			progressBarActive.setVisible(false);
			progressBar.setVisible(true);
		}
	}
}
