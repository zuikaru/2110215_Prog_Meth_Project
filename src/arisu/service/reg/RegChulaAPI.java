package arisu.service.reg;

import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import arisu.service.http.HttpAPI;

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
	public void initSession() {
		HttpsURLConnection con;
		try {
			con = (HttpsURLConnection) this.createConnection(REG_CHULA_QUERY_COURSE);
			con.connect();
		} catch (IOException e) {
			System.out.println("Error: Can't connect to reg.chula service!");
			e.printStackTrace();
			return;
		}
		this.initCookieFromConnection(con);
		con.disconnect();
	}

	public void setPeriod(StudyProgram studyProgram, Year academicYear, Semester semester) {
		this.sendGet(REG_CHULA_QUERY_COURSE, new CourseListQuery(studyProgram, semester, academicYear));
	}

	public void setPeriod(Year academicYear, Semester semester) {
		this.setPeriod(StudyProgram.SEMESTER, academicYear, semester);
	}

	public List<Course> getCourse(CourseListQuery query) {
		String result = this.sendGet(REG_CHULA_QUERY_COURSE, query);
		// Parsing
		List<Course> courses = new ArrayList<>();
		Document doc = Jsoup.parse(result);
		Element table = doc.selectFirst("table#Table4 > tbody");
		for (Element e : table.children()) {
			Element ce = e.child(1).child(0).child(0);
			Element ne = e.child(2).child(0).child(0);
			String name = unescapeString(ne.html());
			String id = ce.attr("title");
			Course course = new Course(id, name, query.getSemester(), query.getAcademicYear(), query.getStudyProgram());
			courses.add(course);
		}
		return courses;
	}

	public CourseDetail getCourseDetail(CourseDetailQuery query) {
		String result = this.sendGet(REG_CHULA_QUERY_COURSE_DETAIL, query);
		if (result == null)
			return null;
		// Parsing
		Document doc = Jsoup.parse(result);
		Element main = doc.selectFirst("form[name=courseScheduleDtlForm]");
		// Sub elements
		Element courseNameElement = main.child(1).child(0);
		Element creditAndReqElement = main.child(2).child(0);
		;
		Element examDateElement = main.child(3).child(0);
		;
		Element sectionsElement = main.child(4).child(0);
		;
		// Traverse courseNameElement
		String thaiName = unescapeString(courseNameElement.child(4).selectFirst("font").html());
		String engName = unescapeString(courseNameElement.child(5).selectFirst("font").html());
		// Traverse creditAndReqElement
		String creditString = unescapeString(creditAndReqElement.child(0).child(0).selectFirst("font").html());
		int credit = 0;
		if(creditString.matches("-?\\d+(\\.\\d+)?"))
			credit = (int) Double.parseDouble(creditString);
		// Parse section details
		System.out.println(query.getCourseNo() + " - " + engName);
		Map<Integer,Section> sections = parseSection(sectionsElement);
		return new CourseDetail(query.getCourseNo(), engName, thaiName, credit, sections);
	}
	
	private Map<Integer,Section> parseSection(Element sectionsElement){
		Map<Integer,Section> sections = new HashMap<>();
		int index = 0;
		// each row
		for(Element row : sectionsElement.children()) {
			if(index < 2) {
				index++; continue;
			}
			int colIndex = 0;
			boolean first = true;
			// collect
			List<String> cols = new ArrayList<>();
			for(Element col : row.children()) {
				Element text = col.selectFirst("font");
				if(text != null) {
					String colText = unescapeString(text.text());
					cols.add(colText);
				}
				else {
					cols.add("");
				}
				colIndex++;
			}
			boolean closed = false;
			int section = 0;
			String teaching = "";
			// closed
			if(cols.get(0).matches("^\\(\\u0E1B\\u0E34\\u0E14\\)$")){
				closed = true;
			}
			// section no
			if(cols.get(1).matches("^\\d+$")) {
				section = Integer.parseInt(cols.get(1));
			}
			// teaching
			teaching = cols.get(2);
			// dates
			List<String> dates = new ArrayList<>();
			for(String date : cols.get(3).split(" ")) {
				dates.add(date);
			}
			// times
			
			System.out.println(cols);
			index++;
		}
		return sections;
	}

	private String unescapeString(String htmlEscapedString) {
		return Parser.unescapeEntities(htmlEscapedString, true).strip().replace("\u00a0", "");
	}

}
