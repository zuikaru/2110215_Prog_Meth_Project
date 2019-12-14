package arisu.util;

import java.time.DayOfWeek;

import javafx.scene.paint.Color;

public enum DayOfWeekColor {
	MONDAY(Color.web("#f1c40f")),
	TUESDAY(Color.web("#f368e0")),
	WEDNESDAY(Color.web("#2ecc71")),
	THURSDAY(Color.web("#e67e22")),
	FRIDAY(Color.web("#3498db")),
	SATURDAY(Color.web("#9b59b6")),
	SUNDAY(Color.web("#c0392b")),
	;
	Color color;
	private DayOfWeekColor(Color color) {
		this.color = color;
	}
	public Color getColor() {
		return color;
	}
	public static DayOfWeekColor of(DayOfWeek day) {
		String enumName = day.name();
		return DayOfWeekColor.valueOf(enumName);
	}
}
