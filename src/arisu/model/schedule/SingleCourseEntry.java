package arisu.model.schedule;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import arisu.ui.schedule.CourseCell;
import arisu.util.LocalTimeRange;
import javafx.scene.control.ListCell;

public class SingleCourseEntry extends CourseAdaptor {

	private List<CourseCell> singleCourse;
	private CourseCell courseCell;

	public SingleCourseEntry(DayOfWeek day, LocalTimeRange timeRange, String courseNumber, String courseName,
			int section, String building, String room, String teachingMethod) {
		this(new CourseCell(day, timeRange, courseNumber, courseName, section, building, room, teachingMethod));
		courseCell.setOrigin(this);
	}

	public SingleCourseEntry(CourseCell courseCell) {
		this.courseCell = courseCell;
		this.courseCell.setOrigin(this);
		this.singleCourse = List.<CourseCell>of(courseCell);
	}

	@Override
	public List<CourseCell> getCourseCells() {
		return singleCourse;
	}

	public void setCourseCell(CourseCell courseCell) {
		this.courseCell = courseCell;
		this.courseCell.setOrigin(this);
		this.singleCourse = List.<CourseCell>of(courseCell);
	}

	public void modify(DayOfWeek day, LocalTimeRange timeRange, String courseNumber, String courseName, int section,
			String building, String room, String teachingMethod) {
		setCourseCell(
				new CourseCell(day, timeRange, courseNumber, courseName, section, building, room, teachingMethod));
	}

	@Override
	public void updateListCell(ListCell<CourseAdaptor> listCell) {
		String courseName = courseCell.getCourseName();
		if (courseName.length() > 15) {
			courseName = courseName.substring(0, 15) + "...";
		}
		listCell.setText(courseCell.getCourseNumber() + (!courseName.isBlank() ? " - " : "")
				+ courseName + " (" + courseCell.getDay().getDisplayName(TextStyle.SHORT, Locale.getDefault()) + ")");
	}

}
