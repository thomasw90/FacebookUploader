package application;
	
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import application.impl.FacebookUploader;
import application.impl.FileChecker;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	
	BlockingQueue<IPicture> queue;
	IFileChecker fileChecker;
	IFacebookUploader facebookUploader;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			queue = new LinkedBlockingDeque<>();
			fileChecker = new FileChecker(queue);
			facebookUploader = new FacebookUploader(queue);
			
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);		
	}
}
