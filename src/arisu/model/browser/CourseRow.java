package arisu.model.browser;

import java.time.DayOfWeek;
import java.util.Objects;

import arisu.model.schedule.SingleCourseEntry;
import arisu.util.LocalTimeRange;

public class CourseRow {
	private String courseNumber = "";
	private String courseName = "";
	private String fullCourseName = "";
	private int credit = -1;
	private int section = -1;
	private boolean isClosed = false;
	private DayOfWeek day = null;
	private LocalTimeRange timeRange = null;
	private String building = "";
	private String room = "";
	private String instructor = "";
	private int student = -1;
	private int maxStudent = -1;
	private String note = "";

	public CourseRow(String courseNumber, String courseName, int credit, int section, DayOfWeek day,
			LocalTimeRange timeRange, String building, String room, String instructor, int student, int maxStudent,
			String note) {
		this.courseNumber = courseNumber;
		this.courseName = courseName;
		this.credit = credit;
		this.section = section;
		this.day = day;
		this.timeRange = timeRange;
		this.building = building;
		this.room = room;
		this.instructor = instructor;
		this.student = student;
		this.maxStudent = maxStudent;
		this.note = note;
	}

	public CourseRow(String courseNumber, String courseName, int credit) {
		this.courseNumber = courseNumber;
		this.courseName = courseName;
		this.credit = credit;
	}

	public CourseRow() {

	}

	public SingleCourseEntry toSingleCourseEntry() {
		return new SingleCourseEntry(day, timeRange, courseNumber, courseName, section, building, room, null);
	}

	public String getCourseNumber() {
		return courseNumber;
	}

	public void setCourseNumber(String courseNumber) {
		this.courseNumber = courseNumber;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public int getSection() {
		return section;
	}

	public void setSection(int section) {
		this.section = section;
	}

	public DayOfWeek getDay() {
		return day;
	}

	public void setDay(DayOfWeek day) {
		this.day = day;
	}

	public LocalTimeRange getTimeRange() {
		return timeRange;
	}

	public void setTimeRange(LocalTimeRange timeRange) {
		this.timeRange = timeRange;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getInstructor() {
		return instructor;
	}

	public void setInstructor(String instructor) {
		this.instructor = instructor;
	}

	public int getStudent() {
		return student;
	}

	public void setStudent(int student) {
		this.student = student;
	}

	public int getMaxStudent() {
		return maxStudent;
	}

	public void setMaxStudent(int maxStudent) {
		this.maxStudent = maxStudent;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getFullCourseName() {
		return fullCourseName;
	}

	public void setFullCourseName(String fullCourseName) {
		this.fullCourseName = fullCourseName;
	}

	public boolean isClosed() {
		return isClosed;
	}

	public void setClosed(boolean isClosed) {
		this.isClosed = isClosed;
	}

	@Override
	public int hashCode() {
		return Objects.hash(building, courseName, courseNumber, credit, day, fullCourseName, instructor, isClosed,
				maxStudent, note, room, section, student, timeRange);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CourseRow other = (CourseRow) obj;
		return Objects.equals(building, other.building) && Objects.equals(courseName, other.courseName)
				&& Objects.equals(courseNumber, other.courseNumber) && credit == other.credit && day == other.day
				&& Objects.equals(fullCourseName, other.fullCourseName) && Objects.equals(instructor, other.instructor)
				&& isClosed == other.isClosed && maxStudent == other.maxStudent && Objects.equals(note, other.note)
				&& Objects.equals(room, other.room) && section == other.section && student == other.student
				&& Objects.equals(timeRange, other.timeRange);
	}

	@Override
	public String toString() {
		return "CourseRow [courseNumber=" + courseNumber + ", courseName=" + courseName + ", fullCourseName="
				+ fullCourseName + ", credit=" + credit + ", section=" + section + ", isClosed=" + isClosed + ", day="
				+ day + ", timeRange=" + timeRange + ", building=" + building + ", room=" + room + ", instructor="
				+ instructor + ", student=" + student + ", maxStudent=" + maxStudent + ", note=" + note + "]";
	}

}
