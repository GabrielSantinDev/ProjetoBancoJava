package com.projetodb.projetobancojava;

import com.projetodb.projetobancojava.control.UserControl;
import com.projetodb.projetobancojava.model.Funcionario;
import com.projetodb.projetobancojava.util.FormatarCampos;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HomeController {

    private Funcionario user;

    @FXML
    private Label lblNome, lblEmail, lblTelefone, lblCargo, lblCpf, lblSalario, lblDataNascimento;

    @FXML
    private Label lblUsername;

    @FXML
    private void initialize() {
        user = UserControl.getUser();

        configLabels();
        lblUsername.setText(lblUsername.getText() + " " + user.getNome());
    }

    private void configLabels() {
        lblNome.setText(user.getNome());
        lblEmail.setText(user.getEmail());
        lblTelefone.setText(FormatarCampos.formatarTelefone(user.getTelefone()));
        lblCargo.setText(user.getCargo());
        lblCpf.setText(FormatarCampos.formatarCPF(user.getCpf()));
        lblDataNascimento.setText(FormatarCampos.formatarData(user.getDataNascimento()));
        lblSalario.setText("R$ " + user.getSalario());
    }

}
