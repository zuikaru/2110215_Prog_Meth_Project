package arisu.ui.schedule;

import java.time.DayOfWeek;

import arisu.util.LocalTimeRange;
import javafx.scene.control.Label;

public class CourseCell extends LabelCell implements ScheduleTableCell{
	
	private DayOfWeek day;
	private LocalTimeRange timeRange;
	private String courseNumber;
	private String courseName;
	private int section;
	private String teachingMethod;
	private String building;
	private String room;
	
	public CourseCell(DayOfWeek day, LocalTimeRange timeRange, String courseNumber, String courseName,
			int section,String building, String room, String teachingMethod) {
		super("");
		this.day = day;
		this.timeRange = timeRange;
		this.courseNumber = courseNumber;
		this.courseName = courseName;
		this.section = section;
		this.teachingMethod = teachingMethod;
		this.building = building;
		this.room = room;
		setText(buildText());
	}
	
	public CourseCell(DayOfWeek day, LocalTimeRange timeRange, String courseNumber, String courseName,
			int section) {
		this(day, timeRange, courseNumber, courseName, section, null, null, null);
	}

	private String buildText() {
		String firstLine = String.format("%s (%d)", courseNumber, section);
		String secondLine = String.format("\n%s", courseName);
		String thirdLine = String.format("\n%s %s(%s)", teachingMethod, building, room);
		String result = firstLine + secondLine;
		if(!(building == null && room == null && teachingMethod == null)) result += thirdLine;
		return result;
	}

	@Override
	public DayOfWeek getDay() {
		return day;
	}

	@Override
	public LocalTimeRange getTimeRange() {
		return timeRange;
	}
}
