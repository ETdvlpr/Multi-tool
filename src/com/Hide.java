package com;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

/**
 *
 * @author Dave Sami
 */
public class Hide extends BorderPane {

    private Label lbl_target, lbl_cover, lbl_output, lbl_msg;
    private TextField field_target, field_cover, field_output;
    private Button btn_target, btn_cover, btn_output;
    private RunButton run_btn;

    public Hide() {
        TopPanel top = new TopPanel("Steganography is the art and science of hiding messages, images, data etc..."
                + "\nIn such a way that no one suspects the presence of data.",
                "\nStep 1. create a folder and add all the files you wish to hide inside this folder."
                + "\nStep 2. right-click and archive this folder"
                + "\nStep 3. select the newly archived file as target folder"
                + "\nStep 4. select the cover file (preferably an image file)"
                + "\nStep 5. select your output location"
                + "\nStep 6. your output will be the same as your cover file but a bit larger"
                + "\nyou can recover your files by opening the output with winrar");
        lbl_target = new Label("Target Folder");
        lbl_cover = new Label("Cover File");
        lbl_output = new Label("Output File");
        lbl_msg = new Label();
        field_target = new TextField();
        field_target.setPrefWidth(200);
        field_cover = new TextField();
        field_cover.setPrefWidth(200);
        field_output = new TextField();
        field_output.setPrefWidth(200);
        btn_target = new Button("Browse");
        btn_cover = new Button("Browse");
        btn_output = new Button("Browse");
        run_btn = new RunButton();
        run_btn.btn.setOnAction((event) -> {
            run_hide();
        });
        btn_target.setOnAction((event) -> {
            browse_target();
        });
        btn_cover.setOnAction((event) -> {
            browse_cover();
        });
        btn_output.setOnAction((event) -> {
            browse_output();
        });
        GridPane grid = new GridPane();
        grid.add(lbl_target, 0, 0);
        grid.add(field_target, 1, 0);
        grid.add(btn_target, 2, 0);
        grid.add(lbl_cover, 0, 1);
        grid.add(field_cover, 1, 1);
        grid.add(btn_cover, 2, 1);
        grid.add(lbl_output, 0, 2);
        grid.add(field_output, 1, 2);
        grid.add(btn_output, 2, 2);
        grid.setAlignment(Pos.CENTER);
        grid.getStylesheets().add(getClass().getResource("/com/style/hide_unhide.css").toExternalForm());

        super.setTop(top);
        super.setCenter(grid);
        super.setBottom(run_btn);
    }

    private void run_hide() {
        File f = new File(field_target.getText());
        if (f.isFile()) {
            f = new File(field_cover.getText());
            if (f.isFile()) {
                String cmd = "Copy /b \"" + field_cover.getText() + "\" + \"" + field_target.getText() + "\" \"" + field_output.getText() + "\"";
                Command.Run(cmd, null);
                lbl_msg.setText("***   successful   ***");
            } else {
                lbl_msg.setText("Please make sure correct cover file is selected");
            }
        } else {
            lbl_msg.setText("Please make sure correct file is in target");
        }
    }

    private void browse_target() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Target folder to be hidden");
        File choice = fc.showOpenDialog(Main.scene.getWindow());
        if(choice!=null)
            field_target.setText(choice.getAbsolutePath());
    }

    private void browse_cover() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Cover file");
        File item = fc.showOpenDialog(Main.scene.getWindow());
        if (item != null) {
            String file = item.getAbsolutePath();
            field_cover.setText(file);
            field_output.setText(item.getParent().concat("\\new_hidden")+file.substring(file.lastIndexOf('.')));
        }
    }

    private void browse_output() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Output file");
        File output = fc.showSaveDialog(Main.scene.getWindow());
        if (output != null) {
            field_output.setText(output.getAbsolutePath());
        }
    }
}
