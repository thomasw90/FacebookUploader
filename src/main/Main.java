package main;
	
import GUI.PaneSwitcher;
import javafx.application.Application;
import javafx.stage.Stage;
import util.IFacebookUploader;
import util.impl.FacebookUploader;

public class Main extends Application {

	IFacebookUploader facebookUploader;
	PaneSwitcher switcher;
	
	@Override
	public void start(Stage primaryStage) {
		try {		
			facebookUploader = new FacebookUploader();
			switcher = new PaneSwitcher(primaryStage, facebookUploader);
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
