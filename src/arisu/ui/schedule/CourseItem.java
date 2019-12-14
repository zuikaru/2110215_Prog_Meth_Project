package arisu.ui.schedule;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class CourseItem extends HBox{
	private CheckBox checkBox;
	private Label course;
	private Button editButton;
	private Button deleteButton;
	
	public CourseItem(String courseDescription) {
		setSpacing(8);
		setAlignment(Pos.BASELINE_LEFT);
		this.course = new Label(courseDescription);
		this.course.setMaxWidth(150);
		this.checkBox = new CheckBox();
		this.editButton = new Button("Edit");
		this.deleteButton = new Button("Delete");
		getChildren().addAll(checkBox, course, editButton, deleteButton);
		this.course.setOnMouseEntered(this::onHover);
		this.course.setOnMouseExited(this::onUnHover);
		this.deleteButton.setOnAction(this::onDelete);
	}
	
	public void onHover(MouseEvent event) {
		System.out.println(course.getText() + " Enter");
	}
	
	public void onUnHover(MouseEvent event) {
		System.out.println(course.getText() + " Exit");
	}
	
	public void onDelete(ActionEvent event) {
		if(getParent() instanceof CourseListPane) {
			CourseListPane pane = (CourseListPane) getParent();
			pane.getChildren().remove(this);
		}
	}

}
