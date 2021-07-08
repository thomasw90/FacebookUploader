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
import backend.IFacebookUploader;

public class UploaderController implements IController {

	/** milliseconds between checks for new files in a directory */
	private final static int CHECKINTERVALL = 2000;
	
	/** Interface to the FacebookUploader */
	private IFacebookUploader uploader;
	
	/** Dialog to select directory */
	private DirectoryChooser dirChooser;
	
	/** Stage the Pane/Scene is located */
	private Stage primaryStage;
	
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
	
	/** Starts searching in the directory and uploading those pictures */
	public void start() {	
		uploader.start(pageNames.getValue(), CHECKINTERVALL, path.getText(), datePicker.getValue(), publishTimes.getText());
	}
	
	/** Stops searching in the directory and uploading those pictures */
	public void stop() {
		uploader.stop();
	}
	
	/** Dialog to chose directory for new picture uploads */
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
