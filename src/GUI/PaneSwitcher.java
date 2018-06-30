package GUI;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import GUI.impl.ScreenType;
import GUI.impl.Controllers.IController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import util.IFacebookUploader;

public class PaneSwitcher {

	private Map<ScreenType, Pane> screens;
	private Stage primaryStage;
	
	public PaneSwitcher(Stage primaryStage, IFacebookUploader uploader) throws IOException {
		this.primaryStage = primaryStage;
		screens = new HashMap<>();
		addScreen(ScreenType.UPLOADER, "impl/resources/Uploader.fxml", uploader);
		addScreen(ScreenType.LOGIN, "impl/resources/Login.fxml", uploader);
		primaryStage.setScene(new Scene(screens.get(ScreenType.LOGIN)));
	}
	
	public void changeScreen(ScreenType type) {
		primaryStage.setScene(new Scene(screens.get(type)));
	}
	
	private void addScreen(ScreenType type, String filePath, IFacebookUploader uploader) throws IOException {	
		FXMLLoader loader = new FXMLLoader(getClass().getResource(filePath));
	    Pane pane = loader.load();
	    IController controller = loader.getController();
	    controller.init(primaryStage, this, uploader);    
		screens.put(type, pane);
	}
}
