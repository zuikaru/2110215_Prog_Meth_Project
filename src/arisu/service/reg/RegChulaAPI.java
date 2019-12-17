package arisu.service.reg;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import arisu.exception.BadResponseException;
import arisu.exception.HttpConnectionException;
import arisu.service.http.HttpAPI;
import arisu.util.LocalTimeRange;

public class RegChulaAPI extends HttpAPI {

	static final String REG_CHULA_BASE_URL = "https://cas.reg.chula.ac.th/";
	static final String REG_CHULA_QUERY_COURSE = "/servlet/com.dtm.chula.cs.servlet.QueryCourseScheduleNew.CourseListNewServlet";
	static final String ENCODING = "x-windows-874";
	static final String REG_CHULA_QUERY_COURSE_DETAIL = "/servlet/com.dtm.chula.cs.servlet.QueryCourseScheduleNew.CourseScheduleDtlNewServlet";

	public RegChulaAPI() {
		super(REG_CHULA_BASE_URL);
		this.setEncoding(ENCODING);
	}

	@Override
	public void initSession() throws HttpConnectionException {
		HttpsURLConnection con = null;
		try {
			con = (HttpsURLConnection) this.createConnection(REG_CHULA_QUERY_COURSE);
			con.connect();
		} catch (IOException e) {
			throw new HttpConnectionException("Error: Can't connect to reg.chula service!", con, e);
		}
		this.initCookieFromConnection(con);
		con.disconnect();
	}

	public void setPeriod(StudyProgram studyProgram, Year academicYear, Semester semester)
			throws HttpConnectionException {
		this.sendGet(REG_CHULA_QUERY_COURSE, new CourseListQuery(studyProgram, semester, academicYear));
	}

	public void setPeriod(Year academicYear, Semester semester) throws HttpConnectionException {
		this.setPeriod(StudyProgram.SEMESTER, academicYear, semester);
	}

	public List<Course> getCourse(CourseListQuery query) throws HttpConnectionException, BadResponseException {
		String result = this.sendGet(REG_CHULA_QUERY_COURSE, query);
		// Parsing
		List<Course> courses = new ArrayList<>();
		Document doc = Jsoup.parse(result);
		Element table = doc.selectFirst("table#Table4 > tbody");
		if (table == null) {
			return courses;
		}
		try {
			for (Element e : table.children()) {
				Element ce = e.child(1).child(0).child(0);
				Element ne = e.child(2).child(0).child(0);
				String name = unescapeString(ne.html());
				String id = ce.attr("title");
				Course course = new Course(id, name, query.getSemester(), query.getAcademicYear(),
						query.getStudyProgram());
				courses.add(course);
			}
			return courses;
		} catch (Exception e) {
			throw new BadResponseException("Can't parse course list from reg.chula!", e);
		}
	}

	public CourseDetail getCourseDetail(CourseDetailQuery query) throws HttpConnectionException, BadResponseException {
		String result = this.sendGet(REG_CHULA_QUERY_COURSE_DETAIL, query);
		if (result == null)
			return null;
		try {
			// Parsing
			Document doc = Jsoup.parse(result);
			Element main = doc.selectFirst("form[name=courseScheduleDtlForm]");
			// Sub elements
			Element courseNameElement = main.child(1).child(0);
			Element creditAndReqElement = main.child(2).child(0);
			Element sectionsElement = main.child(4).child(0);
			// Traverse courseNameElement
			String thaiName = unescapeString(courseNameElement.child(4).selectFirst("font").html());
			String engName = unescapeString(courseNameElement.child(5).selectFirst("font").html());
			// Traverse creditAndReqElement
			String creditString = unescapeString(creditAndReqElement.child(0).child(0).selectFirst("font").html());
			int credit = 0;
			if (creditString.matches("-?\\d+(\\.\\d+)?"))
				credit = (int) Double.parseDouble(creditString);
			// Parse section details
			Map<Integer, Section> sections = parseSection(sectionsElement);
			return new CourseDetail(query.getCourseNo(), engName, thaiName, credit, sections);
		} catch (Exception e) {
			throw new BadResponseException("Can't parse course detail from reg.chula!", e);
		}
	}

