package com;

import java.awt.AWTException;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.imageio.ImageIO;

/**
 *
 * @author Dave Sami
 */
public class Main extends Application {

    public static Scene scene;
    private static Stage stage;
    private static TrayIcon trayIcon;
    private static Shutdown shutdown_content;
    private Hide hide_content;
    private Unhide unhide_content;
    private nav_item nav_shutdown, nav_hide, nav_unhide;
    private handler handle;

    @Override
    public void start(Stage stage) throws Exception {

        this.stage = stage;
        GridPane root = new GridPane();
        BorderPane content = new BorderPane();
        content.setMinWidth(500);
        content.getStyleClass().add("content");
        content.getStylesheets().add(getClass().getResource("/com/style/style.css").toExternalForm());
        VBox navbar = new VBox();
        navbar.getStyleClass().add("navbar");
        navbar.getStylesheets().add(getClass().getResource("/com/style/style.css").toExternalForm());

        shutdown_content = new Shutdown();
        unhide_content = new Unhide();
        hide_content = new Hide();
        handle = new handler();
        nav_shutdown = new nav_item("Shutdown");
        nav_shutdown.getStyleClass().add("active");
        content.setCenter(shutdown_content);
        nav_unhide = new nav_item("Unhide");
        nav_hide = new nav_item("Hide");
        nav_shutdown.setOnAction((event) -> {
            nav_clear((nav_item) event.getSource());
            content.setCenter(shutdown_content);
        });
        nav_unhide.setOnAction((event) -> {
            nav_clear((nav_item) event.getSource());
            content.setCenter(unhide_content);
        });
        nav_hide.setOnAction((event) -> {
            nav_clear((nav_item) event.getSource());
            content.setCenter(hide_content);
        });
        navbar.getChildren().addAll(nav_shutdown, nav_unhide, nav_hide);

        root.add(navbar, 0, 0);
        root.add(content, 1, 0);
        GridPane.setHgrow(content, Priority.ALWAYS);
        GridPane.setVgrow(content, Priority.ALWAYS);
        GridPane.setVgrow(navbar, Priority.ALWAYS);
        scene = new Scene(root);

        createTrayIcon();
        Platform.setImplicitExit(false);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("image/logo.png")));
        stage.setTitle("<ETdvlpr>  Multi-tool  </ETdvlpr>");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private class nav_item extends Button {

        public nav_item(String text) {
            super(text);
            getStyleClass().add("nav_item");
            getStylesheets().add(getClass().getResource("/com/style/style.css").toExternalForm());
        }
    }

    private void nav_clear(nav_item active) {
        nav_shutdown.getStyleClass().remove("active");
        nav_hide.getStyleClass().remove("active");
        nav_unhide.getStyleClass().remove("active");
        active.getStyleClass().add("active");
    }

    private static MenuItem tenMin, thirtyMin, oneHour, show, exit;
    public static Menu extend;

    public void createTrayIcon() {
        if (SystemTray.isSupported()) {

            SystemTray tray = SystemTray.getSystemTray();
            java.awt.Image image = null;
            try {
                image = ImageIO.read(getClass().getResource("/com/image/logo.png"));
            } catch (IOException ex) {
                System.out.println(ex);
            }

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (shutdown_content.shutdown_running && SystemTray.isSupported()) {
                                stage.hide();
                                trayIcon.displayMessage("Multi-tool", "Shutdown timer is still running.\nclose application to cancel countdown timer", TrayIcon.MessageType.INFO);
                            } else {
                                System.exit(0);
                            }
                        }
                    });
                }
            });
            PopupMenu popup = new PopupMenu();

            extend = new Menu("  Extend shutdown       ");
            tenMin = new MenuItem("  10 Minutes");
            thirtyMin = new MenuItem("  30 Minutes");
            oneHour = new MenuItem("  1 Hour");
            show = new MenuItem("  Show");
            exit = new MenuItem("  Exit");
            thirtyMin.addActionListener(handle);
            oneHour.addActionListener(handle);
            tenMin.addActionListener(handle);
            show.addActionListener(handle);
            exit.addActionListener(handle);

            extend.add(tenMin);
            extend.add(thirtyMin);
            extend.add(oneHour);
            extend.setEnabled(false);
            popup.add(extend);
            popup.add(show);
            popup.add(exit);
            trayIcon = new TrayIcon(image, "Multi-tool", popup);
            trayIcon.addActionListener(handle);

            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println(e);
            }
        }
    }

    private static class handler implements ActionListener {

        public handler() {
        }

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (e.getSource() == show || e.getSource() == trayIcon) {
                Platform.runLater(stage::show);
            } else if (e.getSource() == exit) {
                Command.Run("shutdown -a", null);
                System.exit(0);
            } else if (e.getSource() == tenMin) {
                shutdown_content.time_sec += 600;
            } else if (e.getSource() == thirtyMin) {
                shutdown_content.time_sec += 1800;
            } else if (e.getSource() == oneHour) {
                shutdown_content.time_sec += 3600;
            }
        }
    }
}
