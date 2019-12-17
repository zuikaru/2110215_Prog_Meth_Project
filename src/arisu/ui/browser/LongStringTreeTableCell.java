package arisu.ui.browser;

import arisu.model.browser.CourseRow;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeTableCell;

public class LongStringTreeTableCell extends TreeTableCell<CourseRow, String> {
	@Override
	public void updateItem(String item, boolean empty) {
		super.updateItem(item, empty);
		if (!empty && item != null) {
			if (!item.isBlank())
				setTooltip(new Tooltip(item));
			setText(item);
		} else {
			setText(null);
		}
	}
}
