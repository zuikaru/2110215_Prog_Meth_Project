package arisu.ui.schedule;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import arisu.util.LocalTimeRange;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.util.converter.DateTimeStringConverter;

public class TimePicker extends HBox {
	private TextField start;
	private TextField end;
	private final SimpleDateFormat format = new SimpleDateFormat("HH:mm");

	public TimePicker() {
		setAlignment(Pos.BASELINE_LEFT);
		setSpacing(8);
		start = new TextField("08:00");
		start.setPrefWidth(60);
		start.setTextFormatter(new TextFormatter<>(new DateTimeStringConverter(format)));
		start.setText("08:00");
		end = new TextField("16:00");
		end.setPrefWidth(60);
		end.setTextFormatter(new TextFormatter<>(new DateTimeStringConverter(format)));
		end.setText("16:00");
		getChildren().addAll(start, end);
	}

	public LocalTimeRange getTimeRange() {
		String startText = start.getText().strip();
		String endText = end.getText().strip();
		if (startText.length() == 0 || endText.length() == 0) {
			throw new IllegalArgumentException("Time must not be empty!");
		}
		try {
			return new LocalTimeRange(LocalTime.parse(startText), LocalTime.parse(endText));
		} catch (DateTimeParseException ex) {
			throw new IllegalArgumentException("Invalid time format! Time must be in format of HH:mm");
		}
	}

	public TextField getStart() {
		return start;
	}

	public TextField getEnd() {
		return end;
	}
}
