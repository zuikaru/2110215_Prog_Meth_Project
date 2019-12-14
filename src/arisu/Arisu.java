package arisu;

import arisu.ui.ContentPane;
import arisu.ui.CoursePane;
import arisu.ui.EmptySpacePane;
import arisu.ui.NavigationPane;
import arisu.ui.schedule.ClassSchedulePane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

public class Arisu extends Application{
	
	private static Arisu instance;
	
	private Style style = Style.DARK;
	private Stage stage;
	private Scene scene;
	private NavigationPane navigation;
	private ContentPane content;
	private HBox root;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// static reference to application
		instance = this;
		stage = primaryStage;
		// Root
		root = new HBox();
		new JMetro(root, style);
		scene = new Scene(root, 960, 540);
		// Components
		navigation = new NavigationPane(this);
		content = new ContentPane(this);
		content.add("home", new ClassSchedulePane(this));
		content.add("course", new CoursePane(this));
		content.add("setting", new EmptySpacePane(this, "Setting"));
		content.setCurrent("home");
		root.getChildren().addAll(navigation, content);
		root.getStyleClass().add("background");
		primaryStage.setTitle("Class Schedule Planner");
		primaryStage.setScene(scene);
		primaryStage.getIcons().addAll(
				new Image(ClassLoader.getSystemResource("app-icon-64.png").toString()));
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
	
	public ContentPane getContentPane() {
		return this.content;
	}
	
	public Style getStyle() {
		return style;
	}

}
