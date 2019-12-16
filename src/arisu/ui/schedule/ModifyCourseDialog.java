package arisu.ui.schedule;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import arisu.exception.InvalidInputException;
import arisu.model.ScheduleTableModel;
import arisu.model.SingleCourseEntry;
import arisu.util.LocalTimeRange;
import arisu.util.UIUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ModifyCourseDialog extends VBox {
	// Input fields
	protected Label courseNoLabel = new Label("Course Number");
	protected TextField courseNo = new TextField("");
	protected Label courseNameLabel = new Label("Course Name");
	protected TextField courseName = new TextField("");
	protected Label sectionLabel = new Label("Section");
	protected TextField section = new TextField("1");
	protected Label dayLabel = new Label("Day");
	protected ComboBox<DayOfWeek> day = new ComboBox<>(dayOfWeekList());
	protected Label timeLabel = new Label("Time");
	protected TimePicker time = new TimePicker();
	protected Label buildingLabel = new Label("Building (Optional)");
	protected TextField building = new TextField("");
	protected Label roomLabel = new Label("Room (Optional)");
	protected TextField room = new TextField("");
	// Button
	protected Button commitChange = new Button("Add");
	protected Button cancel = new Button("Cancel");
	protected HBox buttonContainer = new HBox();
	// Other
	protected boolean editMode = false;
	private SingleCourseEntry currentCourseEntry;
	protected Stage stage;
	protected ClassSchedulePane schedulePane;
	// Formatter

	public ModifyCourseDialog(Stage stage, ClassSchedulePane schedulePane) {
		this.stage = stage;
		this.schedulePane = schedulePane;
		setSpacing(12);
		setPadding(new Insets(16));
		courseNo.setMaxWidth(250);
		courseNo.setPromptText("eg. 2110215");
		courseName.setMaxWidth(250);
		courseName.setPromptText("eg. PROG METH I");
		section.setMaxWidth(250);
		section.setText("1");
		day.setCellFactory(this::makeDayCell);
		day.setValue(DayOfWeek.MONDAY);
		day.setButtonCell(makeDayCell());
		building.setMaxWidth(250);
		building.setPromptText("eg. ENG3");
		room.setMaxWidth(250);
		room.setPromptText("eg. 315");
		courseNoLabel.setStyle("-fx-font-size: 14px");
		courseNameLabel.setStyle("-fx-font-size: 14px");
		sectionLabel.setStyle("-fx-font-size: 14px");
		dayLabel.setStyle("-fx-font-size: 14px");
		timeLabel.setStyle("-fx-font-size: 14px");
		buildingLabel.setStyle("-fx-font-size: 14px");
		roomLabel.setStyle("-fx-font-size: 14px");
		// Button
		commitChange.setOnAction(this::onChangeCommit);
		cancel.setOnAction(this::onCancel);
		buttonContainer.setSpacing(8);
		buttonContainer.getChildren().addAll(commitChange, cancel);
		getChildren().addAll(courseNoLabel, courseNo, courseNameLabel, courseName, sectionLabel, section, dayLabel, day,
				timeLabel, time, buildingLabel, building, roomLabel, room, buttonContainer);
	}

	public void setEditMode(boolean edit) {
		if (edit) {
			this.editMode = true;
			this.commitChange.setText("Save");
		} else {
			this.editMode = false;
			this.commitChange.setText("Edit");
		}
	}

	public void feedCurrentData(SingleCourseEntry currentCourseEntry) {
		this.currentCourseEntry = currentCourseEntry;
		// Load old data into each field
		copyDataFrom(currentCourseEntry);
	}

	public void copyDataFrom(SingleCourseEntry courseEntry) {
		CourseCell courseCell = courseEntry.getFirstCourseCell();
		this.courseNo.setText(courseCell.getCourseNumber());
		this.courseName.setText(courseCell.getCourseName());
		this.section.setText(String.valueOf(courseCell.getSection()));
		this.day.setValue(courseCell.getDay());
		LocalTimeRange timeRange = courseCell.getTimeRange();
		this.time.getStart().setText(timeRange.start().toString());
		this.time.getEnd().setText(timeRange.end().toString());
		this.building.setText(courseCell.getBuilding());
		this.room.setText(courseCell.getRoom());
	}

	private void validateInput() throws InvalidInputException {
		Map<String, String> messages = new HashMap<>();
		// course no
		String courseNoText = courseNo.getText().strip();
		if (!courseNoText.matches("\\d+")) {
			messages.put(courseNoLabel.getText(), "Course number must be a number!");
		} else if (courseNoText.length() != 7) {
			messages.put(courseNoLabel.getText(),
					"Invalid length of course number! (expected 7 but got " + courseNoText.length() + ")");
		}
		// section
		try {
			int sectionNo = Integer.parseInt(section.getText());
			if (sectionNo <= 0) {
				messages.put(sectionLabel.getText(), "Section must be greater than 0!");
			}
		} catch (NumberFormatException ex) {
			messages.put(sectionLabel.getText(), "Section must be a number!");
		}
		// time range
		LocalTimeRange timeRange = null;
		try {
			timeRange = time.getTimeRange();
		} catch (IllegalArgumentException ex) {
			messages.put(timeLabel.getText(), "Invalid time range: " + ex.getMessage());
		}
		if(timeRange != null) {
			if (timeRange.start().isBefore(ScheduleTableView.TIME_LOWER_LIMIT)
					|| timeRange.end().isAfter(ScheduleTableView.TIME_UPPER_LIMIT)) {
				messages.put(timeLabel.getText(),
						"Only times greater than or equal 08:00 and lesser than or equal 16:00 are supported!");
			}
			else if(timeRange.start().getMinute()%30 != 0 || timeRange.end().getMinute()%30 != 0) {
				messages.put(timeLabel.getText(),
						"Miutes must be divisible by 30!");
				
			}
		}
		if (messages.size() > 0) {
			throw new InvalidInputException(messages);
		}
	}

	public void onChangeCommit(ActionEvent event) {
		ScheduleTableModel tableModel = schedulePane.getTableModel();
		try {
			validateInput();
			SingleCourseEntry courseEntry = null;
			boolean success = false;
			// If edit then modify existing instance
			if (!this.editMode) {
				courseEntry = new SingleCourseEntry(day.getValue(), time.getTimeRange(), courseNo.getText().strip(),
						courseName.getText(), Integer.parseInt(section.getText()), building.getText(), room.getText(),
						"LECT");
			} else {
				courseEntry = currentCourseEntry;
			}
			// Check for conflict
			boolean conflict = true;
			if (this.editMode) {
				conflict = tableModel.oldItemConflict(day.getValue(), time.getTimeRange(), courseEntry);
			} else {
				conflict = tableModel.newItemConflict(courseEntry);
			}
			if (!conflict) {
				if (this.editMode) {
					tableModel.remove(courseEntry);
					courseEntry.modify(day.getValue(), time.getTimeRange(), courseNo.getText().strip(),
							courseName.getText(), Integer.parseInt(section.getText()), building.getText(),
							room.getText(), "LECT");
				}
				tableModel.add(courseEntry);
				success = true;
			} else {
				Alert alert = UIUtil.makeAlert(AlertType.ERROR, "Time conflict with existing courses in the table!");
				alert.show();
			}
			// If success -> select then close
			if (success) {
				tableModel.sort();
				schedulePane.getCourseListPane().getSelectionModel().select(courseEntry);
				stage.close();
			}
		} catch (InvalidInputException ex) {
			Alert alert = UIUtil.makeAlert(AlertType.ERROR);
			String errorMessage = "";
			for (Map.Entry<String, String> message : ex.getValidationMessages().entrySet()) {
				errorMessage += message.getKey() + ": " + message.getValue() + "\n";
			}
			Label errorLabel = new Label(errorMessage);
			errorLabel.setWrapText(true);
			alert.getDialogPane().setContent(errorLabel);
			alert.show();

		} catch (Exception ex) {
			Alert alert = UIUtil.makeAlert(AlertType.ERROR, "Some unknown error occurred. Please try again!");
			alert.show();
			ex.printStackTrace();
		}
	}

	public void onCancel(ActionEvent event) {
		stage.close();
	}

	private ObservableList<DayOfWeek> dayOfWeekList() {
		ObservableList<DayOfWeek> list = FXCollections.observableArrayList();
		for (DayOfWeek d : DayOfWeek.values()) {
			if (!d.equals(DayOfWeek.SUNDAY))
				list.add(d);
		}
		return list;
	}

	private ListCell<DayOfWeek> makeDayCell(ListView<DayOfWeek> param) {
		return makeDayCell();
	}

	private ListCell<DayOfWeek> makeDayCell() {
		return new ListCell<DayOfWeek>() {
			@Override
			public void updateItem(DayOfWeek item, boolean empty) {
				super.updateItem(item, empty);
				if (item != null) {
					setText(item.getDisplayName(TextStyle.FULL, Locale.getDefault()));
				} else {
					setText(null);
				}
			}
		};
	}
}
