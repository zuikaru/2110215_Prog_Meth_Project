package arisu.model;

import java.util.ArrayList;
import java.util.List;

import arisu.service.reg.ClassSchedule;
import arisu.service.reg.Course;
import arisu.service.reg.CourseDetail;
import arisu.service.reg.Section;
import arisu.ui.schedule.CourseCell;
import arisu.util.LocalTimeRange;
import javafx.scene.control.ListCell;

public class RegCourseEntry extends CourseAdaptor{
	
	private List<CourseCell> courseCells;
	private Course course;
	private CourseDetail detail;
	private Section section;
	
	public RegCourseEntry(Course course, CourseDetail detail, Section section) {
		this.courseCells = new ArrayList<>();
		this.course = course;
		this.detail = detail;
		this.section = section;
		for(ClassSchedule schedule : section.getClassSchedules()) {
			CourseCell each = new CourseCell(
					schedule.getDay(), new LocalTimeRange(schedule.getStart(), schedule.getEnd()), 
					course.getNumber(), course.getName(), section.getNumber());
			each.setOrigin(this);
			courseCells.add(each);
		}
	}

	@Override
	public List<CourseCell> getCourseCells() {
		return courseCells;
	}

	@Override
	public void updateListCell(ListCell<CourseAdaptor> listCell) {
		listCell.setText(course.getNumber() + " - " + course.getName());
	}
}
