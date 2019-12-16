package arisu.ui.schedule;

import java.time.DayOfWeek;

import arisu.model.CourseAdaptor;
import arisu.util.LocalTimeRange;

public class CourseCell extends LabelCell implements ScheduleTableCell{
	
	private DayOfWeek day;
	private LocalTimeRange timeRange;
	private String courseNumber;
	private String courseName;
	private int section;
	private String teachingMethod;
	private String building;
	private String room;
	private CourseAdaptor origin;
	
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
		String displayedRoom = (room != null) ? (room.isBlank() ? "" : String.format("(%s)", room)) : "";
		String firstLine = String.format("%s (%d)", courseNumber, section);
		String secondLine = courseName;
		String thirdLine = String.format("%s%s", building == null ? "" : building, displayedRoom);
		String result = firstLine + "\n" + secondLine + (thirdLine.isBlank() ? "" : "\n" + thirdLine);
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

	public String getCourseNumber() {
		return courseNumber;
	}

	public String getCourseName() {
		return courseName;
	}

	public int getSection() {
		return section;
	}

	public String getTeachingMethod() {
		return teachingMethod;
	}

	public String getBuilding() {
		return building;
	}

	public String getRoom() {
		return room;
	}
	
	public CourseAdaptor getOrigin() {
		return origin;
	}
	
	public void setOrigin(CourseAdaptor courseAdaptor) {
		this.origin = courseAdaptor;
	}

	@Override
	public String toString() {
		return "CourseCell [day=" + day + ", timeRange=" + timeRange + ", courseNumber=" + courseNumber
				+ ", courseName=" + courseName + ", section=" + section + ", teachingMethod=" + teachingMethod
				+ ", building=" + building + ", room=" + room + "]";
	}
	
}
