package arisu.ui.navigation;

import arisu.Arisu;
import arisu.util.SimpleBackground;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

public class NavigationPane extends VBox {

	private final Scene scene;

	public NavigationPane(Arisu app) {
		this.scene = app.getScene();
		getChildren().addAll(new NavigationItem("Edit", new Image("assets/menu/design.png")),
				new NavigationItem("Browse", new Image("assets/menu/books.png")));
		setBackground(SimpleBackground.web("#34495e"));
		setMinWidth(150);
		setPrefHeight(this.scene.getHeight());
		this.scene.heightProperty().addListener((obs, oldValue, newValue) -> {
			this.setPrefHeight(this.scene.getHeight());
		});
	}
}
