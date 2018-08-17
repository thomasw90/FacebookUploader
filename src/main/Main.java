package main;

	
import GUI.PaneSwitcher;
import GUI.impl.ScreenType;
import javafx.application.Application;
import javafx.stage.Stage;
import util.IFacebookUploader;
import util.impl.FacebookUploader;

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {		
			IFacebookUploader facebookUploader = new FacebookUploader();
			PaneSwitcher switcher = new PaneSwitcher(primaryStage, facebookUploader);
			switcher.changeScreen(ScreenType.LOGIN);
			primaryStage.show();
			primaryStage.setTitle("Facebook Picture Uploader");			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);		
	}
}
