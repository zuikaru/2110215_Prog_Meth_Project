package arisu.ui.schedule;

import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

public class CourseListPane extends VBox{
	private ClassSchedulePane schedulePane;
	public CourseListPane(ClassSchedulePane schedulePane) {
		this.schedulePane = schedulePane;
		setSpacing(8);
	}
}
