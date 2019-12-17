package arisu.util;

import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;

public class SimpleBorder {
	public static Border solid(Paint paint, double thickness) {
		return solid(paint, new BorderWidths(thickness));
	}

	public static Border solid(Paint paint, BorderWidths widths) {
		return new Border(new BorderStroke(paint, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, widths));
	}
}
