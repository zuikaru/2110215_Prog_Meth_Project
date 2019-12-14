package arisu.model;

import java.util.ArrayList;
import java.util.List;

import arisu.service.reg.ClassSchedule;
import arisu.service.reg.Course;
import arisu.service.reg.CourseDetail;
import arisu.service.reg.Section;
import arisu.ui.schedule.CourseCell;
import arisu.ui.schedule.ScheduleTableView;
import arisu.util.LocalTimeRange;

public class CourseEntry extends CourseCellAdaptor{
	
	private List<CourseCell> courseCells;
	private Course course;
	private CourseDetail detail;
	private Section section;
	
	public CourseEntry(Course course, CourseDetail detail, Section section) {
		this.courseCells = new ArrayList<>();
		this.course = course;
		this.detail = detail;
		this.section = section;
		for(ClassSchedule schedule : section.getClassSchedules()) {
			CourseCell each = new CourseCell(
					schedule.getDay(), new LocalTimeRange(schedule.getStart(), schedule.getEnd()), 
					course.getNumber(), course.getName(), section.getNumber());
			courseCells.add(each);
		}
	}

	@Override
	public List<CourseCell> getCourseCells() {
		return courseCells;
	}
}
