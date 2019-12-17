package arisu.ui.schedule;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import arisu.Arisu;
import arisu.model.schedule.CourseAdaptor;
import arisu.model.schedule.SingleCourseEntry;
import arisu.util.UIUtil;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.transform.Transform;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ClassScheduleControlPane extends HBox {
	private ClassSchedulePane schedulePane;
	private Button save;
	private Button add;
	private Button duplicate;
	private Button edit;
	private Button delete;
	private FileChooser fileChooser;
	private static final double DIALOG_WIDTH = 300;
	private static final double DIALOG_HEIGHT = 600;

	public ClassScheduleControlPane(ClassSchedulePane schedulePane) {
		this.schedulePane = schedulePane;
		setPrefHeight(50);
		setSpacing(12);
		setAlignment(Pos.CENTER_RIGHT);
		save = new Button("Save Image");
		save.setStyle("-fx-font-size: 14px");
		save.setOnAction(this::onSave);
		add = new Button("New");
		add.setStyle("-fx-font-size: 14px");
		add.setOnAction(this::onAdd);
		edit = new Button("Edit");
		edit.setStyle("-fx-font-size: 14px");
		edit.setOnAction(this::onEdit);
		duplicate = new Button("Duplicate");
		duplicate.setStyle("-fx-font-size: 14px");
		duplicate.setOnAction(this::onDuplicate);
		delete = new Button("Delete");
		delete.setStyle("-fx-font-size: 14px");
		delete.setOnAction(this::onDelete);
		getChildren().addAll(save, add, duplicate, edit, delete);
		// File chooser
		fileChooser = new FileChooser();
		fileChooser.setTitle("Save Image");
		fileChooser.setInitialFileName("table");
		FileChooser.ExtensionFilter png = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
		fileChooser.getExtensionFilters().add(png);
	}

	private Stage createBaseStage() {
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setResizable(false);
		return stage;
	}

	private <T extends Parent> T styleRoot(T root) {
		UIUtil.styleNode(root);
		return root;
	}

	public void onAdd(ActionEvent event) {
		// setup stage
		Stage stage = createBaseStage();
		stage.setTitle("Add new item");
		stage.getIcons().add(new Image(ClassLoader.getSystemResource("assets/dialog/add.png").toString()));
		// create dialog
		ModifyCourseDialog root = styleRoot(new ModifyCourseDialog(stage, schedulePane));
		// show and wait for input
		showDialogUsing(stage, root);
	}

	public void onEdit(ActionEvent event) {
		if (!checkEligibility())
			return;
		CourseAdaptor selected = schedulePane.getCourseListPane().getSelected();
		// setup stage
		Stage stage = createBaseStage();
		stage.getIcons().add(new Image(ClassLoader.getSystemResource("assets/dialog/pencil.png").toString()));
		stage.setTitle(selected.getCourseNumber() + (selected.getCourseName().isBlank() ? "" : " - ")
				+ selected.getCourseName());
		// create dialog
		ModifyCourseDialog root = styleRoot(new ModifyCourseDialog(stage, schedulePane));
		root.setEditMode(true);
		root.feedCurrentData((SingleCourseEntry) selected);
		// show and wait for input
		showDialogUsing(stage, root);
	}

	public void onDuplicate(ActionEvent event) {
		if (!checkEligibility())
			return;
		CourseAdaptor selected = schedulePane.getCourseListPane().getSelected();
		// setup stage
		Stage stage = createBaseStage();
		stage.getIcons().add(new Image(ClassLoader.getSystemResource("assets/dialog/copy.png").toString()));
		stage.setTitle("Duplicate");
		// create dialog
		ModifyCourseDialog root = styleRoot(new ModifyCourseDialog(stage, schedulePane));
		root.feedCurrentData((SingleCourseEntry) selected);
		// show and wait for input
		showDialogUsing(stage, root);
	}

	private boolean checkEligibility() {
		CourseAdaptor selected = schedulePane.getCourseListPane().getSelected();
		if (selected == null) {
			return false;
		}
		if (!(selected instanceof SingleCourseEntry)) {
			Alert alert = UIUtil.makeAlert(AlertType.ERROR, "Course data from Reg can't be edit!");
			alert.show();
			return false;
		}
		return true;
	}

	private void showDialogUsing(Stage stage, Parent root) {
		Scene scene = new Scene(root, DIALOG_WIDTH, DIALOG_HEIGHT);
		// show and wait for input
		stage.setScene(scene);
		stage.showAndWait();
	}

	public void onSave(ActionEvent event) {
		ScheduleTableView tableView = schedulePane.getTableView();
		double scale = 2;
		WritableImage writableImage = new WritableImage((int) Math.rint(scale * tableView.getWidth()),
				(int) Math.rint(scale * tableView.getHeight()));
		SnapshotParameters spa = new SnapshotParameters();
		spa.setTransform(Transform.scale(scale, scale));
		tableView.snapshot(spa, writableImage);
		BufferedImage image = SwingFXUtils.fromFXImage(writableImage, null);

		// Show save file dialog
		File file = fileChooser.showSaveDialog(Arisu.app().getStage());
		if (file != null) {
			fileChooser.setInitialDirectory(file.getParentFile());
			try {
				ImageIO.write(image, "png", file);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void onDelete(ActionEvent event) {
		CourseAdaptor cc = schedulePane.getCourseListPane().getSelected();
		if (cc != null) {
			cc.removeFrom(schedulePane.getTableView());
			schedulePane.getTableModel().remove(cc);
		}
	}
}
