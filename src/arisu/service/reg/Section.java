package arisu.service.reg;

import java.util.List;
import java.util.Objects;

public class Section {
	private int number;
	private boolean isClosed;
	private int student;
	private int maxStudent;
	private List<ClassSchedule> classSchedules;
	private List<String> instructors;
	private String note;
	
	public Section(int number, boolean isClosed, int student, int maxStudent, List<ClassSchedule> classSchedules,
			List<String> instructors, String note) {
		super();
		this.number = number;
		this.isClosed = isClosed;
		this.student = student;
		this.maxStudent = maxStudent;
		this.classSchedules = classSchedules;
		this.instructors = instructors;
		this.note = note;
	}
	
	public Section(int number, boolean isClosed, int student, int maxStudent, List<ClassSchedule> classSchedules,
			List<String> instructors) {
		this(number, isClosed, student, maxStudent, classSchedules, instructors, "");
	}

	public int getNumber() {
		return number;
	}

	public boolean isClosed() {
		return isClosed;
	}

	public int getStudent() {
		return student;
	}

	public int getMaxStudent() {
		return maxStudent;
	}

	public List<ClassSchedule> getClassSchedules() {
		return classSchedules;
	}

	public List<String> getInstructors() {
		return instructors;
	}

	public String getNote() {
		return note;
	}

	@Override
	public int hashCode() {
		return Objects.hash(classSchedules, instructors, isClosed, maxStudent, note, number, student);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Section other = (Section) obj;
		return Objects.equals(classSchedules, other.classSchedules) && Objects.equals(instructors, other.instructors)
				&& isClosed == other.isClosed && maxStudent == other.maxStudent && Objects.equals(note, other.note)
				&& number == other.number && student == other.student;
	}

	@Override
	public String toString() {
		return "Section [number=" + number + ", isClosed=" + isClosed + ", student=" + student + ", maxStudent="
				+ maxStudent + ", classSchedules=" + classSchedules + ", instructors=" + instructors + ", note=" + note
				+ "]";
	}
	
}