	private Map<Integer, Section> parseSection(Element sectionsElement) {
		Map<Integer, Section> sections = new LinkedHashMap<>();
		int index = 0;
		int latestSection = -1;
		// each row
		for (Element row : sectionsElement.children()) {
			if (index < 2) {
				index++;
				continue;
			}
			// collect
			List<String> cols = new ArrayList<>();
			for (Element col : row.children()) {
				Element text = col.selectFirst("font");
				if (text != null) {
					String colText = unescapeString(text.text());
					cols.add(colText);
				} else {
					cols.add("");
				}
			}
			boolean closed = false;
			int section = 0;
			String teaching = "";
			String building = "";
			String room = "";
			LocalTimeRange time = null;
			Set<String> instructors = new HashSet<>();
			int student = 0;
			int maxStudent = 0;
			String note = "";
			// special case: colspan=2
			if (row.child(0).attr("colspan").equals("2")) {
				cols.add(0, "");
				section = latestSection;
			}
			// closed
			if (cols.get(0).matches("^\\(\\u0E1B\\u0E34\\u0E14\\)$")) {
				closed = true;
			}
			// section no
			if (cols.get(1).matches("^\\d+$")) {
				section = Integer.parseInt(cols.get(1));
			}
			// teaching
			teaching = cols.get(2);
			// dates
			List<DayOfWeek> days = new ArrayList<>();
			for (String day : cols.get(3).split(" ")) {
				days.add(parseRegChulaDay(day));
			}
			// times
			if (cols.get(4).matches("^\\d{1,2}\\:\\d{2}\\-\\d{1,2}\\:\\d{2}$")) {
				String[] timeString = cols.get(4).split("-");
				String excessZeroHour = "^[3-9]{1}0\\:\\d{1,2}$";
				for (int i = 0; i < 2; i++) {
					// add leading zero
					if (timeString[i].length() == 4) {
						timeString[i] = "0" + timeString[i];
					}
					// HACK: Attempt to fix some case of invalid date from reg.chula
					if (timeString[i].matches(excessZeroHour)) {
						timeString[i] = new StringBuilder(timeString[i]).deleteCharAt(1).insert(0, '0').toString();
					}
				}
				time = new LocalTimeRange(LocalTime.parse(timeString[0]), LocalTime.parse(timeString[1]));
			}
			// building
			building = cols.get(5);
			room = cols.get(6);
			// instructors
			instructors.addAll(Arrays.asList(cols.get(7).split(",")));
			// student
			// check for special case: no data about student/max student from this row
			if (cols.size() == 10) {
				String[] studentCurrentMax = cols.get(9).split("/");
				student = Integer.parseInt(studentCurrentMax[0]);
				maxStudent = Integer.parseInt(studentCurrentMax[1]);
			}
			// note
			note = cols.get(8);
			List<ClassSchedule> schedules = null;
			// if not exists
			if (!sections.containsKey(section)) {
				schedules = new ArrayList<>();
				Section sectionObject = new Section(section, closed, student, maxStudent, schedules, instructors, note);
				sections.put(section, sectionObject);
			}
			// else
			else {
				schedules = sections.get(section).getClassSchedules();
			}
			for (DayOfWeek day : days) {
				schedules.add(new ClassSchedule(day, time == null ? null : time.start(),
						time == null ? null : time.end(), building, room, new ArrayList<>(instructors)));
			}
			index++;
			latestSection = section;
		}
		return sections;
	}

	private DayOfWeek parseRegChulaDay(String day) {
		switch (day) {
		case "MO":
			return DayOfWeek.MONDAY;
		case "TU":
			return DayOfWeek.TUESDAY;
		case "WE":
			return DayOfWeek.WEDNESDAY;
		case "TH":
			return DayOfWeek.THURSDAY;
		case "FR":
			return DayOfWeek.FRIDAY;
		case "SA":
			return DayOfWeek.SATURDAY;
		case "SU":
			return DayOfWeek.SUNDAY;
		default:
			return null;
		}
	}

	private String unescapeString(String htmlEscapedString) {
		return Parser.unescapeEntities(htmlEscapedString, true).strip().replace("\u00a0", "");
	}

}
