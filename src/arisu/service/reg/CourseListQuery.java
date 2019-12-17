package arisu.service.reg;

import java.time.Year;
import java.util.function.Function;

import arisu.service.http.AbstractQuery;
import arisu.service.http.QueryParameter;

public class CourseListQuery extends AbstractQuery {
	// Normal parameters
	protected StudyProgram studyProgram;
	protected Semester semester;
	protected QueryParameter<Year> academicYear;
	protected QueryParameter<String> courseNo;
	protected QueryParameter<String> courseName;
	protected QueryParameter<String> faculty;
	protected QueryParameter<String> examDate;
	protected QueryParameter<String> examStart;
	protected QueryParameter<String> examEnd;
	protected QueryParameter<String> courseType;
	// Magic/Unused parameters
	protected QueryParameter<String> submitX;
	protected QueryParameter<String> submitY;
	protected QueryParameter<String> magicExamDate;
	protected QueryParameter<String> genedCode;
	protected QueryParameter<Semester> currentSemester; // HACK
	protected QueryParameter<Year> currentAcademicYear;
	protected QueryParameter<String> magicExamStart;
	protected QueryParameter<String> magicExamEnd;
	protected QueryParameter<String> activeStatus;
	protected QueryParameter<Year> magicCurrentAcademicYear;
	protected QueryParameter<String> download;
	protected QueryParameter<String> lang;

	private static final Function<Year, String> TO_BUDDHIST_YEAR = year -> Integer.toString(year.getValue() + 543);

	protected void initMagicFields() {
		magicExamDate = new QueryParameter<>("examdateCombo", "I2018207/05/1476");
		submitX = new QueryParameter<String>("submit.x", "34");
		submitY = new QueryParameter<String>("submit.y", "8");
		lang = new QueryParameter<>("lang", "T");
		genedCode = new QueryParameter<>("genedcode");
		currentSemester = new QueryParameter<>("cursemester", Semester.FIRST);
		currentAcademicYear = new QueryParameter<>("curacadyear", Year.now(), TO_BUDDHIST_YEAR);
		magicExamStart = new QueryParameter<>("examstart");
		magicExamEnd = new QueryParameter<>("examend");
		activeStatus = new QueryParameter<>("activestatus", "ON");
		magicCurrentAcademicYear = new QueryParameter<>("acadyearEfd", Year.now(), TO_BUDDHIST_YEAR);
		download = new QueryParameter<>("download");
	}

	public CourseListQuery() {
		// Internal/magic field
		initMagicFields();
		// Normal
		semester = Semester.FIRST;
		studyProgram = StudyProgram.SEMESTER;
		academicYear = new QueryParameter<Year>("acadyear", Year.now(), TO_BUDDHIST_YEAR);
		courseNo = new QueryParameter<>("courseno");
		courseName = new QueryParameter<>("coursename");
		faculty = new QueryParameter<>("faculty");
		examDate = new QueryParameter<>("examdate");
		examStart = new QueryParameter<>("examstartshow");
		examEnd = new QueryParameter<>("examstartshow");
		courseType = new QueryParameter<>("coursetype");
	}

	/**
	 * Copy constructor Only copy field that is visible to public (via
	 * getter/setter)
	 * 
	 * @param q
	 */
	public CourseListQuery(CourseListQuery q) {
		// Internal/magic field
		initMagicFields();
		// Copy edible field
		semester = q.semester;
		studyProgram = q.studyProgram;
		academicYear = new QueryParameter<>(q.academicYear);
		courseNo = new QueryParameter<>(q.courseNo);
		courseName = new QueryParameter<>(q.courseName);
		faculty = new QueryParameter<>(q.faculty);
		examStart = new QueryParameter<>(q.examStart);
		examEnd = new QueryParameter<>(q.examEnd);
		courseType = new QueryParameter<>(q.courseType);
	}

	public CourseListQuery(StudyProgram studyProgram, Semester semester, Year academicYear) {
		this();
		this.studyProgram = studyProgram;
		this.semester = semester;
		this.academicYear.setValue(academicYear);
	}

	public void setCourseNo(String courseNo) {
		this.courseNo.setValue(courseNo);
		// Update faculty from course no.
		if (courseNo != null && courseNo.matches("\\d{2,}")) {
			this.faculty.setValue(courseNo.trim().substring(0, 2));
		} else {
			this.faculty.setValue(null);
		}
	}

	public String getCourseNo() {
		return this.courseNo.getValue();
	}

	public void setCourseName(String courseNo) {
		this.courseName.setValue(courseNo);
	}

	public String getCourseName() {
		return this.courseName.getValue();
	}

	public void setSemester(Semester semester) {
		this.semester = semester;
	}

	public Semester getSemester() {
		return this.semester;
	}

	public void setStudyProgram(StudyProgram studyProgram) {
		this.studyProgram = studyProgram;
	}

	public StudyProgram getStudyProgram() {
		return this.studyProgram;
	}

	public void setAcademicYear(Year year) {
		this.academicYear.setValue(year);
	}

	public void setAcademicYear(int year) {
		this.academicYear.setValue(Year.of(year));
	}

	public Year getAcademicYear() {
		return this.academicYear.getValue();
	}

	public String getFaculty() {
		return this.faculty.getValue();
	}
}
