package com;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 *
 * @author Dave Sami
 */
public class Shutdown extends BorderPane {

    private TopPanel top;
    private Label lbl_timer;
    private RunButton run_btn;
    private RadioButton radio_shutdown, radio_restart, radio_hibernate;
    private Slider time_select;
    public boolean shutdown_running;
    public int time_sec;

    public Shutdown() {
        super();
        top = new TopPanel("This is a shutdown timer. \nSet the timer using the slider below it and select shutdown, hibernate or restart.");
        lbl_timer = new Label("00:30:00");
        run_btn = new RunButton();
        radio_hibernate = new RadioButton("Hibernate");
        radio_shutdown = new RadioButton("Shutdown");
        radio_restart = new RadioButton("Restart");
        ToggleGroup group = new ToggleGroup();
        time_select = new Slider(1, 180, 30);
        time_select.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                time_sec = (int) (60 * time_select.getValue());
                lbl_timer.setText(String.format("%02d:%02d:%02d", time_sec / 3600, (time_sec % 3600) / 60, time_sec % 60));
            }
        });
        run_btn.btn.setOnAction((event) -> {
            run_shutdown(event);
        });

        radio_shutdown.setSelected(true);
        radio_hibernate.setToggleGroup(group);
        radio_shutdown.setToggleGroup(group);
        radio_restart.setToggleGroup(group);
        radio_hibernate.getStyleClass().add("option");
        radio_shutdown.getStyleClass().add("option");
        radio_restart.getStyleClass().add("option");

        lbl_timer.getStyleClass().add("lbl_timer");
        lbl_timer.getStylesheets().add(getClass().getResource("/com/style/shutdown.css").toExternalForm());
        VBox options = new VBox();
        options.getStylesheets().add(getClass().getResource("/com/style/style.css").toExternalForm());
        options.getChildren().addAll(radio_shutdown, radio_restart, radio_hibernate);

        GridPane grid = new GridPane();
        grid.add(lbl_timer, 0, 0);
        grid.add(time_select, 0, 1);
        grid.add(options, 0, 2);
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(50, 10, 10, 50));
        grid.setVgap(15);
        grid.setHgap(15);

        super.setTop(top);
        super.setCenter(grid);
        super.setBottom(run_btn);
    }

    private Timeline timeline;

    private void run_shutdown(ActionEvent event) {
        if (!shutdown_running) {
            time_select.setDisable(true);
            radio_hibernate.setDisable(true);
            radio_shutdown.setDisable(true);
            radio_restart.setDisable(true);
            time_sec = (int) (time_select.getValue() * 60);

            String command = "shutdown ";
            if (radio_shutdown.isSelected()) {
                command += "-s";
            } else {
                command += "-r";
            }
            command += " -t " + time_sec;
            if (!radio_hibernate.isSelected()) {
                Command.Run(command, null);
            }

            timeline = new Timeline();
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    time_sec--;
                    lbl_timer.setText(String.format("%02d:%02d:%02d", time_sec / 3600, (time_sec % 3600) / 60, time_sec % 60));
                    if (time_sec <= 0) {
                        timeline.stop();
                        if (radio_hibernate.isSelected()) {
                            Command.Run("shutdown /h", null);
                        }
                    }
                }
            }));
            timeline.playFromStart();
            shutdown_running = true;
            run_btn.btn.setText("Stop");
            Main.extend.setEnabled(true);
        } else {
            timeline.stop();
            String command = "shutdown -a";
            if (!radio_hibernate.isSelected()) {
                Command.Run(command, null);
            }
            time_select.setDisable(false);
            radio_hibernate.setDisable(false);
            radio_shutdown.setDisable(false);
            radio_restart.setDisable(false);
            shutdown_running = false;
            Main.extend.setEnabled(false);
            run_btn.btn.setText("Run");
        }
    }
}
