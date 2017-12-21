package com;

import java.io.File;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

/**
 *
 * @author Dave Sami
 */
public class Unhide extends BorderPane{

    private RunButton run_btn;
    private TextField field_unhide;
    private Button btn_browse;
    private CheckBox check_hidden, check_system, check_readOnly;
    private Label lbl_msg;
    
    public Unhide() {
        TopPanel top = new TopPanel("Use the 'Browse' button to navigate to the foler you want to unhide or\ntype in the correct path in the textbox and press the run button.");
        lbl_msg = new Label();
        run_btn = new RunButton();
        field_unhide = new TextField();
        btn_browse = new Button("Browse");
        check_hidden = new CheckBox("Hidden");
        check_system = new CheckBox("System");
        check_readOnly = new CheckBox("Read Only");
        check_hidden.setSelected(true);
        check_system.setSelected(true);
        check_readOnly.setSelected(true);
        run_btn.btn.setOnAction((event) -> {
            unhide_run();
        });
        btn_browse.setOnAction((event) -> {
            browse_file();
        });
        VBox options = new VBox(check_hidden, check_system, check_readOnly, lbl_msg);
        options.getStylesheets().add(getClass().getResource("/com/style/hide_unhide.css").toExternalForm());
        HBox file_choose = new HBox(field_unhide, btn_browse);
        file_choose.setAlignment(Pos.CENTER);
        options.setAlignment(Pos.CENTER);
        
        super.setLeft(options);
        super.setCenter(file_choose);
        super.setTop(top);
        super.setBottom(run_btn);
    }    
    private void unhide_run() {
        File f = new File(field_unhide.getText());
        String cmd = " && attrib ";
        cmd = check_hidden.isSelected() ? cmd + "-h " : cmd;
        cmd = check_system.isSelected() ? cmd + "-s " : cmd;
        cmd = check_readOnly.isSelected() ? cmd + "-r " : cmd;
        if (" && attrib ".equals(cmd)) {
            lbl_msg.setText("need to select atleast one of the options");
        } else {
            cmd += "/s /d ";
            if (f.isDirectory()) {
                Command.Run(field_unhide.getText().substring(0, 2) + cmd, field_unhide.getText());
                lbl_msg.setText("***   success   ***");
            } else {
                lbl_msg.setText("***   make sure you select a folder   ***");
            }
        }
    }
    private void browse_file() {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Target folder to be hidden");
        File choice = dc.showDialog(Main.scene.getWindow());
        if(choice!=null)
            field_unhide.setText(choice.getAbsolutePath());
    }
}
