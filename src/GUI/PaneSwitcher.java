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

	/** Map for switching to other Panes */
	private Map<ScreenType, Pane> screens;
	
	/** Stage the Pane/Scene has to be set */
	private Stage primaryStage;
	
	/** 
	 * This is the constructor 
	 * @param primaryStage Stage the Pane/Scene has to be set
	 * @param uploader interface to the FacebookUploader for the controllers
	 */
	public PaneSwitcher(Stage primaryStage, IFacebookUploader uploader) throws IOException {
		this.primaryStage = primaryStage;
		screens = new HashMap<>();
		addScreen(ScreenType.UPLOADER, "impl/resources/Uploader.fxml", uploader);
		addScreen(ScreenType.LOGIN, "impl/resources/Login.fxml", uploader);
	}
	
	/** 
	 * Changes the displayed Pane/Scene
	 * @param type Pane/Scene that has to be displayed
	 */
	public void changeScreen(ScreenType type) {
		primaryStage.setScene(new Scene(screens.get(type)));
	}
	
	/** 
	 * Creates Panes, initializes those controllers and adds Panes into local map
	 * @param type Type of Pane/Scene that has to be created and added
	 * @param filePath Path of FXML-file
	 * @param interface to the FacebookUploader for the controllers
	 */
	private void addScreen(ScreenType type, String filePath, IFacebookUploader uploader) throws IOException {	
		FXMLLoader loader = new FXMLLoader(getClass().getResource(filePath));
	    Pane pane = loader.load();
	    IController controller = loader.getController();
	    controller.init(primaryStage, this, uploader);    
		screens.put(type, pane);
	}
}
