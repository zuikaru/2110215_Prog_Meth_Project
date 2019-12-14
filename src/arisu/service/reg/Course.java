package arisu.service.reg;

import java.time.Year;
import java.util.Objects;

public class Course {
	private String number;
	private String name;
	private Semester semester;
	private Year academicYear;
	private StudyProgram studyProgram;

	public Course(String number, String name, Semester semester, Year academicYear, StudyProgram studyProgram) {
		this.number = number;
		this.name = name;
		this.semester = semester;
		this.academicYear = academicYear;
		this.studyProgram = studyProgram;
	}

	public String getNumber() {
		return this.number;
	}

	public String getName() {
		return this.name;
	}

	public Semester getSemester() {
		return this.semester;
	}

	public StudyProgram getStudyProgram() {
		return this.studyProgram;
	}

	public Year getAcademicYear() {
		return academicYear;
	}

	public void setAcademicYear(Year academicYear) {
		this.academicYear = academicYear;
	}

	public CourseDetailQuery courseDetailQuery() {
		return new CourseDetailQuery(this);
	}

	@Override
	public int hashCode() {
		return Objects.hash(academicYear, name, number, semester, studyProgram);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Course other = (Course) obj;
		return Objects.equals(academicYear, other.academicYear) && Objects.equals(name, other.name)
				&& Objects.equals(number, other.number) && semester == other.semester
				&& studyProgram == other.studyProgram;
	}

	@Override
	public String toString() {
		return "Course [number=" + number + ", name=" + name + ", semester=" + semester + ", academicYear="
				+ academicYear + ", studyProgram=" + studyProgram + "]";
	}

}
