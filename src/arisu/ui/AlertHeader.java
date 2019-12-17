package arisu.ui;

import arisu.util.SimpleBackground;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class AlertHeader extends GridPane {
	private Label header;
	private ImageView image;

	public AlertHeader(Alert alert) {
		setPadding(new Insets(16));
		setMaxWidth(Double.MAX_VALUE);
		setBackground(SimpleBackground.web("#333"));
		header = new Label(alert.getHeaderText());
		header.setWrapText(true);
		header.setAlignment(Pos.CENTER_LEFT);
		header.setMaxWidth(Double.MAX_VALUE);
		header.setMaxHeight(Double.MAX_VALUE);
		header.setStyle("-fx-font-size: 18px");
		header.textProperty().bind(alert.headerTextProperty());
		image = new ImageView(loadImage(alert.getAlertType()));
		image.setPreserveRatio(true);
		image.setFitWidth(64);
		alert.alertTypeProperty().addListener((obs) -> {
			image.setImage(loadImage(alert.getAlertType()));
		});
		// Add to grid
		add(header, 0, 0);
		add(image, 1, 0);
		// column constraints
		ColumnConstraints textColumn = new ColumnConstraints();
		textColumn.setFillWidth(true);
		textColumn.setHgrow(Priority.ALWAYS);
		ColumnConstraints imageColumn = new ColumnConstraints();
		imageColumn.setFillWidth(false);
		imageColumn.setHgrow(Priority.NEVER);
		getColumnConstraints().setAll(textColumn, imageColumn);
	}

	private Image loadImage(AlertType alertType) {
		if (alertType == null || alertType == AlertType.NONE)
			return null;
		else {
			return new Image(getRes("assets/dialog/" + alertType.toString().toLowerCase() + ".png"));
		}
	}

	private String getRes(String url) {
		return ClassLoader.getSystemResource(url).toString();
	}
}
