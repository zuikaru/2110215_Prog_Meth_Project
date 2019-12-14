package arisu.ui.schedule;

import java.time.DayOfWeek;
import java.util.Objects;

import arisu.util.LocalTimeRange;

public class CellPosition implements ScheduleTableCell{
	private DayOfWeek day;
	private LocalTimeRange range;

	public CellPosition(DayOfWeek day, LocalTimeRange range) {
		this.day = day;
		this.range = range;
	}

	@Override
	public DayOfWeek getDay() {
		return day;
	}

	@Override
	public LocalTimeRange getTimeRange() {
		return range;
	}

	@Override
	public int hashCode() {
		return Objects.hash(day, range);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CellPosition other = (CellPosition) obj;
		return day == other.day && Objects.equals(range, other.range);
	}

	@Override
	public String toString() {
		return "CellPosition [day=" + day + ", range=" + range + "]";
	}
	
}
