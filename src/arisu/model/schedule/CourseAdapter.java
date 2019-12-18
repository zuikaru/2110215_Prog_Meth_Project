package arisu.model.schedule;

import java.util.List;

import arisu.ui.schedule.CourseCell;
import arisu.ui.schedule.ScheduleTableView;
import javafx.scene.control.ListCell;

public abstract class CourseAdapter implements ScheduleTableMutator {

	public abstract List<CourseCell> getCourseCells();

	public abstract void updateListCell(ListCell<CourseAdapter> listCell);

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
		for (CourseCell cell : getCourseCells()) {
			tableView.addCell(cell);
		}
	}

	@Override
	public void removeFrom(ScheduleTableView tableView) {
		for (CourseCell cell : getCourseCells()) {
			tableView.removeCell(cell);
		}
	}
}
