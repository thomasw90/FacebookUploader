package GUI.impl.Controllers;

import GUI.PaneSwitcher;
import GUI.impl.ScreenType;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import util.IFacebookUploader;

public class LoginController implements IController {

	@SuppressWarnings("unused")
	private Stage primaryStage;
	private PaneSwitcher switcher;
	private IFacebookUploader uploader;
	
	@FXML
	private TextField accesstoken;
	@FXML
	private Text notification;
	
	@Override
	public void init(Stage primaryStage, PaneSwitcher switcher, IFacebookUploader uploader) {
		this.primaryStage = primaryStage;
		this.switcher = switcher;
		this.uploader = uploader;
	}
	
	public void login() {
		if(uploader.login(accesstoken.getText())) {
			switcher.changeScreen(ScreenType.UPLOADER);
		} else {
			notification.setText("Login failed");
			notification.setFill(Color.RED);
		}
	}
}
