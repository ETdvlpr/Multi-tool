package com;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author Dave Sami
 */

public class RunButton extends VBox{
    public Button btn;
    public RunButton() {
        btn = new Button("Run");
        btn.getStyleClass().add("btn_run");
        btn.getStylesheets().add(getClass().getResource("/com/style/style.css").toExternalForm());
        btn.setTextAlignment(TextAlignment.CENTER);
        
        super.getChildren().add(btn);
        super.setAlignment(Pos.BOTTOM_RIGHT);
    }
    
}
