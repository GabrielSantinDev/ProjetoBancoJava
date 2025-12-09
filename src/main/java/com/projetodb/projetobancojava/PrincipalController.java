package com.projetodb.projetobancojava;

import com.projetodb.projetobancojava.control.UserControl;
import com.projetodb.projetobancojava.database.ConexaoDB;
import com.projetodb.projetobancojava.model.Funcionario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class PrincipalController {

    @FXML
    private StackPane stackPane;

    @FXML
    private VBox vBoxMenu;

    @FXML
    private HBox hBoxSair;

    @FXML
    private Button btnHome, btnContas, btnTransacoes, btnClientes, btnFuncionarios, btnLogs;

    @FXML
    Pane home, contas, transacoes, clientes, funcionarios, logs;

    private static final Funcionario user = UserControl.getUser();
    private static final String cargo = user.getCargo();


    @FXML
    private void initialize() throws IOException {
        configPanel();
        marcarSelecionado(btnHome);

        btnHome.setOnAction(e -> {
            stackPane.getChildren().setAll(home);
            marcarSelecionado(btnHome);
        });

        btnContas.setOnAction(e -> {
            stackPane.getChildren().setAll(contas);
            marcarSelecionado(btnContas);
        });

        btnTransacoes.setOnAction(e -> {
            stackPane.getChildren().setAll(transacoes);
            marcarSelecionado(btnTransacoes);
        });

        btnClientes.setOnAction(e -> {
            stackPane.getChildren().setAll(clientes);
            marcarSelecionado(btnClientes);
        });

        btnFuncionarios.setOnAction(e -> {
            stackPane.getChildren().setAll(funcionarios);
            marcarSelecionado(btnFuncionarios);
        });

        btnLogs.setOnAction(e -> {
            stackPane.getChildren().setAll(logs);
            marcarSelecionado(btnLogs);
        });

        hBoxSair.setOnMouseReleased(mouseEvent -> sair(mouseEvent));

        if (cargo.equals("gerente")) {
            System.out.println("Logado como gerente");
        } else if (cargo.equals("assistente")) {
            System.out.println("Logado como assistente");
        } else {
            System.out.println("Errro! Cargo invalido...");
        }
    }

    private void configPanel() throws IOException {
        home = FXMLLoader.load(getClass().getResource("home.fxml"));
        contas = FXMLLoader.load(getClass().getResource("contas.fxml"));
        transacoes = FXMLLoader.load(getClass().getResource("transacoes.fxml"));
        clientes = FXMLLoader.load(getClass().getResource("clientes.fxml"));
        funcionarios = FXMLLoader.load(getClass().getResource("funcionarios.fxml"));
        logs = FXMLLoader.load(getClass().getResource("extratos.fxml"));

        stackPane.getChildren().setAll(home);
    }

    private void sair(MouseEvent event) {
        try {
            UserControl.setUser(null);
            ConexaoDB.logarUsuario("login_user", "login_user");

            Stage stageAtual = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stageAtual.close();

            Parent root = FXMLLoader.load(getClass().getResource("/com/projetodb/projetobancojava/login.fxml"));
            Stage loginStage = new Stage();

            Image icone = new Image(getClass().getResourceAsStream("/images/bankIcon.png"));
            loginStage.getIcons().add(icone);
            loginStage.setTitle("Gerenciamento de Banco");
            loginStage.setResizable(false);

            loginStage.setScene(new Scene(root));
            loginStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void marcarSelecionado(Button btn) {

        btnHome.getStyleClass().remove("selected");
        btnContas.getStyleClass().remove("selected");
        btnFuncionarios.getStyleClass().remove("selected");
        btnClientes.getStyleClass().remove("selected");
        btnTransacoes.getStyleClass().remove("selected");
        btnLogs.getStyleClass().remove("selected");

        if (!btn.getStyleClass().contains("selected")) {
            btn.getStyleClass().add("selected");
        }
    }

}
