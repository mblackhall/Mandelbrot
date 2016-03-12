package com.marcus.draw;

import com.marcus.draw.algorithms.*;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * @author marcus
 */
public class DrawingApp extends Application {
    private final static double WIDTH = 700;
    private static final double HEIGHT = 700;
    public static int MAX_ITS = 1000;

    double xstep = 3 / WIDTH, ystep = xstep;

    double x0 = -2, y0 = 1.5;
    DetermineColor dc = new Scheme1();
    Canvas canvas = new Canvas(WIDTH, HEIGHT);

    Rectangle r = new Rectangle();
    WritableImage image = new WritableImage((int) WIDTH, (int) HEIGHT);
    PixelWriter pw = image.getPixelWriter();

    Pane pane = new Pane();
    final GraphicsContext context = canvas.getGraphicsContext2D();

    public static void main(String[] args) {

        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        BorderPane root = new BorderPane();

        MenuBar menuBar = new MenuBar();

        // --- Menu File
        Menu menuColors = new Menu("Colours");
        MenuItem scheme1 = new MenuItem("Scheme1");
        MenuItem scheme2 = new MenuItem("Scheme2");
        MenuItem scheme3 = new MenuItem("Scheme3");
        menuColors.getItems().add(scheme1);
        menuColors.getItems().add(scheme2);
        menuColors.getItems().add(scheme3);

        menuBar.getMenus().addAll(menuColors);
        root.setTop(menuBar);

        r.setFill(Color.TRANSPARENT);
        r.setStroke(Color.WHITE);

        // VBox root = new VBox();

        pane.setPrefWidth(WIDTH);
        pane.setPrefHeight(HEIGHT);
        // root.getChildren().add(pane);
        pane.getChildren().add(canvas);
        pane.getChildren().add(r);


        MyEventhandler mouseHandler = new MyEventhandler();
        pane.setOnMouseDragged(mouseHandler);
        pane.setOnMousePressed(mouseHandler);
        pane.setOnMouseReleased(mouseHandler);
        pane.setOnMouseClicked(mouseHandler);


        root.setCenter(pane);
        stage.setResizable(false);
        Scene scene = new Scene(root, WIDTH, HEIGHT + 100);
        scene.setFill(Color.LIGHTGRAY);
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "1000",
                        "10000",
                        "100000"
                );
        final ComboBox comboBox = new ComboBox(options);
        root.setBottom(comboBox);
        scheme1.setOnAction(

                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {

                        dc = new Scheme1();
                        redraw();
                    }
                });
        scheme2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                dc = new Scheme2();
                redraw();
            }
        });
        scheme3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                dc = new Scheme3();
                redraw();
            }
        });
        comboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue observableValue, String o, String t1) {

                MAX_ITS = Integer.parseInt(t1);
            }
        });

        redraw();
        stage.setScene(scene);
        stage.setTitle("IVIK Mandelbrot set");
        stage.show();
    }

    void callFunction(PixelWriter pw, Algorithm algo) {

        double xn = 0;
        double yn = 0;
        int noIts = 0;

        for (int i = 0; i < WIDTH; i++) {
            xn = x0 + xstep * i;

            for (int j = 0; j < HEIGHT; j++) {
                yn = y0 - ystep * j;
                noIts = algo.execute(xn, yn);
                // Color c = Color.hsb(0.95f + 10 * noIts, 0.6f, 1.0f);
                pw.setColor(i, j, dc.pixelColor(noIts));
            }
        }


    }

    class MyEventhandler implements EventHandler<MouseEvent> {

        double startx, starty;


        @Override
        public void handle(MouseEvent mouseEvent) {


            if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
                System.out.println("mouse pressed");
                startx = mouseEvent.getX();
                starty = mouseEvent.getY();
                r.setX(startx);
                r.setY(starty);
                r.setWidth(0);
                r.setHeight(0);

            }

            if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_DRAGGED)) {

                r.setWidth(mouseEvent.getX() - startx);
                r.setHeight(mouseEvent.getX() - startx);

            }
            if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {


                if ((Math.abs(mouseEvent.getX() - startx)) < 0.001
                        && Math.abs(mouseEvent.getY() - starty) < 0.001
                        ) {
                    return;
                }

                r.setWidth(0);
                r.setHeight(0);

                double xn = x0 + startx * xstep;
                double yn = y0 - starty * ystep;

                double newy = y0 + mouseEvent.getY();
                double newx = x0 + mouseEvent.getX() * xstep;

                x0 = xn;
                y0 = yn;
                xstep = (newx - xn) / WIDTH;
                ystep = xstep;
                redraw();
            }
        }


    }

    private void redraw() {
        context.clearRect(0, 0, WIDTH, HEIGHT);
        image = new WritableImage((int) WIDTH, (int) HEIGHT);
        PixelWriter pw = image.getPixelWriter();
        callFunction(pw, new MandelBrot());
        context.drawImage(image, 0, 0);
    }

}

