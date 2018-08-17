package GUI.impl.Controllers;

import GUI.PaneSwitcher;
import javafx.stage.Stage;
import util.IFacebookUploader;

public interface IController {
	
	/** Initializes controller */
	void init(Stage primaryStage, PaneSwitcher switcher, IFacebookUploader uploader);
}
