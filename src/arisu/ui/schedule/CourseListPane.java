package arisu.ui.schedule;

import arisu.model.schedule.CourseAdapter;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.TextAlignment;

public class CourseListPane extends ListView<CourseAdapter> {
	private ClassSchedulePane schedulePane;
	private CourseAdapter selected;
	private Label empty;

	public CourseListPane(ClassSchedulePane schedulePane) {
		this.schedulePane = schedulePane;
		this.setItems(schedulePane.getTableModel().getCourses());
		this.setCellFactory(this::createListCell);
		this.getSelectionModel().selectedItemProperty().addListener(this::changed);
		empty = new Label("It's empty here!");
		empty.setStyle("-fx-font-size: 14px");
		empty.setAlignment(Pos.BASELINE_CENTER);
		empty.setTextAlignment(TextAlignment.CENTER);
		empty.setPrefWidth(200);
		this.setPlaceholder(empty);
		this.setOnKeyPressed(this::onKeyPressed);
	}

	private void onKeyPressed(KeyEvent e) {
		if (e.getCode() == KeyCode.DELETE) {
			if (this.selected != null) {
				schedulePane.getControlPane().onDelete(new ActionEvent());
			}
		} else if (e.getCode() == KeyCode.ENTER) {
			schedulePane.getControlPane().onEdit(new ActionEvent());
		}
	}

	private ListCell<CourseAdapter> createListCell(ListView<CourseAdapter> list) {
		return new CourseListCell();
	}

	private void changed(ObservableValue<? extends CourseAdapter> ov, CourseAdapter oldVal, CourseAdapter newVal) {
		this.selected = newVal;
	}

	public CourseAdapter getSelected() {
		return this.selected;
	}

	static class CourseListCell extends ListCell<CourseAdapter> {
		@Override
		public void updateItem(CourseAdapter item, boolean empty) {
			super.updateItem(item, empty);
			if (!empty) {
				setGraphic(new Label("\u2022"));
				Tooltip tooltip = new Tooltip();
				tooltip.setText(this.getItem().getCourseName());
				this.setTooltip(tooltip);
				item.updateListCell(this);
			} else {
				setGraphic(null);
				setText(null);
			}
		}
	}
}
