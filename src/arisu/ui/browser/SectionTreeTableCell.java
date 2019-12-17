package arisu.ui.browser;

import arisu.model.browser.CourseRow;
import javafx.scene.control.TreeTableCell;

public class SectionTreeTableCell extends TreeTableCell<CourseRow, Integer> {
	@Override
	public void updateItem(Integer item, boolean empty) {
		super.updateItem(item, empty);
		if (!empty && item != null) {
			if (item.intValue() != -1) {
				CourseRow courseRow = this.getTreeTableRow().getTreeItem().getValue();
				String additional = "";
				if (courseRow.isClosed()) {
					additional = " (Closed)";
				}
				setText(String.valueOf(item) + additional);
			} else {
				setText(null);
			}
		} else {
			setText(null);
		}
	}
}
