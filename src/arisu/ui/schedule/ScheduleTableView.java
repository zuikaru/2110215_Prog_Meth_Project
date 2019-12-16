package arisu.ui.schedule;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.Locale;

import arisu.util.DayOfWeekColor;
import arisu.util.LocalTimeRange;
import arisu.util.SimpleBackground;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ScheduleTableView extends GridPane{
	static final int DEFAULT_CELL_WIDTH = 100;
	static final int DEFAULT_CELL_HEIGHT = 75;
	static final LocalTime TIME_LOWER_LIMIT = LocalTime.of(8, 0);
	static final LocalTime TIME_UPPER_LIMIT = LocalTime.of(16, 0);
	private ClassSchedulePane schedulePane;
	
	public ScheduleTableView(ClassSchedulePane schedulePane) {
		this.schedulePane = schedulePane;
		setBackground(SimpleBackground.web("#333"));
		initDayOfWeek();
		initTime();
	}
	
	public void addCell(ScheduleTableCell cell) {
		DayOfWeek day = cell.getDay(); 
		LocalTimeRange range = cell.getTimeRange();
		if(cell instanceof Node) {
			addNodeAt((Node)cell, day, range);
		}
		else {
			throw new IllegalArgumentException("Argument cell must be a subclass of Node!");
		}
	}
	public void addAllCell(ScheduleTableCell ...cells) {
		for(ScheduleTableCell cell : cells)
			addCell(cell);
	}
	
	public void removeCell(ScheduleTableCell cell) {
		DayOfWeek day = cell.getDay(); 
		LocalTimeRange range = cell.getTimeRange();
		removeNodeAt(day, range);
	}
	
	public void addNodeAt(Node node, DayOfWeek day, LocalTimeRange range) {
		validateArgs(day, range);
		int row = calculateRow(day, range);
		int col = calculateCol(day, range);
		int colspan = calculateColSpan(day, range);
		add(node, col, row, colspan, 1);
	}
	
	public int removeNodeAt(DayOfWeek day, LocalTimeRange range) {
		validateArgs(day, range);
		int row = calculateRow(day, range);
		int col = calculateCol(day, range);
		Iterator<Node> it = getChildren().iterator();
		int removed = 0;
		while(it.hasNext()) {
			Node child = it.next();
			Integer rowIndex = getRowIndex(child);
			Integer colIndex = getColumnIndex(child);
			if(rowIndex != null && colIndex != null) {
				if(rowIndex == row && colIndex == col) {
					it.remove();
					removed++;
				}
			}
		}
		return removed;
	}
	
	private void validateArgs(DayOfWeek day, LocalTimeRange range) {
		if(day.equals(DayOfWeek.SUNDAY)) {
			throw new IllegalArgumentException("Sunday is not supported!");
		}
		if(range.start().isBefore(TIME_LOWER_LIMIT) || range.end().isAfter(TIME_UPPER_LIMIT)) {
			throw new IllegalArgumentException("Only times greater than 08:00 and lesser than 16:00 are supported!");
		}
	}
	
	private int calculateRow(DayOfWeek day, LocalTimeRange range) {
		return day.getValue() + 1;
	}
	
	private int calculateCol(DayOfWeek day, LocalTimeRange range) {
		return 1 + (int) (TIME_LOWER_LIMIT.until(range.start(), ChronoUnit.MINUTES)/30);
	}
	
	private int calculateColSpan(DayOfWeek day, LocalTimeRange range) {
		int colspan = (int) (range.duration(ChronoUnit.MINUTES)/30);
		if(colspan <= 0) colspan = 1;
		return colspan;
	}
	
	private void initDayOfWeek() {
		for(DayOfWeek day : DayOfWeek.values()) {
			if(day.equals(DayOfWeek.SUNDAY)) continue;
			String text = day.getDisplayName(TextStyle.SHORT, Locale.getDefault()).toUpperCase();
			//Label label = new LabelCell(text, DEFAULT_CELL_WIDTH/2, DEFAULT_CELL_HEIGHT);
			Label label = new LabelCell(text);
			label.setMinSize( DEFAULT_CELL_WIDTH/2, DEFAULT_CELL_HEIGHT);
			label.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 12));
			Color background = DayOfWeekColor.of(day).getColor();
			label.setBackground(SimpleBackground.paint(background));
			add(label, 0, 1 + day.getValue());
		}
	}
	
	private void initTime() {
		LocalTime start = TIME_LOWER_LIMIT;
		LocalTime end = TIME_UPPER_LIMIT;
		int i = 1;
		while(start.isBefore(end)) {
			LocalTime next = start.plusMinutes(60);
			String time = start.toString() + "-" + next.toString();
			Label label = new LabelCell(time, DEFAULT_CELL_WIDTH, DEFAULT_CELL_HEIGHT/1.5);
			label.setMinSize(DEFAULT_CELL_WIDTH, DEFAULT_CELL_HEIGHT/1.5);
			label.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 12));
			label.setBackground(SimpleBackground.web("#34495e"));
			Label helper = new LabelCell(String.valueOf(2*i - 1), DEFAULT_CELL_WIDTH/2, 20);
			helper.setMinSize(DEFAULT_CELL_WIDTH/2, 20);
			Label helper2 = new LabelCell(String.valueOf(2*i), DEFAULT_CELL_WIDTH/2, 20);
			helper2.setMinSize(DEFAULT_CELL_WIDTH/2, 20);
			add(label, 2*i - 1, 0, 2, 1);
			add(helper, 2*i - 1, 1);
			add(helper2, 2*i, 1);
			start = next;
			i++;
		}
	}
	
}
