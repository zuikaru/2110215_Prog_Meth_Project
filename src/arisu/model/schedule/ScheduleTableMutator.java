package arisu.model.schedule;

import arisu.ui.schedule.ScheduleTableView;

public interface ScheduleTableMutator {
	void addTo(ScheduleTableView tableView);

	void removeFrom(ScheduleTableView tableView);
}
