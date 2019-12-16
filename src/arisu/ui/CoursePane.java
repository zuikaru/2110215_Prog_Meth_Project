package arisu.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import arisu.Arisu;
import arisu.service.reg.Course;
import arisu.service.reg.CourseDetail;
import arisu.service.reg.CourseDetailQuery;
import arisu.service.reg.CourseListQuery;
import arisu.service.reg.RegChulaAPI;
import arisu.service.reg.Semester;
import arisu.util.SizeAdapter;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class CoursePane extends VBox{
	private TextArea text = new TextArea();
	private ProgressBar p;
	private Button refresh;
	private boolean working = false;
	public CoursePane(Arisu app) {
		setPadding(new Insets(16));
		SizeAdapter.use(app.getScene(), this);
		Label title = new Label("Browse Course List");
		title.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 32));
		getChildren().add(title);
		setMargin(title, new Insets(0, 0, 0, 32));
		initTable();
		refresh = new Button("Refresh");
		refresh.setOnAction((e) -> {
			if(working) return;
			working = true;
			p.setVisible(true);
			refreshData();
		});
		getChildren().add(refresh);
		text.setPromptText("Press refresh to get new data from reg!");
		getChildren().add(text);
		p = new ProgressBar();
		p.setVisible(false);
		getChildren().add(p);
	}
	
	private void initTable() {
		
	}
	
	private void refreshData() {
		new Thread(() -> {
			String result = null;
			boolean success = false;
			try {
				System.out.println("[RegChulaAPI] Querying data...");
				RegChulaAPI api = new RegChulaAPI();
				api.init();
				CourseListQuery q = new CourseListQuery();
				q.setCourseNo("21102");
				q.setSemester(Semester.SECOND);
				final Map<Course, CourseDetail> courses = new HashMap<>();
				List<Course> rawList = api.getCourse(q);
				for(Course c : rawList) {
					courses.put(c, api.getCourseDetail(new CourseDetailQuery(c)));
					p.setProgress((double)courses.size()/rawList.size());
				}
				StringBuilder sb = new StringBuilder();
				for(Map.Entry<Course, CourseDetail> e : courses.entrySet()) {
					sb.append(e.getKey() + ": " + e.getValue() + "\n");
				}
				result = sb.toString();
				success = true;
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			if(success) {
				System.out.println("[RegChulaAPI] Query completed!");
			}
			else {
				System.out.println("[RegChulaAPI] Query failed!");
			}
			String copyResult = result == null ? "" : result;
			Platform.runLater(() -> {
				working = false;
				p.setVisible(false);
				p.setProgress(0);
				text.setText(copyResult);
			});
		}).start();
	}
}
