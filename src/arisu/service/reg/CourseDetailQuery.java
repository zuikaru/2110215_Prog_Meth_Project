package arisu.service.reg;

import arisu.service.http.AbstractQuery;
import arisu.service.http.QueryParameter;

public class CourseDetailQuery extends AbstractQuery {
	protected StudyProgram studyProgram;
	protected QueryParameter<String> courseNo;

	public CourseDetailQuery(StudyProgram studyProgram, String courseNo) {
		this.studyProgram = studyProgram;
		this.courseNo = new QueryParameter<>("courseNo", courseNo);
	}

	public CourseDetailQuery(CourseDetailQuery q) {
		this(q.getStudyProgram(), q.getCourseNo());
	}

	public CourseDetailQuery(Course course) {
		this(course.getStudyProgram(), course.getNumber());
	}

	public StudyProgram getStudyProgram() {
		return studyProgram;
	}

	public void setStudyProgram(StudyProgram studyProgram) {
		this.studyProgram = studyProgram;
	}

	public String getCourseNo() {
		return courseNo.getValue();
	}

	public void setCourseNo(String courseNo) {
		this.courseNo.setValue(courseNo);
	}

}
