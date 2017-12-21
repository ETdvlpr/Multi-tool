package com;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author Dave Sami
 */
public class TopPanel extends VBox{

    public TopPanel(String text) {
        super();
        Label lbl_top = new Label(text);
        lbl_top.getStyleClass().add("top_label");
        lbl_top.getStylesheets().add(getClass().getResource("/com/style/style.css").toExternalForm());
        lbl_top.setTextAlignment(TextAlignment.CENTER);
        lbl_top.setWrapText(true);
        super.getChildren().add(lbl_top);
        super.getChildren().add(new Separator(Orientation.HORIZONTAL));
        super.setAlignment(Pos.CENTER);
    }
    public TopPanel(String t1, String t2) {
        Label lbl_top = new Label(t1);
        Label l2 = new Label(t2);
        lbl_top.getStyleClass().add("top_label");
        l2.getStyleClass().add("top_label");
        lbl_top.getStylesheets().add(getClass().getResource("/com/style/style.css").toExternalForm());
        l2.getStylesheets().add(getClass().getResource("/com/style/style.css").toExternalForm());
        lbl_top.setTextAlignment(TextAlignment.CENTER);
        l2.setTextAlignment(TextAlignment.JUSTIFY);
        super.getChildren().add(lbl_top);
        super.getChildren().add(l2);
        super.getChildren().add(new Separator(Orientation.HORIZONTAL));
    }
}
