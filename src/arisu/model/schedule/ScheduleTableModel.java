package arisu.model.schedule;

import java.time.DayOfWeek;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import arisu.ui.schedule.ClassSchedulePane;
import arisu.ui.schedule.CourseCell;
import arisu.ui.schedule.ScheduleTableView;
import arisu.util.LocalTimeRange;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class ScheduleTableModel {
	private ScheduleTableView tableView;
	private ObservableList<CourseAdapter> courses;
	private Map<DayOfWeek, Map<LocalTimeRange, CourseAdapter>> usedTimes;

	public ScheduleTableModel(ClassSchedulePane schedulePane) {
		this.tableView = schedulePane.getTableView();
		this.courses = FXCollections.<CourseAdapter>observableArrayList();
		this.courses.addListener(this::onChanged);
		this.usedTimes = new HashMap<>();
		for (DayOfWeek day : DayOfWeek.values()) {
			if (day.equals(DayOfWeek.SUNDAY))
				continue;
			usedTimes.put(day, new HashMap<>());
		}
	}

	public void addAll(CourseAdapter... courseAdapters) {
		for (CourseAdapter courseAdapter : courseAdapters) {
			add(courseAdapter);
		}
	}

	public void add(CourseAdapter courseAdapter) {
		markUsedTime(courseAdapter);
		this.courses.add(courseAdapter);
	}

	public void remove(CourseAdapter courseAdapter) {
		freeUsedTime(courseAdapter);
		this.courses.remove(courseAdapter);
	}

	public void replace(CourseAdapter oldVal, CourseAdapter newVal) {
		freeUsedTime(oldVal);
		int index = this.courses.indexOf(oldVal);
		this.courses.remove(index);
		markUsedTime(newVal);
		this.courses.add(index, newVal);

	}

	public void sort() {
		FXCollections.sort(courses, Comparator.comparing(CourseAdapter::getCourseNumber));
	}

	private void markUsedTime(CourseAdapter courseAdaptor) {
		for (CourseCell cell : courseAdaptor.getCourseCells()) {
			usedTimes.get(cell.getDay()).put(cell.getTimeRange(), courseAdaptor);
		}
	}

	private void freeUsedTime(CourseAdapter courseAdaptor) {
		for (CourseCell cell : courseAdaptor.getCourseCells()) {
			usedTimes.get(cell.getDay()).remove(cell.getTimeRange());
		}
	}

	public void onChanged(ListChangeListener.Change<? extends CourseAdapter> change) {
		while (change.next()) {
			if (change.wasAdded()) {
				for (CourseAdapter cc : change.getAddedSubList()) {
					cc.addTo(tableView);
				}
			} else if (change.wasRemoved()) {
				for (CourseAdapter cc : change.getRemoved()) {
					cc.removeFrom(tableView);
				}
			}
		}
	}

	public ObservableList<CourseAdapter> getCourses() {
		return courses;
	}

	public boolean newItemConflict(CourseAdapter input) {
		// For each cell
		for (CourseCell cell : input.getCourseCells()) {
			// Key: time range, Value: owner
			Map<LocalTimeRange, CourseAdapter> times = usedTimes.get(cell.getDay());
			// check if it overlap with some other course
			for (LocalTimeRange time : times.keySet()) {
				if (cell.getTimeRange().overlapWith(time) && !input.equals(times.get(time))) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean oldItemConflict(Map<DayOfWeek, LocalTimeRange> times, CourseAdapter except) {
		// For each cell
		for (DayOfWeek day : times.keySet()) {
			LocalTimeRange timeRequired = times.get(day);
			for (LocalTimeRange timeUsed : usedTimes.get(day).keySet()) {
				if (timeRequired.overlapWith(timeUsed) && usedTimes.get(day).get(timeUsed) != except) {
					return true;
				}
			}

		}
		return false;
	}

	public boolean oldItemConflict(DayOfWeek dayOfWeek, LocalTimeRange timeRange, CourseAdapter except) {
		return oldItemConflict(Map.of(dayOfWeek, timeRange), except);
	}

}
