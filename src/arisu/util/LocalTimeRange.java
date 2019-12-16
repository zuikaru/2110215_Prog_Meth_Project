package arisu.util;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.TemporalUnit;
import java.util.Objects;

public class LocalTimeRange {
	
	private LocalTime start;
	
	private LocalTime end;
	
	public LocalTimeRange(LocalTime start, LocalTime end) {
		this.start = start;
		this.end = end;
		if(!isValidRange()) {
			throw new IllegalArgumentException("Start time must come before end time!");
		}
	}
	
	public LocalTimeRange(LocalTimeRange range) {
		this(range.start, range.end);
	}
	
	public LocalTimeRange(LocalTime start, Duration duration) {
		this(start, start.plus(duration));
	}
	
	public LocalTimeRange(int hours1, int minutes1, int hours2, int minutes2) {
		this(LocalTime.of(hours1, minutes1), LocalTime.of(hours2, minutes2));
	}
	public LocalTime start() {
		return start;
	}
	
	public LocalTime end() {
		return end;
	}
	
	public long duration(TemporalUnit unit) {
		return start.until(end, unit);
	}
	
	public boolean isValidRange() {
		return start.isBefore(end);
	}
	
	public boolean overlapWith(LocalTimeRange another) {
		if(this.start.isBefore(another.start)) {
			return this.end.isAfter(another.start);
		}
		else {
			return another.end.isAfter(this.start);
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(end, start);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LocalTimeRange other = (LocalTimeRange) obj;
		return Objects.equals(end, other.end) && Objects.equals(start, other.start);
	}

	@Override
	public String toString() {
		return "LocalTimeRange [start=" + start + ", end=" + end + "]";
	}
	
	
}
