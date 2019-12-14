package arisu.model;

import arisu.ui.schedule.ScheduleTableView;

public interface ScheduleTableMutator {
	void addTo(ScheduleTableView tableView);
	void removeFrom(ScheduleTableView tableView);
}
