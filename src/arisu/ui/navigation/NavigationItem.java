package arisu.ui.navigation;

import arisu.Arisu;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public class NavigationItem extends HBox {
	private static final Effect RESET = new ColorAdjust();
	private static final Effect HOVER = new ColorAdjust(0, 0, -0.25, 0);
	private ImageView buttonIcon;
	private Label label;
	private String link;

	public NavigationItem(String name, Image image, String link) {
		setSpacing(16);
		setPadding(new Insets(16, 0, 16, 16));
		this.link = link;
		this.label = new Label(name);
		this.label.setFont(Font.font(18));
		this.buttonIcon = new ImageView(image);
		this.setAlignment(Pos.CENTER_LEFT);
		this.buttonIcon.setPreserveRatio(true);
		this.buttonIcon.setFitHeight(32);
		getChildren().addAll(buttonIcon, label);
		this.setCursor(Cursor.HAND);
		this.setOnMouseEntered((e) -> {
			this.setEffect(HOVER);
			this.label.setEffect(HOVER);
		});
		this.setOnMouseExited((e) -> {
			this.setEffect(RESET);
			this.label.setEffect(RESET);
		});
		this.setOnMouseClicked((e) -> {
			if (e.getButton() == MouseButton.PRIMARY) {
				Arisu app = Arisu.app();
				app.getContentPane().setCurrent(this.link);
			}
		});
	}

	public NavigationItem(String name, Image image) {
		this(name, image, name.toLowerCase());
	}

	public NavigationItem(String name) {
		this(name, new Image("assets/menu/chevron-right-white-256.png"), name.toLowerCase());
	}

}
