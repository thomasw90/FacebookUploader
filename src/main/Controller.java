package main;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
	private DatePicker datePicker;
	@FXML
	private TextField publishTimes;
	@FXML
	Text numUploads;
	@FXML
	Text numToUpload;
	@FXML
	Text finishing;
	@FXML
	ProgressBar progressBarActive;
	@FXML
	ProgressBar progressBar;
	@FXML
	Button startButton;
	@FXML
	Button stopButton;
		
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		uploader = new FacebookUploader();
		numUploads.textProperty().bindBidirectional(uploader.getNumUploads(), new NumberStringConverter());
		numToUpload.textProperty().bindBidirectional(uploader.getNumToUpload(), new NumberStringConverter());
		progressBarActive.visibleProperty().bind(uploader.isActive());
		progressBar.visibleProperty().bind(uploader.isActive().not());
		startButton.disableProperty().bind(uploader.isActive().or(uploader.isFinishing()));
		stopButton.disableProperty().bind(uploader.isActive().not().or(uploader.isFinishing()));
		finishing.visibleProperty().bind(uploader.isFinishing());
	}
	
	public void login() {
		if(uploader.login(accesstoken.getText(), pageID.getText())) {
			loginNotification.setText("login successful");
			loginNotification.setFill(Color.GREEN);
		} else {
			loginNotification.setText("login failed");
			loginNotification.setFill(Color.RED);
		}
	}
	
	public void start() {	
		uploader.start(CHECKINTERVALL, path.getText(), datePicker.getValue(), publishTimes.getText());
	}
	
	public void stop() {
		uploader.stop();
	}
}
