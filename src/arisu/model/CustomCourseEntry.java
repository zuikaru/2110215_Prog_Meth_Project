package arisu.model;

import java.time.DayOfWeek;
import java.util.List;

import arisu.ui.schedule.CourseCell;
import arisu.util.LocalTimeRange;

public class CustomCourseEntry extends CourseCellAdaptor{
	
	private final List<CourseCell> singleCourse;
	
	public CustomCourseEntry(DayOfWeek day, LocalTimeRange timeRange, String courseNumber, String courseName,
			int section,String building, String room, String teachingMethod) {
		this.singleCourse = List.<CourseCell>of(
				new CourseCell(day, timeRange, courseNumber, courseName, section, building, room, teachingMethod))
		;
	}
	
	public CustomCourseEntry(CourseCell courseCell) {
		this.singleCourse = List.<CourseCell>of(courseCell);
	}

	@Override
	public List<CourseCell> getCourseCells() {
		// TODO Auto-generated method stub
		return singleCourse;
	}

}
