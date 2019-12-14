package arisu.service.reg;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public class ClassSchedule {

	private String teachingMethod;
	private DayOfWeek day;
	private LocalTime start;
	private LocalTime end;
	private String building;
	private String room;
	private List<String> instructors;

	public ClassSchedule(String teachingMethod, DayOfWeek day, LocalTime start, LocalTime end, String building,
			String room, List<String> instructors) {
		this.teachingMethod = teachingMethod;
		this.day = day;
		this.start = start;
		this.end = end;
		this.building = building;
		this.room = room;
		this.instructors = instructors;
	}

	public ClassSchedule(String teachingMethod, DayOfWeek day, LocalTime start, LocalTime end, String building,
			String room, String instructor) {
		this(teachingMethod, day, start, end, building, room, List.of(instructor));
	}

	public ClassSchedule(DayOfWeek day, LocalTime start, LocalTime end, String building, String room,
			List<String> instructors) {
		this("LECT", day, start, end, building, room, instructors);
	}

	public ClassSchedule(DayOfWeek day, LocalTime start, LocalTime end, String building, String room,
			String instructor) {
		this("LECT", day, start, end, building, room, List.of(instructor));
	}

	public ClassSchedule(DayOfWeek day, LocalTime start, LocalTime end, String building, String room) {
		this("LECT", day, start, end, building, room, List.of("STAFF"));
	}

	public String getTeachingMethod() {
		return teachingMethod;
	}

	public DayOfWeek getDay() {
		return day;
	}

	public LocalTime getStart() {
		return start;
	}

	public LocalTime getEnd() {
		return end;
	}

	public String getBuilding() {
		return building;
	}

	public String getRoom() {
		return room;
	}

	public List<String> getInstructors() {
		return instructors;
	}

	@Override
	public int hashCode() {
		return Objects.hash(building, day, end, instructors, room, start, teachingMethod);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClassSchedule other = (ClassSchedule) obj;
		return Objects.equals(building, other.building) && day == other.day && Objects.equals(end, other.end)
				&& Objects.equals(instructors, other.instructors) && Objects.equals(room, other.room)
				&& Objects.equals(start, other.start) && Objects.equals(teachingMethod, other.teachingMethod);
	}

	@Override
	public String toString() {
		return "ClassSchedule [teachingMethod=" + teachingMethod + ", day=" + day + ", start=" + start + ", end=" + end
				+ ", building=" + building + ", room=" + room + ", instructors=" + instructors + "]";
	}

}
