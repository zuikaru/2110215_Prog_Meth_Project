package arisu.ui.schedule;

import arisu.util.SimpleBorder;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class LabelCell extends Label {
	public LabelCell(String text) {
		super(text);
		setAlignment(Pos.BASELINE_CENTER);
		setTextAlignment(TextAlignment.CENTER);
		setFont(Font.font(Font.getDefault().getFamily(), 12));
		setBorder(SimpleBorder.solid(Color.WHITESMOKE, 0.5));
		setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}

	public LabelCell(String text, double width, double height) {
		this(text);
		setPrefSize(width, height);
	}
}
