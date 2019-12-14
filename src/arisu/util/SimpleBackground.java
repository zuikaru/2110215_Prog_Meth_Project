package arisu.util;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class SimpleBackground {
	public static Background paint(Paint paint) {
		return new Background(new BackgroundFill(paint, CornerRadii.EMPTY, null));
	}
	
	public static Background rgb(int red, int green, int blue, int opacity) {
		return paint(Color.rgb(red, green, blue, opacity));
	}
	
	public static Background rgb(int red, int green, int blue) {
		return paint(Color.rgb(red, green, blue));
	}
	
	public static Background web(String colorString, double opacity) {
		return paint(Color.web(colorString, opacity ));
	}
	
	public static Background web(String colorString) {
		return paint(Color.web(colorString));
	}
}
