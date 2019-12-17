package arisu.ui.browser;

import java.util.HashMap;
import java.util.Map;

import arisu.exception.InvalidInputException;
import arisu.service.reg.Semester;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;

public class SearchPane extends GridPane {
	// Acad year
	private Label yearLabel = new Label("Year");
	private TextField year = new TextField("2019");
	// Semester
	private Label semesterLabel = new Label("Semester");
	private ComboBox<Semester> semester = new ComboBox<>(FXCollections.observableArrayList(Semester.values()));
	// Course no
	private Label courseNumberLabel = new Label("Course no.");
	private TextField courseNumber = new TextField("");
	// Course name
	private Label courseNameLabel = new Label("Course name");
	private TextField courseName = new TextField("");
	// Search
	private Button search = new Button("Search");

	public SearchPane(CourseBrowserPane browserPane) {
		setHgap(8);
		setVgap(8);
		year.setPromptText("eg. 2019");
		year.setPrefWidth(100);
		semester.setValue(Semester.SECOND);
		courseNumber.setPromptText("eg. 2110");
		courseNumber.setPrefWidth(100);
		courseName.setPromptText("eg. PROG");
		courseName.setTextFormatter(new TextFormatter<>(change -> {
			change.setText(change.getText().toUpperCase());
			return change;
		}));
		search.setOnAction(browserPane::onSubmit);
		add(yearLabel, 0, 0);
		add(year, 1, 0);
		add(semesterLabel, 2, 0);
		add(semester, 3, 0);
		add(courseNumberLabel, 0, 1);
		add(courseNumber, 1, 1);
		add(courseNameLabel, 2, 1);
		add(courseName, 3, 1);
		add(search, 4, 1);

	}

	public void validateInput() throws InvalidInputException {
		Map<String, String> errors = new HashMap<>();
		if (!year.getText().matches("^\\d{4}$")) {
			errors.put(yearLabel.getText(), "Not a valid year!");
		}
		if (errors.size() > 0) {
			throw new InvalidInputException(errors);
		}
	}

	public TextField getYear() {
		return year;
	}

	public ComboBox<Semester> getSemester() {
		return semester;
	}

	public TextField getCourseNumber() {
		return courseNumber;
	}

	public TextField getCourseName() {
		return courseName;
	}
}
