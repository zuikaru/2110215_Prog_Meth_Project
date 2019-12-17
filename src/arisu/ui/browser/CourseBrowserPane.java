package arisu.ui.browser;

import java.time.DayOfWeek;
import java.time.Year;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import arisu.Arisu;
import arisu.exception.BadResponseException;
import arisu.exception.HttpConnectionException;
import arisu.exception.InvalidInputException;
import arisu.model.browser.CourseRow;
import arisu.model.schedule.SingleCourseEntry;
import arisu.service.reg.ClassSchedule;
import arisu.service.reg.Course;
import arisu.service.reg.CourseDetail;
import arisu.service.reg.CourseDetailQuery;
import arisu.service.reg.CourseListQuery;
import arisu.service.reg.RegChulaAPI;
import arisu.service.reg.Section;
import arisu.ui.schedule.ClassSchedulePane;
import arisu.util.LocalTimeRange;
import arisu.util.UIUtil;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class CourseBrowserPane extends VBox {
	private Arisu app;
	private RegChulaAPI api = new RegChulaAPI();
	private ProgressBar p;
	private Button addToTable;
	private SearchPane searchPane;
	private TreeTableView<CourseRow> treeTableView;
	private TreeItem<CourseRow> treeTableRoot;
	// Column for table
	private TreeTableColumn<CourseRow, String> courseNumberColumn;
	private TreeTableColumn<CourseRow, String> courseNameColumn;
	private TreeTableColumn<CourseRow, Integer> creditColumn;
	private TreeTableColumn<CourseRow, Integer> sectionColumn;
	private TreeTableColumn<CourseRow, Integer> studentColumn;
	private TreeTableColumn<CourseRow, Integer> maxStudentColumn;
	private TreeTableColumn<CourseRow, DayOfWeek> dayColumn;
	private TreeTableColumn<CourseRow, LocalTimeRange> timeColumn;
	private TreeTableColumn<CourseRow, String> buildingColumn;
	private TreeTableColumn<CourseRow, String> roomColumn;
	private TreeTableColumn<CourseRow, String> instructorColumn;
	private TreeTableColumn<CourseRow, String> noteColumn;
	private boolean working = false;

	public CourseBrowserPane(Arisu app) {
		this.app = app;
		setPadding(new Insets(16));
		setSpacing(8);
		UIUtil.useSizeAdapter(app.getScene(), this);
		Label title = new Label("Browse Course List");
		title.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 32));
		setMargin(title, new Insets(0, 0, 0, 32));
		addToTable = new Button("Add Selected To Table");
		addToTable.setOnAction(this::onAdd);
		p = new ProgressBar();
		p.setVisible(false);
		createTableView();
		searchPane = new SearchPane(this);
		getChildren().addAll(title, searchPane, p, treeTableView, addToTable);
	}

	@SuppressWarnings("unchecked")
	private void createTableView() {
		// Creating the root element
		treeTableRoot = new TreeItem<>();
		treeTableRoot.setExpanded(true);
		// Creating a column
		courseNumberColumn = new TreeTableColumn<>("Course");
		courseNameColumn = new TreeTableColumn<>("Name");
		creditColumn = new TreeTableColumn<>("Credits");
		sectionColumn = new TreeTableColumn<>("Section");
		studentColumn = new TreeTableColumn<>("Regis");
		maxStudentColumn = new TreeTableColumn<>("Max");
		noteColumn = new TreeTableColumn<>("Note");
		dayColumn = new TreeTableColumn<>("Day");
		timeColumn = new TreeTableColumn<>("Time");
		buildingColumn = new TreeTableColumn<>("Building");
		roomColumn = new TreeTableColumn<>("Room");
		instructorColumn = new TreeTableColumn<>("Instructors");
		courseNumberColumn.setPrefWidth(75);
		courseNumberColumn.setStyle("-fx-alignment: TOP-CENTER");
		courseNameColumn.setPrefWidth(150);
		// Defining cell content
		courseNumberColumn.setCellValueFactory((CellDataFeatures<CourseRow, String> p) -> new ReadOnlyStringWrapper(
				p.getValue().getValue().getCourseNumber()));
		courseNameColumn.setCellValueFactory((CellDataFeatures<CourseRow, String> p) -> new ReadOnlyStringWrapper(
				p.getValue().getValue().getCourseName()));
		creditColumn.setCellValueFactory((CellDataFeatures<CourseRow, Integer> p) -> new ReadOnlyObjectWrapper<>(
				p.getValue().getValue().getCredit()));
		creditColumn.setCellFactory((TreeTableColumn<CourseRow, Integer> col) -> new IntegerTreeTableCell());
		sectionColumn.setCellValueFactory((CellDataFeatures<CourseRow, Integer> p) -> new ReadOnlyObjectWrapper<>(
				p.getValue().getValue().getSection()));
		sectionColumn.setCellFactory((TreeTableColumn<CourseRow, Integer> col) -> new SectionTreeTableCell());
		studentColumn.setCellValueFactory((CellDataFeatures<CourseRow, Integer> p) -> new ReadOnlyObjectWrapper<>(
				p.getValue().getValue().getStudent()));
		studentColumn.setCellFactory((TreeTableColumn<CourseRow, Integer> col) -> new IntegerTreeTableCell());
		maxStudentColumn.setCellValueFactory((CellDataFeatures<CourseRow, Integer> p) -> new ReadOnlyObjectWrapper<>(
				p.getValue().getValue().getMaxStudent()));
		maxStudentColumn.setCellFactory((TreeTableColumn<CourseRow, Integer> col) -> new IntegerTreeTableCell());
		noteColumn.setCellValueFactory((CellDataFeatures<CourseRow, String> p) -> new ReadOnlyStringWrapper(
				p.getValue().getValue().getNote()));
		noteColumn.setCellFactory((TreeTableColumn<CourseRow, String> col) -> new LongStringTreeTableCell());
		dayColumn
				.setCellValueFactory((CellDataFeatures<CourseRow, DayOfWeek> p) -> new ReadOnlyObjectWrapper<DayOfWeek>(
						p.getValue().getValue().getDay()));
		timeColumn.setCellValueFactory(
				(CellDataFeatures<CourseRow, LocalTimeRange> p) -> new ReadOnlyObjectWrapper<LocalTimeRange>(
						p.getValue().getValue().getTimeRange()));
		instructorColumn.setCellValueFactory((CellDataFeatures<CourseRow, String> p) -> new ReadOnlyStringWrapper(
				p.getValue().getValue().getInstructor()));
		instructorColumn.setCellFactory((TreeTableColumn<CourseRow, String> col) -> new LongStringTreeTableCell());
		buildingColumn.setCellValueFactory((CellDataFeatures<CourseRow, String> p) -> new ReadOnlyStringWrapper(
				p.getValue().getValue().getBuilding()));
		roomColumn.setCellValueFactory((CellDataFeatures<CourseRow, String> p) -> new ReadOnlyStringWrapper(
				p.getValue().getValue().getRoom()));
		// Creating a tree table view
		treeTableView = new TreeTableView<>(treeTableRoot);
		treeTableView.setShowRoot(false);
		treeTableView.getColumns().addAll(courseNumberColumn, courseNameColumn, creditColumn, sectionColumn, dayColumn,
				timeColumn, buildingColumn, roomColumn, instructorColumn, studentColumn, maxStudentColumn, noteColumn);
	}

	public void onAdd(ActionEvent event) {
		if (working) {
			UIUtil.makeAlert(AlertType.INFORMATION, "Search in progress, please wait!").show();
			return;
		}
		TreeItem<CourseRow> selected = treeTableView.getSelectionModel().getSelectedItem();
		if (selected != null) {
			CourseRow courseRow = selected.getValue();
			if (containsEnoughData(courseRow)) {
				// closed section
				if (courseRow.isClosed()) {
					Alert closedAlert = UIUtil.makeAlert(AlertType.WARNING, "Closed section, continue?", ButtonType.YES,
							ButtonType.NO, ButtonType.CANCEL);
					SimpleBooleanProperty continueValue = new SimpleBooleanProperty(false);
					closedAlert.showAndWait().ifPresent(type -> {
						if (type == ButtonType.YES) {
							continueValue.set(true);
						}
					});
					if (!continueValue.get()) {
						return;
					}
				}
				// get table model
				ClassSchedulePane pane = app.getContentPane().getNodeAs("edit", ClassSchedulePane.class);
				SingleCourseEntry entry = courseRow.toSingleCourseEntry();
				if (!pane.getTableModel().newItemConflict(entry)) {
					pane.getTableModel().add(entry);
					Alert alert = UIUtil.makeAlert(AlertType.INFORMATION);
					alert.setContentText("Course added to the table!");
					alert.show();
				} else {
					Alert alert = UIUtil.makeAlert(AlertType.ERROR);
					alert.setContentText("Selected course day and time conflict with existing courses!");
					alert.show();
				}
			} else {
				Alert alert = UIUtil.makeAlert(AlertType.ERROR);
				alert.setContentText("Selected row doesn't contain enough data!");
				alert.show();
			}
		} else {
			Alert alert = UIUtil.makeAlert(AlertType.ERROR);
			alert.setContentText("Please make a selection first!");
			alert.show();
		}
	}

	private boolean containsEnoughData(CourseRow courseRow) {
		if (courseRow == null) {
			return false;
		} else {
			return ! (courseRow.getDay() == null || courseRow.getTimeRange() == null || courseRow.getCourseNumber() == null);
		}
	}

	public void onSubmit(ActionEvent event) {
		try {
			searchPane.validateInput();
		} catch (InvalidInputException ex) {
			String message = "";
			for (String key : ex.getValidationMessages().keySet()) {
				message += key + ":" + ex.getValidationMessages().get(key);
			}
			Alert alert = UIUtil.makeAlert(AlertType.ERROR, message);
			alert.show();
			return;
		}
		if (working) {
			UIUtil.makeAlert(AlertType.INFORMATION, "Search in progress, please wait!").show();
			return;
		}
		working = true;
		p.setVisible(true);
		getData();
	}

	private void getData() {
		new Thread(() -> {
			SimpleBooleanProperty success = new SimpleBooleanProperty(false);
			List<Course> courseList = null;
			final Map<Course, CourseDetail> courses = new LinkedHashMap<>();
			String errorMessage = null;
			try {
				api.refreshSession();
				CourseListQuery q = new CourseListQuery();
				q.setAcademicYear(Year.parse(searchPane.getYear().getText()));
				q.setCourseNo(searchPane.getCourseNumber().getText());
				q.setCourseName(searchPane.getCourseName().getText());
				q.setSemester(searchPane.getSemester().getValue());
				courseList = api.getCourse(q);
				for (Course c : courseList) {
					courses.put(c, api.getCourseDetail(new CourseDetailQuery(c)));
					p.setProgress((double) courses.size() / courseList.size());
				}
				success.set(true);
			} catch (BadResponseException | HttpConnectionException e) {
				errorMessage = e.getMessage();
			} catch (Exception e) {
				errorMessage = "Unknown error occured: " + e.getMessage();
			}
			String copyOfErrorMessage = errorMessage;
			Platform.runLater(() -> {
				if (success.get()) {
					treeTableRoot.getChildren().clear();
					if (courses.isEmpty()) {
						UIUtil.makeAlert(AlertType.INFORMATION, "Result is empty!").show();
					} else {
						// For each course
						for (Map.Entry<Course, CourseDetail> courseEntry : courses.entrySet()) {
							Course course = courseEntry.getKey();
							CourseDetail courseDetail = courseEntry.getValue();
							TreeItem<CourseRow> parent = new TreeItem<CourseRow>(
									new CourseRow(course.getNumber(), course.getName(), courseDetail.getCredit()));
							// For each section
							for (Map.Entry<Integer, Section> section : courseDetail.getSections().entrySet()) {
								TreeItem<CourseRow> child = new TreeItem<CourseRow>(
										new CourseRow(course.getNumber(), course.getName(), courseDetail.getCredit()));
								child.getValue().setSection(section.getKey());
								child.getValue().setClosed(section.getValue().isClosed());
								child.getValue().setStudent(section.getValue().getStudent());
								child.getValue().setMaxStudent(section.getValue().getMaxStudent());
								child.getValue().setNote(section.getValue().getNote());
								List<ClassSchedule> schedules = section.getValue().getClassSchedules();
								if (schedules != null) {
									if (schedules.size() == 1) {
										ClassSchedule schedule = schedules.get(0);
										child.getValue().setDay(schedule.getDay());
										if (schedule.getStart() != null && schedule.getEnd() != null) {
											child.getValue().setTimeRange(
													new LocalTimeRange(schedule.getStart(), schedule.getEnd()));
										}
										child.getValue().setInstructor(String.join(",", schedule.getInstructors()));
										child.getValue().setBuilding(schedule.getBuilding());
										child.getValue().setRoom(schedule.getRoom());
									} else {
										for (ClassSchedule schedule : schedules) {
											TreeItem<CourseRow> grandChild = new TreeItem<CourseRow>(new CourseRow(
													course.getNumber(), course.getName(), courseDetail.getCredit()));
											grandChild.getValue().setSection(section.getKey());
											grandChild.getValue().setClosed(section.getValue().isClosed());
											grandChild.getValue().setDay(schedule.getDay());
											if (schedule.getStart() != null && schedule.getEnd() != null) {
												grandChild.getValue().setTimeRange(
														new LocalTimeRange(schedule.getStart(), schedule.getEnd()));
											}
											grandChild.getValue()
													.setInstructor(String.join(",", schedule.getInstructors()));
											grandChild.getValue().setBuilding(schedule.getBuilding());
											grandChild.getValue().setRoom(schedule.getRoom());
											child.getChildren().add(grandChild);
										}
									}
								}
								parent.getChildren().add(child);
							}
							treeTableRoot.getChildren().add(parent);
						}
					}
				} else {
					UIUtil.makeAlert(AlertType.ERROR, copyOfErrorMessage).show();
				}
				working = false;
				p.setVisible(false);
				p.setProgress(0);
			});
		}).start();
	}
}
