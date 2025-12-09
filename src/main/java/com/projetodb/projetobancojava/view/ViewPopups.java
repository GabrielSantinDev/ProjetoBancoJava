package com.projetodb.projetobancojava.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
public class ViewPopups {

    public static void mostrarPopupErro(String mensagem) {
        Stage popup = new Stage();

        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setResizable(false);
        popup.setTitle("Erro");

        Label icon = new Label("⚠");
        icon.setStyle("-fx-font-size: 40; -fx-text-fill: red");

        Label lbl = new Label(mensagem);
        lbl.setStyle("-fx-font-size: 16; -fx-text-fill: #333;");

        Button btnOk = new Button("OK");
        btnOk.setStyle(
                "-fx-background-color: linear-gradient(to right, #4a67e1, #6aceec); -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 20; -fx-background-radius: 4px;"
        );
        btnOk.setOnAction(e -> popup.close());

        VBox root = new VBox(15, icon, lbl, btnOk);
        root.setAlignment(Pos.CENTER);
        root.setStyle(
                "-fx-background-color: white; -fx-padding: 20; -fx-border-radius: 6; -fx-background-radius: 6;"
        );

        root.setEffect(new DropShadow(20, Color.gray(0, 0.4)));

        Scene scene = new Scene(root);
        popup.setScene(scene);

        popup.showAndWait();
    }

}
