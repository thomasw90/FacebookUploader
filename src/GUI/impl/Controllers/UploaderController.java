package GUI.impl.Controllers;

import java.io.File;

import GUI.PaneSwitcher;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import util.IFacebookUploader;

public class UploaderController implements IController {

	private final static int CHECKINTERVALL = 2000;
	
	private IFacebookUploader uploader;
	private DirectoryChooser dirChooser;
	private Stage primaryStage;
	@SuppressWarnings("unused")
	private GUI.PaneSwitcher switcher;
	
	@FXML
	private TextField pageID;
	@FXML
	private Text loginNotification;
	@FXML
	private TextField path;	
	@FXML
	private DatePicker datePicker;
	@FXML
	private TextField publishTimes;
	@FXML
	Text numUploads;
	@FXML
	Text numToUpload;
	@FXML
	Text finishing;
	@FXML
	ProgressBar progressBarActive;
	@FXML
	ProgressBar progressBar;
	@FXML
	Button startButton;
	@FXML
	Button stopButton;
	@FXML
	ComboBox<String> pageNames;
	
	@Override
	public void init(Stage primaryStage, PaneSwitcher switcher, IFacebookUploader uploader) {
		this.primaryStage = primaryStage;
		this.switcher = switcher;
		this.uploader = uploader;
		dirChooser = new DirectoryChooser();
		numUploads.textProperty().bindBidirectional(uploader.getNumUploads(), new NumberStringConverter());
		numToUpload.textProperty().bindBidirectional(uploader.getNumToUpload(), new NumberStringConverter());
		progressBarActive.visibleProperty().bind(uploader.isActive());
		progressBar.visibleProperty().bind(uploader.isActive().not());
		startButton.disableProperty().bind(uploader.isActive().or(uploader.isFinishing()));
		stopButton.disableProperty().bind(uploader.isActive().not().or(uploader.isFinishing()));
		finishing.visibleProperty().bind(uploader.isFinishing());
		pageNames.setItems(uploader.getPageNames());
	}
	
	public void start() {	
		uploader.start(pageNames.getValue(), CHECKINTERVALL, path.getText(), datePicker.getValue(), publishTimes.getText());
	}
	
	public void stop() {
		uploader.stop();
	}
	
	public void openDirChooser() {
		if(!path.getText().isEmpty()) {
			dirChooser.setInitialDirectory(new File(path.getText()));
		}

		File file = dirChooser.showDialog(primaryStage);
		if(file != null) {
			path.setText(file.getAbsolutePath());
		}
	}
}