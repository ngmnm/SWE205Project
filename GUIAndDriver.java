
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class GUIAndDriver extends Application {
	
	static File workspace = new File("DataBase.dat");
	static FileOutputStream out;
	static FileInputStream in;
	static ArrayList<DrawShape> shapeArray = new ArrayList<DrawShape>();
	
	
	public static void save(Stage stage) {
		try {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Export");

			FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("DAT files (*.dat*)",
					"*.dat");
			fileChooser.getExtensionFilters().add(extensionFilter);

			workspace = fileChooser.showSaveDialog(stage);

			out = new FileOutputStream(workspace);
			ObjectOutputStream write = new ObjectOutputStream(out);

			write.writeObject(shapeArray);
			write.close();
		} catch (Exception e) {

			e.getStackTrace();

		}
	}
	
	public static void main(String[] args) {
		
		
		launch(args);
	}

	public static String shapeType = "";
	public String color = "";

	private static DrawShape tempShape;

	@Override
	public void start(Stage arg0) throws Exception {

		// Background setup
		BorderPane mainPane = new BorderPane();
		GridPane vMenuPane = new GridPane();
		GridPane hMenuPane = new GridPane();
		vMenuPane.setPadding(new Insets(15));
		hMenuPane.setPadding(new Insets(10));

		vMenuPane.setVgap(25);
		vMenuPane.setHgap(10);
		hMenuPane.setVgap(8);
		hMenuPane.setHgap(10);

		Rectangle2D screenBounds = Screen.getPrimary().getBounds();
		Scene mainScene = new Scene(mainPane, screenBounds.getWidth(), (screenBounds.getHeight() * 0.94));
		mainScene.setFill(Color.LIGHTGREY);
		arg0.setScene(mainScene);
		arg0.setTitle("MyPaintShop");
		arg0.show();

		Canvas drawRegion = new Canvas((mainScene.getWidth()), (mainScene.getHeight() - 210));

		AnchorPane anchorPane = new AnchorPane();
		mainPane.getChildren().add(anchorPane);
		anchorPane.getChildren().add(drawRegion);

		mainPane.getChildren().add(drawRegion);
		Rectangle vMenuBar = new Rectangle(0, 0, 250, mainScene.getWidth());
		vMenuBar.setFill(Color.GREY);

		Rectangle shapeRec = new Rectangle(10, 60, 230, 345);
		shapeRec.setFill(Color.LIGHTGREY);

		Rectangle colorRec = new Rectangle(10, 500, 230, 160);
		colorRec.setFill(Color.LIGHTGREY);

		Rectangle hMenuBar = new Rectangle(250, (drawRegion.getHeight() + 25), mainScene.getWidth() - 250, 300);
		hMenuBar.setFill(Color.GREY);

		Rectangle propRec = new Rectangle(285, (drawRegion.getHeight() + 40), mainScene.getWidth() - 765, 155);
		propRec.setFill(Color.LIGHTGREY);

		Rectangle workSpcRec = new Rectangle(mainScene.getWidth() - 410, (drawRegion.getHeight() + 40), 200, 155);
		workSpcRec.setFill(Color.LIGHTGREY);

		Line vMenuBorderLine = new Line(250, 0, 250, mainScene.getHeight());
		Line hMenuBorderLine = new Line(250, (drawRegion.getHeight() + 25), mainScene.getWidth(),
				(drawRegion.getHeight() + 25));

		mainPane.getChildren().addAll(vMenuBar, hMenuBar, vMenuBorderLine, hMenuBorderLine, shapeRec, colorRec, propRec,
				workSpcRec);
		mainPane.setLeft(vMenuPane);
		mainPane.setBottom(hMenuPane);

		// change font settings
		Font mainFont = new Font(20);

		// Shapes setup
		Label shapes = new Label("Shapes:");
		shapes.setFont(mainFont);
		vMenuPane.add(shapes, 0, 0);

		// triangle setup
		double xShift = 10;
		double yShift = -30;
		double tSize = 1.2;
		Polygon triBtn = new Polygon(tSize * 50 + xShift, tSize * 100 + yShift, tSize * 10 + xShift,
				tSize * 150 + yShift, tSize * 90 + xShift, tSize * 150 + yShift);
		triBtn.setFill(Color.GRAY);

		triBtn.setOnMouseClicked(e -> {

			drawRegion.addEventFilter(MouseEvent.ANY, new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {

					if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
						tempShape = new DrawTriangle(event.getX(), event.getY(), color);

						tempShape.setPosX(event.getX());
						tempShape.setPosY(event.getY());
						tempShape.createShape(event, anchorPane);

					}
					if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {

						tempShape.editShape(event);
						tempShape.setPosX(event.getX());
						tempShape.setPosY(event.getY());

					}
					if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {

						drawRegion.removeEventFilter(MouseEvent.ANY, this);
						tempShape.addShape(tempShape, shapeArray);
					}
				}
			});

		});

		// square setup
		Rectangle sqrBtn = new Rectangle(140, 190, 80, 80);
		sqrBtn.setFill(Color.GREY);

		sqrBtn.setOnMouseClicked(e -> {

			drawRegion.addEventFilter(MouseEvent.ANY, new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {

					if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
						tempShape = new DrawSquare(event.getX(), event.getY(), color);

						tempShape.setPosX(event.getX());
						tempShape.setPosY(event.getY());
						tempShape.createShape(event, anchorPane);

					}
					if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {

						tempShape.editShape(event);
						tempShape.setPosX(event.getX());
						tempShape.setPosY(event.getY());

					}
					if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
						tempShape.addShape(tempShape,shapeArray);
						drawRegion.removeEventFilter(MouseEvent.ANY, this);

					}
				}
			});

		});

		// ellipse setup
		Ellipse elpsBtn = new Ellipse(75, 230, 55, 30);
		elpsBtn.setFill(Color.GREY);

		elpsBtn.setOnMouseClicked(e -> {

			drawRegion.addEventFilter(MouseEvent.ANY, new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {

					if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
						tempShape = new DrawEllipse(event.getX(), event.getY(), color);

						tempShape.setPosX(event.getX());
						tempShape.setPosY(event.getY());
						tempShape.createShape(event, anchorPane);

					}
					if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {

						tempShape.editShape(event);
						tempShape.setPosX(event.getX());
						tempShape.setPosY(event.getY());

					}
					if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
						tempShape.addShape(tempShape,shapeArray);
						drawRegion.removeEventFilter(MouseEvent.ANY, this);

					}
				}
			});

		});

		// rectangle setup
		Rectangle recBtn = new Rectangle(40, 300, 170, 75);
		recBtn.setFill(Color.GREY);

		recBtn.setOnMouseClicked(e -> {

			drawRegion.addEventFilter(MouseEvent.ANY, new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {

					if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
						tempShape = new DrawRectangle(event.getX(), event.getY(), color);

						tempShape.setPosX(event.getX());
						tempShape.setPosY(event.getY());
						tempShape.createShape(event, anchorPane);

					}
					if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {

						tempShape.editShape(event);
						tempShape.setPosX(event.getX());
						tempShape.setPosY(event.getY());

					}
					if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
						tempShape.addShape(tempShape,shapeArray);
						drawRegion.removeEventFilter(MouseEvent.ANY, this);
					}
				}
			});

		});

		// circle setup
		Circle cirBtn = new Circle(180, 120, 40);
		cirBtn.setFill(Color.GREY);

		cirBtn.setOnMouseClicked(e -> {

			drawRegion.addEventFilter(MouseEvent.ANY, new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {

					if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
						tempShape = new DrawCircle(event.getX(), event.getY(), color);

						tempShape.setPosX(event.getX());
						tempShape.setPosY(event.getY());
						tempShape.createShape(event, anchorPane);

					}
					if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {

						tempShape.editShape(event);
						tempShape.setPosX(event.getX());
						tempShape.setPosY(event.getY());

					}
					if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
						tempShape.addShape(tempShape,shapeArray);
						drawRegion.removeEventFilter(MouseEvent.ANY, this);

						

					}

				}
			});

		});

		mainPane.getChildren().addAll(triBtn, cirBtn, sqrBtn, elpsBtn, recBtn);

		// Filler (empty Label)
		Label filler1 = new Label("");
		filler1.setFont(new Font(250));
		vMenuPane.add(filler1, 0, 1);

		// Colors setup
		Label colors = new Label("Colors:");
		colors.setFont(mainFont);
		vMenuPane.add(colors, 0, 2);
		Button[][] colorArray = new Button[3][3];
		for (int i = 0; i < colorArray.length; i++) {
			for (int j = 0; j < colorArray.length; j++) {
				colorArray[i][j] = new Button("               ");
				colorArray[i][j].setShape(new Circle(1.5));
				vMenuPane.add(colorArray[i][j], j, i + 3);
			}
		}
		colorArray[0][0].setStyle("-fx-background-color: black");
		colorArray[0][0].setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				color = "BLACK";

			}
		});

		colorArray[0][1].setStyle("-fx-background-color: brown");
		colorArray[0][1].setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				color ="BROWN";

			}
		});

		colorArray[0][2].setStyle("-fx-background-color: red");
		colorArray[0][2].setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				color = "RED";

			}
		});

		colorArray[1][0].setStyle("-fx-background-color: blue");
		colorArray[1][0].setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				color = "BLUE";

			}
		});

		colorArray[1][1].setStyle("-fx-background-color: yellow");
		colorArray[1][1].setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				color = "YELLOW";

			}
		});

		colorArray[1][2].setStyle("-fx-background-color: orange");
		colorArray[1][2].setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				color = "ORANGE";

			}
		});
		colorArray[2][0].setStyle("-fx-background-color: green");
		colorArray[2][0].setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				color = "GREEN";

			}
		});

		colorArray[2][1].setStyle("-fx-background-color: grey");
		colorArray[2][1].setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				color = "GREY";

			}
		});
		colorArray[2][2].setStyle("-fx-background-color: white");
		colorArray[2][2].setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				color = "WHITE";

			}
		});

		// Filler (Empty Label)
		Label filler2 = new Label("                                 ");
		filler2.setFont(new Font(30));
		hMenuPane.add(filler2, 0, 0);

		// Filler (Empty Label)
		Label filler3 = new Label("");
		filler3.setFont(new Font(0.5));
		hMenuPane.add(filler3, 1, 4);

		// Shape Properties (Position & Delete-Shape Button)
		Label shapeprop = new Label("Shape Properties:");
		shapeprop.setFont(mainFont);
		hMenuPane.add(shapeprop, 1, 0);

		Button delShape = new Button("Delete Shape");
		delShape.setStyle("-fx-text-fill: red");
		GridPane.setValignment(delShape, VPos.BOTTOM);
		GridPane.setHalignment(delShape, HPos.CENTER);
		hMenuPane.add(delShape, 2, 0);

		Label xLabel = new Label("Position X =");
		xLabel.setFont(mainFont);
		GridPane.setHalignment(xLabel, HPos.RIGHT);
		hMenuPane.add(xLabel, 1, 1);
		TextField xIn = new TextField();
		hMenuPane.add(xIn, 2, 1);

		Label yLabel = new Label("Position Y =");
		yLabel.setFont(mainFont);
		GridPane.setHalignment(yLabel, HPos.RIGHT);
		TextField yIn = new TextField();
		hMenuPane.add(yIn, 2, 2);
		hMenuPane.add(yLabel, 1, 2);

		// Shape Properties (Width, Length and Radius(major/minor) )
		Label lengthLabel = new Label("              Length = ");
		lengthLabel.setFont(mainFont);
		GridPane.setHalignment(lengthLabel, HPos.RIGHT);
		TextField lengthIn = new TextField();
		hMenuPane.add(lengthIn, 4, 1);
		hMenuPane.add(lengthLabel, 3, 1);

		Label widthLabel = new Label("              Width = ");
		widthLabel.setFont(mainFont);
		GridPane.setHalignment(widthLabel, HPos.RIGHT);
		TextField widthIn = new TextField();
		hMenuPane.add(widthIn, 4, 2);
		hMenuPane.add(widthLabel, 3, 2);

		Label RadiusLabel = new Label("              Radius = ");
		RadiusLabel.setFont(mainFont);
		GridPane.setHalignment(RadiusLabel, HPos.RIGHT);
		TextField RadiusIn = new TextField();
		hMenuPane.add(RadiusIn, 4, 3);
		hMenuPane.add(RadiusLabel, 3, 3);

		Label majorLabel = new Label("Major = ");
		majorLabel.setFont(mainFont);
		GridPane.setHalignment(majorLabel, HPos.RIGHT);
		TextField majorIn = new TextField();
		hMenuPane.add(majorIn, 6, 3);
		hMenuPane.add(majorLabel, 5, 3);

		Label minorLabel = new Label("Minor = ");
		minorLabel.setFont(mainFont);
		GridPane.setHalignment(minorLabel, HPos.RIGHT);
		TextField minorIn = new TextField();
		hMenuPane.add(minorIn, 8, 3);
		hMenuPane.add(minorLabel, 7, 3);

		// Workspace Options (Save, Open and Delete)
		Label Workoptn = new Label("              Workspace Options:");
		Workoptn.setFont(mainFont);
		hMenuPane.add(Workoptn, 9, 0);

		Button saveBtn = new Button("        Save...    ");
		saveBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				save(arg0);

			}
		});
		hMenuPane.add(saveBtn, 9, 1);
		GridPane.setHalignment(saveBtn, HPos.CENTER);

		Button openBtn = new Button("       Open...    ");
		hMenuPane.add(openBtn, 9, 2);
		GridPane.setHalignment(openBtn, HPos.CENTER);
		openBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

			
			}
		});

		

		Button clearBtn = new Button("      CLEAR      ");
		clearBtn.setStyle("-fx-text-fill: red");
		hMenuPane.add(clearBtn, 9, 3);
		GridPane.setHalignment(clearBtn, HPos.CENTER);

		mainPane.setStyle("-fx-background-color: #FFFFFF;");

		shapeType = "rectangle";
		Button submit = new Button("      Submit Changes      ");
	//	Submit.setStyle("-fx-text-fill: red");
		hMenuPane.add(submit, 4, 0);
		GridPane.setHalignment(submit, HPos.CENTER);
		GridPane.setValignment(submit, VPos.BOTTOM);
		
		
		
		
		drawRegion.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				xIn.setText(event.getX() + "");
				yIn.setText(event.getY() + "");

				//for (Shape shape : Shape.getShapeArray()) {

					//if (shape.isSelected(event.getX(), event.getY())) {
						//System.out.println("zq");
						//shape.editShape(Double.parseDouble(widthIn.getText()),Double.parseDouble(lengthIn.getText()));
					

					//}
					//;
				//}
			}
		});
		submit.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
			
			}
		});
		
	}

}
