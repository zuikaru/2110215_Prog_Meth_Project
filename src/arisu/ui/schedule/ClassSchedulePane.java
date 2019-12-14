package arisu.ui.schedule;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import arisu.Arisu;
import arisu.model.ScheduleTableModel;
import arisu.util.SimpleBorder;
import arisu.util.UIUtil;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Transform;

public class ClassSchedulePane extends BorderPane{
	
	private Arisu app;
	private ScheduleTableView tableView;
	private ScheduleTableModel tableModel;
	private CourseListPane courseListPane;
	private ScrollPane tableScroll;
	private ScrollPane sideScroll;
	private Label title; 
	
	public ClassSchedulePane(Arisu app) {
		this.app = app;
		setPadding(new Insets(16));
		// components
		title = createTitle("Schedule Planner");
		tableView = new ScheduleTableView(this);
		tableModel = new ScheduleTableModel(this);
		tableScroll = new ScrollPane(tableView);
		courseListPane = new CourseListPane(this);
		sideScroll = new ScrollPane(courseListPane);
		sideScroll.setBorder(SimpleBorder.solid(Color.WHITESMOKE, BorderStroke.THIN));
		sideScroll.prefWidthProperty().addListener((obs,o,n) -> {
			sideScroll.setPrefWidth(tableView.getHeight());
		});
		Button button = new Button("Snapshot");
		setAlignment(button, Pos.TOP_LEFT);
		setAlignment(tableScroll, Pos.TOP_LEFT);
		// margin
		setMargin(title, new Insets(0,0,0,32));
		setMargin(tableScroll, new Insets(8,8,8,8));
		setMargin(sideScroll, new Insets(8,8,8,8));
		setTop(title);
		setCenter(tableScroll);
		setBottom(button);
		setRight(sideScroll);
		// HACK: Fix blurry ScrollPane
		UIUtil.fixBlurryScrollPane(sideScroll, tableScroll);
		// TODO: Refactor saving image
		button.setOnAction((e) -> {
			double scale = 2;
			WritableImage writableImage = new WritableImage((int)Math.rint(scale*tableView.getWidth()), (int)Math.rint(scale*tableView.getHeight()));
			SnapshotParameters spa = new SnapshotParameters();
			spa.setTransform(Transform.scale(scale, scale));
			tableView.snapshot(spa, writableImage);   
			BufferedImage image = SwingFXUtils.fromFXImage(writableImage, null);
		    File outputFile = new File("schedule.png");
			try {
			      ImageIO.write(image, "png", outputFile);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		});
	}
	
	private Label createTitle(String name) {
		Label label = new Label(name);
		label.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 32));
		return label;
	}
	
	public ScheduleTableView getTableView() {
		return tableView;
	}
	
	public ScheduleTableModel getTableModel() {
		return tableModel;
	}
}
