package arisu.model;

import java.util.List;

import arisu.ui.schedule.CourseCell;
import arisu.ui.schedule.ScheduleTableView;
import javafx.scene.control.ListCell;

public abstract class CourseAdaptor implements ScheduleTableMutator{
	
	public abstract List<CourseCell> getCourseCells();
	
	public abstract void updateListCell(ListCell<CourseAdaptor> listCell);
	
	public String getCourseName() {
		return getFirstCourseCell().getCourseName();
	}
	
	public String getCourseNumber() {
		return getFirstCourseCell().getCourseNumber();
	}
	
	public CourseCell getFirstCourseCell() {
		return this.getCourseCells().get(0);
	}

	@Override
	public void addTo(ScheduleTableView tableView) {
		for(CourseCell cell : getCourseCells()) {
			tableView.addCell(cell);
		}
	}

	@Override
	public void removeFrom(ScheduleTableView tableView) {
		for(CourseCell cell : getCourseCells()) {
			tableView.removeCell(cell);
		}
	}
}
