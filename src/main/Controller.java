package main;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
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
		
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		uploader = new FacebookUploader();
		//searchNotification.textProperty().bind(null);
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
			searchNotification.setText("läuft");
		} catch(NumberFormatException e){
			searchNotification.setText("Fehler");
		}
	}
	
	public void stop() {
		uploader.stop();
	}
}
