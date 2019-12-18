package arisu;

import java.util.logging.Logger;

import arisu.ui.ContentPane;
import arisu.ui.browser.CourseBrowserPane;
import arisu.ui.navigation.NavigationPane;
import arisu.ui.schedule.ClassSchedulePane;
import arisu.util.UIUtil;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.Style;

public class Arisu extends Application {

	private static Arisu instance;

	private Style style = Style.DARK;
	private Stage stage;
	private Scene scene;
	private ContentPane content;
	private HBox root;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		// static reference to application
		instance = this;
		stage = primaryStage;
		// Root
		root = new HBox();
		UIUtil.styleNode(root);
		scene = new Scene(root, 1120, 630);
		stage.setMaxWidth(1280);
		stage.setMaxHeight(720);
		// Components
		NavigationPane navigation = new NavigationPane(this);
		content = new ContentPane(this);
		content.add("edit", new ClassSchedulePane(this));
		content.add("browse", new CourseBrowserPane(this));
		content.setCurrent("edit");
		root.getChildren().addAll(navigation, content);
		primaryStage.setTitle("Class Schedule Planner");
		primaryStage.setScene(scene);
		primaryStage.getIcons().addAll(new Image(ClassLoader.getSystemResource("app-icon.png").toString()));
		primaryStage.show();
	}

	public static Arisu app() {
		return instance;
	}

	public HBox getRoot() {
		return this.root;
	}

	public Scene getScene() {
		return this.scene;
	}

	public Stage getStage() {
		return this.stage;
	}

	public ContentPane getContentPane() {
		return this.content;
	}

	public Style getStyle() {
		return style;
	}
	
	public static Logger logger() {
		return Logger.getLogger("Arisu");
	}

}
