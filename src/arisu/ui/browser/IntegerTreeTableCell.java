package arisu.ui.browser;

import arisu.model.browser.CourseRow;
import javafx.scene.control.TreeTableCell;

public class IntegerTreeTableCell extends TreeTableCell<CourseRow, Integer> {
	@Override
	public void updateItem(Integer item, boolean empty) {
		super.updateItem(item, empty);
		if (!empty && item != null) {
			if (item.intValue() != -1) {
				setText(String.valueOf(item));
			} else {
				setText(null);
			}
		} else {
			setText(null);
		}
	}
}
