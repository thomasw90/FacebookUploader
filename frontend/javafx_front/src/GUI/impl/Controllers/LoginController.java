package GUI.impl.Controllers;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.Version;
import com.restfb.scope.FacebookPermissions;
import com.restfb.scope.ScopeBuilder;

import GUI.PaneSwitcher;
import GUI.impl.ScreenType;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import backend.IFacebookUploader;
import util.Storage;

public class LoginController implements IController {

	private static final String APP_ID = "2128746860732929";

	private static final String APP_SECRET = "5b9cf135900da933868e33ba717bf482";

	private static final String SUCCESS_URL = "https://www.facebook.com/connect/login_success.html";

	/** Stage the Pane/Scene is located */
	private Stage primaryStage;

	/** Is used to switch Pane on login */
	private PaneSwitcher switcher;

	/** Interface to the FacebookUploader */
	private IFacebookUploader uploader;

	@FXML
	private Text notification;

	@Override
	public void init(Stage primaryStage, PaneSwitcher switcher, IFacebookUploader uploader) {
		this.primaryStage = primaryStage;
		this.switcher = switcher;
		this.uploader = uploader;
	}
	
	@Override
	public void activated() {		
        if(uploader.login(Storage.getInstance().getAccessToken())) {
        	switcher.changeScreen(ScreenType.UPLOADER);
        }
	}

	/**
	 * Tries to login with Access-Token. Changes Pane on success, throws error on
	 * failure
	 */
	public void login() {
		ScopeBuilder scope = new ScopeBuilder();
		scope.addPermission(FacebookPermissions.PAGES_MANAGE_POSTS);
		DefaultFacebookClient facebookClient = new DefaultFacebookClient(Version.LATEST);
		String loginDialogUrl = facebookClient.getLoginDialogUrl(APP_ID, SUCCESS_URL, scope);
		WebView webView = new WebView();
		WebEngine webEngine = webView.getEngine();
		primaryStage.setScene(new Scene(new VBox(webView), 1000, 600, Color.web("#666970")));
		webEngine.load(loginDialogUrl + "&display=popup&response_type=code");
		webEngine.locationProperty().addListener((property, oldValue, newValue) -> {
			if (newValue.startsWith(SUCCESS_URL)) {
				int codeOffset = newValue.indexOf("code=");
				String code = newValue.substring(codeOffset + "code=".length());
				AccessToken accessToken;
				try {
					accessToken = facebookClient.obtainUserAccessToken(APP_ID, APP_SECRET, SUCCESS_URL, code);
					accessToken = facebookClient.obtainExtendedAccessToken(APP_ID, APP_SECRET, accessToken.getAccessToken());
					if (uploader.login(accessToken.getAccessToken())) {
						Storage.getInstance().setAccessToken(accessToken.getAccessToken());
						switcher.changeScreen(ScreenType.UPLOADER);
					}
				} catch (Exception t) {
					notification.setText("Login failed");
					notification.setFill(Color.RED);
					switcher.changeScreen(ScreenType.LOGIN);
				}
			}
		});
	}
}
