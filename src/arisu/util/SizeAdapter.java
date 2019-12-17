package arisu.util;

import javafx.beans.value.ChangeListener;
import javafx.scene.Scene;
import javafx.scene.layout.Region;

public class SizeAdapter {

	public static void use(Scene scene, Region node) {
		node.setPrefSize(scene.getWidth(), scene.getHeight());
		ChangeListener<Number> sizeListener = (observable, oldValue, newValue) -> {
			node.setPrefSize(scene.getWidth(), scene.getHeight());
		};
		scene.widthProperty().addListener(sizeListener);
		scene.heightProperty().addListener(sizeListener);
	}

}
