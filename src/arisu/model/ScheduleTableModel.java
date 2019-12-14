package arisu.model;

import java.time.DayOfWeek;

import arisu.ui.schedule.ClassSchedulePane;
import arisu.ui.schedule.CourseCell;
import arisu.ui.schedule.ScheduleTableView;
import arisu.util.LocalTimeRange;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class ScheduleTableModel {
	private ScheduleTableView tableView;
	private ClassSchedulePane schedulePane;
	private ObservableList<CourseCellAdaptor> courses;

	public ScheduleTableModel(ClassSchedulePane schedulePane) {
		this.schedulePane = schedulePane;
		this.tableView = schedulePane.getTableView();
		this.courses = FXCollections.<CourseCellAdaptor>observableArrayList();
		this.courses.addListener(this::onChanged);
		this.courses.add(
				new CustomCourseEntry(
						new CourseCell(DayOfWeek.FRIDAY, new LocalTimeRange(9,0,12,0), "2110215", "PROG METH I", 2))
		);
	}
	
	public void onChanged(ListChangeListener.Change<? extends CourseCellAdaptor> change) {
		while(change.next()) {
			if(change.wasAdded()) {
				for(CourseCellAdaptor cc : change.getAddedSubList()) {
					cc.addTo(tableView);
				}
			}
			else if(change.wasRemoved()) {
				for(CourseCellAdaptor cc : change.getRemoved()) {
					cc.removeFrom(tableView);
				}
			}
		}
	}
	
	public ObservableList<CourseCellAdaptor> getCourses(){
		return courses;
	}
	
}
