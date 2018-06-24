package main;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import util.IFacebookUploader;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {

	IFacebookUploader facebookUploader;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("resources/GUI.fxml"));
			Parent root = loader.load();
			Controller controller = loader.getController();
			controller.setStage(primaryStage);
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
			root.requestFocus();
			primaryStage.setTitle("Facebook Picture Uploader");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);		
	}
}
