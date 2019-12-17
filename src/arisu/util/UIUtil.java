package arisu.util;

import arisu.Arisu;
import arisu.ui.AlertHeader;
import javafx.beans.value.ChangeListener;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import jfxtras.styles.jmetro.JMetro;

/**
 * Collection of utility for UI system
 */
public class UIUtil {
	/**
	 * Fix blurry element that is wrapped inside the ScrollPane
	 * 
	 * @param scrollPanes A list of ScrollPane node to be fixed
	 * @see https://stackoverflow.com/a/50486726
	 */
	public static void fixBlurryScrollPane(ScrollPane... scrollPanes) {
		for (ScrollPane each : scrollPanes) {
			each.skinProperty().addListener((obs, o, n) -> {
				StackPane stackPane = (StackPane) each.lookup("ScrollPane .viewport");
				stackPane.setCache(false);
			});
		}
	}

	public static void styleNode(Parent parent) {
		new JMetro(parent, Arisu.app().getStyle());
		parent.getStyleClass().add("background");
	}

	public static Alert makeAlert(AlertType alertType, String contentText, ButtonType... buttons) {
		Alert alert = new Alert(alertType, contentText, buttons);
		alert.getDialogPane().setHeader(new AlertHeader(alert));
		UIUtil.styleNode(alert.getDialogPane());
		return alert;
	}

	public static Alert makeAlert(AlertType alertType) {
		return UIUtil.makeAlert(alertType, "");
	}

	public static void useSizeAdapter(Scene scene, Region node) {
		node.setPrefSize(scene.getWidth(), scene.getHeight());
		ChangeListener<Number> sizeListener = (observable, oldValue, newValue) -> node.setPrefSize(scene.getWidth(),
				scene.getHeight());
		scene.widthProperty().addListener(sizeListener);
		scene.heightProperty().addListener(sizeListener);
	}

}
