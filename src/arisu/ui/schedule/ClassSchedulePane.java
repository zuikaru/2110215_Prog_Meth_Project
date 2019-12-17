package arisu.ui.schedule;

import arisu.Arisu;
import arisu.model.schedule.ScheduleTableModel;
import arisu.util.SimpleBorder;
import arisu.util.UIUtil;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ClassSchedulePane extends BorderPane {

	private ScheduleTableView tableView;
	private ScheduleTableModel tableModel;
	private CourseListPane courseListPane;
	private ClassScheduleControlPane controlPane;
	private ScrollPane tableScroll;
	private Label title;

	public ClassSchedulePane(Arisu app) {
		setPadding(new Insets(16));
		// components
		title = createTitle("Schedule Planner");
		tableView = new ScheduleTableView(this);
		tableModel = new ScheduleTableModel(this);
		tableScroll = new ScrollPane(tableView);
		tableScroll.setHbarPolicy(ScrollBarPolicy.ALWAYS);
		tableScroll.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		tableScroll.setPannable(true);
		tableScroll.setBorder(SimpleBorder.solid(Color.WHITESMOKE, 0.25));
		courseListPane = new CourseListPane(this);
		courseListPane.setBorder(SimpleBorder.solid(Color.WHITESMOKE, 0.25));
		controlPane = new ClassScheduleControlPane(this);
		// Alignment
		setAlignment(controlPane, Pos.TOP_RIGHT);
		setAlignment(tableScroll, Pos.TOP_LEFT);
		// margin
		setMargin(title, new Insets(0, 0, 0, 32));
		setMargin(tableScroll, new Insets(8, 8, 8, 8));
		setMargin(courseListPane, new Insets(8, 8, 8, 8));
		setTop(title);
		setCenter(tableScroll);
		setBottom(controlPane);
		setRight(courseListPane);
		// Fix blurry ScrollPane
		UIUtil.fixBlurryScrollPane(tableScroll);
	}

	private Label createTitle(String name) {
		Label label = new Label(name);
		label.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 32));
		return label;
	}

	public ScheduleTableView getTableView() {
		return tableView;
	}

	public ScheduleTableModel getTableModel() {
		return tableModel;
	}

	public CourseListPane getCourseListPane() {
		return this.courseListPane;
	}

	public ClassScheduleControlPane getControlPane() {
		return this.controlPane;
	}
}