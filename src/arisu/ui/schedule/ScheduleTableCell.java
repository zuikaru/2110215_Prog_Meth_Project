package arisu.ui.schedule;

import java.time.DayOfWeek;

import arisu.util.LocalTimeRange;

public interface ScheduleTableCell{
	DayOfWeek getDay();
	LocalTimeRange getTimeRange();
}
