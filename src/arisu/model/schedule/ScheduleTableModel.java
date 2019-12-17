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
	private ClassSchedulePane schedulePane;
	private ObservableList<CourseAdaptor> courses;
	private Map<DayOfWeek, Map<LocalTimeRange, CourseAdaptor>> usedTimes;

	public ScheduleTableModel(ClassSchedulePane schedulePane) {
		this.schedulePane = schedulePane;
		this.tableView = schedulePane.getTableView();
		this.courses = FXCollections.<CourseAdaptor>observableArrayList();
		this.courses.addListener(this::onChanged);
		this.usedTimes = new HashMap<>();
		for (DayOfWeek day : DayOfWeek.values()) {
			if (day.equals(DayOfWeek.SUNDAY))
				continue;
			usedTimes.put(day, new HashMap<>());
		}
	}

	public void addAll(CourseAdaptor... courseAdaptors) {
		for (CourseAdaptor courseAdaptor : courseAdaptors) {
			add(courseAdaptor);
		}
	}

	public void add(CourseAdaptor courseAdaptor) {
		markUsedTime(courseAdaptor);
		this.courses.add(courseAdaptor);
	}

	public void remove(CourseAdaptor courseAdaptor) {
		freeUsedTime(courseAdaptor);
		this.courses.remove(courseAdaptor);
	}

	public void replace(CourseAdaptor oldVal, CourseAdaptor newVal) {
		freeUsedTime(oldVal);
		int index = this.courses.indexOf(oldVal);
		this.courses.remove(index);
		markUsedTime(newVal);
		this.courses.add(index, newVal);

	}

	public void sort() {
		FXCollections.sort(courses, Comparator.comparing(CourseAdaptor::getCourseNumber));
	}

	private void markUsedTime(CourseAdaptor courseAdaptor) {
		for (CourseCell cell : courseAdaptor.getCourseCells()) {
			usedTimes.get(cell.getDay()).put(cell.getTimeRange(), courseAdaptor);
		}
	}

	private void freeUsedTime(CourseAdaptor courseAdaptor) {
		for (CourseCell cell : courseAdaptor.getCourseCells()) {
			usedTimes.get(cell.getDay()).remove(cell.getTimeRange());
		}
	}

	public void onChanged(ListChangeListener.Change<? extends CourseAdaptor> change) {
		while (change.next()) {
			if (change.wasAdded()) {
				for (CourseAdaptor cc : change.getAddedSubList()) {
					cc.addTo(tableView);
				}
			} else if (change.wasRemoved()) {
				for (CourseAdaptor cc : change.getRemoved()) {
					cc.removeFrom(tableView);
				}
			}
		}
	}

	public ObservableList<CourseAdaptor> getCourses() {
		return courses;
	}

	public boolean newItemConflict(CourseAdaptor input) {
		// For each cell
		for (CourseCell cell : input.getCourseCells()) {
			// Key: time range, Value: owner
			Map<LocalTimeRange, CourseAdaptor> times = usedTimes.get(cell.getDay());
			// check if it overlap with some other course
			for (LocalTimeRange time : times.keySet()) {
				if (cell.getTimeRange().overlapWith(time) && !input.equals(times.get(time))) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean oldItemConflict(Map<DayOfWeek, LocalTimeRange> times, CourseAdaptor except) {
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

	public boolean oldItemConflict(DayOfWeek dayOfWeek, LocalTimeRange timeRange, CourseAdaptor except) {
		return oldItemConflict(Map.of(dayOfWeek, timeRange), except);
	}

}
