package arisu.ui;

import arisu.Arisu;
import arisu.util.SimpleBackground;
import arisu.util.UIUtil;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

public class EmptySpacePane extends StackPane {
	private Scene scene;
	private Node center;

	public EmptySpacePane(Arisu app, String text) {
		this(app, newLabel(text));
	}

	public EmptySpacePane(Arisu app, Node node) {
		this.scene = app.getScene();
		center = node;
		setBackground(SimpleBackground.web("#7f8c8d"));
		setAlignment(Pos.CENTER);
		getChildren().add(center);
		UIUtil.useSizeAdapter(scene, this);
	}

	public EmptySpacePane(Arisu app) {
		this(app, "It's empty here.");
	}

	private static Label newLabel(String text) {
		Label label = new Label(text);
		label.setFont(Font.font(32));
		return label;
	}
}
