package arisu.util;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;

/**
 * Collection of utility for UI system
 */
public class UIUtil {
	/**
	 * Fix blurry element that wrapped inside the ScrollPane
	 * @param scrollPanes A list of ScrollPane node to be fixed
	 * @see https://stackoverflow.com/a/50486726
	 */
	public static void fixBlurryScrollPane(ScrollPane ...scrollPanes) {
		for(ScrollPane each : scrollPanes) {
			each.skinProperty().addListener((obs, o, n) -> {
				StackPane stackPane = (StackPane) each.lookup("ScrollPane .viewport");
				stackPane.setCache(false);
			});
		}
	}
	
	
}
