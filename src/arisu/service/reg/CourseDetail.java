package arisu.service.reg;

import java.util.Map;
import java.util.Objects;

public class CourseDetail {
	private String courseNo;
	private String fullName;
	private String fullNameThai;
	private int credit;
	private Map<Integer, Section> sections;

	public CourseDetail(String courseNo, String fullName, String fullNameThai, int credit,
			Map<Integer, Section> sections) {
		this.courseNo = courseNo;
		this.fullName = fullName;
		this.fullNameThai = fullNameThai;
		this.credit = credit;
		this.sections = sections;
	}

	public String getCourseNumber() {
		return courseNo;
	}

	public String getFullName() {
		return fullName;
	}

	public String getFullNameThai() {
		return fullNameThai;
	}

	public int getCredit() {
		return credit;
	}

	public Map<Integer, Section> getSections() {
		return sections;
	}

	@Override
	public int hashCode() {
		return Objects.hash(courseNo, credit, fullName, fullNameThai, sections);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CourseDetail other = (CourseDetail) obj;
		return Objects.equals(courseNo, other.courseNo) && credit == other.credit
				&& Objects.equals(fullName, other.fullName) && Objects.equals(fullNameThai, other.fullNameThai)
				&& Objects.equals(sections, other.sections);
	}

	@Override
	public String toString() {
		return "CourseDetail [courseNo=" + courseNo + ", fullName=" + fullName + ", fullNameThai=" + fullNameThai
				+ ", credit=" + credit + ", sections=" + sections + "]";
	}

}
