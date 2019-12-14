package arisu.ui;

import java.lang.reflect.Field;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.skin.ScrollPaneSkin;
import javafx.scene.layout.StackPane;

public class CustomScrollPaneSkin extends ScrollPaneSkin{
	public CustomScrollPaneSkin(ScrollPane scrollpane) {
		super(scrollpane);
		try {
			Field viewRectField = ScrollPaneSkin.class.getDeclaredField("viewRect");
			viewRectField.setAccessible(true);
			StackPane viewRect = (StackPane) viewRectField.get(this);
			viewRect.setCache(false);
		} catch (Exception e) {
			System.out.println("Failed to disable cache!");
			e.printStackTrace();
		}
	}
}
