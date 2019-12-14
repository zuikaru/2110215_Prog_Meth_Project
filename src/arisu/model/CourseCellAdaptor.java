package arisu.model;

import java.util.List;

import arisu.ui.schedule.CourseCell;
import arisu.ui.schedule.ScheduleTableView;

public abstract class CourseCellAdaptor implements ScheduleTableMutator{
	
	public abstract List<CourseCell> getCourseCells();

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
