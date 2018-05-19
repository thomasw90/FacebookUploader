package application.GUI;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import util.IFacebookUploader;
import util.impl.FacebookUploader;

public class Controller implements Initializable {

	private IFacebookUploader uploader;
	
	@FXML
	private TextField accesstoken;
	@FXML
	private TextField pageID;
	@FXML
	private Text loginNotification;
	@FXML
	private TextField intervall;
	@FXML
	private TextField path;	
	@FXML
	private Text searchNotification;
		
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		uploader = new FacebookUploader();
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
			uploader.start(Integer.parseInt(intervall.getText()) * 1000, path.getText());
			searchNotification.setText("");
		} catch(NumberFormatException e){
			searchNotification.setText("bitte Ganzzahl eingeben");
		}
	}
	
	public void stop() {
		uploader.stop();
	}
}
