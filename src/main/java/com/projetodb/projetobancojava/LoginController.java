package com.projetodb.projetobancojava;

import com.projetodb.projetobancojava.control.UserControl;
import com.projetodb.projetobancojava.dao.FuncionarioDAO;
import com.projetodb.projetobancojava.database.ConexaoDB;
import com.projetodb.projetobancojava.model.Funcionario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtSenhaVisivel;
    @FXML
    private PasswordField psfSenha;
    @FXML
    private CheckBox cbxMostrarSenha;
    @FXML
    private Label lblError;
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button btnLogin;

    @FXML
    private void initialize() {
        UserControl.setUser(null);

        txtEmail.requestFocus();
        btnLogin.setFocusTraversable(false);

        txtSenhaVisivel.textProperty().bindBidirectional(psfSenha.textProperty());
        txtSenhaVisivel.setVisible(false);
        txtSenhaVisivel.setManaged(false);
        lblError.setVisible(false);

        // --- DEBUG - ENTRAR COMO ADMIN ---
        anchorPane.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.F1) {
                try {
                    UserControl.setUser(new FuncionarioDAO().retornarFuncionario(1));
                    ConexaoDB.logarUsuario("gerente", "gerente123");
                    mudarCenaPrincipal();
                } catch (SQLException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        // --------------------------

        cbxMostrarSenha.setOnAction(event -> {
            if (cbxMostrarSenha.isSelected()) {
                txtSenhaVisivel.setVisible(true);
                txtSenhaVisivel.setManaged(true);
                psfSenha.setVisible(false);
                psfSenha.setManaged(false);
            } else {
                txtSenhaVisivel.setVisible(false);
                txtSenhaVisivel.setManaged(false);
                psfSenha.setVisible(true);
                psfSenha.setManaged(true);
            }
        });

    }

    @FXML
    protected void onLoginButtonClick(ActionEvent event) throws IOException, SQLException {

        FuncionarioDAO dao = new FuncionarioDAO();

        String email = txtEmail.getText();
        String senha = psfSenha.getText();

        int idUser = dao.checarLogin(email, senha);

        if (idUser > -1) {
            Funcionario f = dao.retornarFuncionario(idUser);
            UserControl.setUser(f);

            if (f.getCargo().equals("assistente")) {
                ConexaoDB.logarUsuario("assistente", "assistente123");
            } else if (f.getCargo().equals("gerente")) {
                ConexaoDB.logarUsuario("gerente", "gerente123");
            } else {
                ConexaoDB.logarUsuario("login_user", "login_user");
            }

            mudarCenaPrincipal();
        } else {
            txtEmail.setStyle("-fx-border-color: linear-gradient(to right, #bf082a, #fd1a12); -fx-border-width: 2;");
            psfSenha.setStyle("-fx-border-color: linear-gradient(to right, #bf082a, #fd1a12); -fx-border-width: 2;");
            txtSenhaVisivel.setStyle("-fx-border-color: linear-gradient(to right, #bf082a, #fd1a12); -fx-border-width: 2;");
            lblError.setVisible(true);
        }

    }

    @FXML
    private void mudarCenaPrincipal() throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("principal.fxml"));

        Stage stage = (Stage) anchorPane.getScene().getWindow();
        Scene scene = new Scene(root);

        Image icone = new Image(getClass().getResourceAsStream("/images/bankIcon.png"));
        stage.getIcons().add(icone);
        stage.setTitle("Gerenciamento de Banco");

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}