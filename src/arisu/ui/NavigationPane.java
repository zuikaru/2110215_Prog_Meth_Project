package arisu.ui;

import arisu.Arisu;
import arisu.util.SimpleBackground;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

public class NavigationPane extends VBox{
	
	private final Scene scene;
	
	public NavigationPane(Arisu app) {
		this.scene = app.getScene();
		getChildren().addAll(
				new NavigationItem("Home", new Image("assets/menu/cute-home-64.png")),
				new NavigationItem("Course", new Image("assets/menu/cute-books-64.png")),
				new NavigationItem("Setting", new Image("assets/menu/cute-gear-64.png"))
		);
		setBackground(SimpleBackground.web("#34495e"));
		setMinWidth(150);
		setPrefHeight(this.scene.getHeight());
	    this.scene.heightProperty().addListener((obs, oldValue, newValue) -> {
	    	this.setPrefHeight(this.scene.getHeight());
	    });
	}
}
