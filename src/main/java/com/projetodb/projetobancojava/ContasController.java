package com.projetodb.projetobancojava;

import com.projetodb.projetobancojava.dao.ClienteDAO;
import com.projetodb.projetobancojava.dao.ContaDAO;
import com.projetodb.projetobancojava.model.Cliente;
import com.projetodb.projetobancojava.model.Conta;
import com.projetodb.projetobancojava.util.RelatorioUtil;
import com.projetodb.projetobancojava.view.ViewPopups;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.sql.SQLException;
import java.util.ArrayList;

public class ContasController {

    private final ContaDAO contaDAO = new ContaDAO();
    private ArrayList<Conta> listaContas = new ArrayList<>();
    private ObservableList<Conta> listaContasObs;
    private FilteredList<Conta> filtroContas;

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private ArrayList<Cliente> listaClientes = new ArrayList<>();
    private ArrayList<Integer> listaClientesId = new ArrayList<>();
    private ObservableList<Integer> listaClientesObs;

    @FXML
    private AnchorPane btnImprimir;

    @FXML
    private TableColumn<Conta, Integer> cliente_id;

    @FXML
    private TableColumn<Conta, Integer> id;

    @FXML
    private TableColumn<Conta, String> numero;

    @FXML
    private TableColumn<Conta, Double> saldo;

    @FXML
    private TableColumn<Conta, String> senha;

    @FXML
    private TableView<Conta> tblContas;

    @FXML
    private TableColumn<Conta, String> tipo;

    @FXML
    private TextField txtPesquisar, txtNumero, txtSenha, txtSaldo;

    @FXML
    private ImageView imgEditar;

    @FXML
    private Button btnCriar, btnExcluir, btnEditar, btnLimpar;

    @FXML
    private ComboBox cbxTipo, cbxClienteId;

    private boolean modoEdicao = false;
    private int editarId = -1;

    @FXML
    private void initialize() throws SQLException {
        atualizaTabela();
        configValues();

        txtPesquisar.textProperty().addListener((obs, oldValue, newValue) -> {
            filtroContas.setPredicate(conta -> {
                if (newValue == null || newValue.isBlank()) {
                    return true;
                }

                String filtroLower = newValue.toLowerCase();

                if (String.valueOf(conta.getId()).contains(filtroLower)) return true;
                if (conta.getNumero().contains(filtroLower)) return true;
                if (conta.getTipo().toLowerCase().contains(filtroLower)) return true;
                if (String.valueOf(conta.getClienteId()).contains(filtroLower)) return true;

                return false;
            });
        });
    }


    private void configValues() {

        cbxTipo.setItems(FXCollections.observableArrayList("Poupança", "Corrente"));
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        numero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        senha.setCellValueFactory(new PropertyValueFactory<>("senha"));
        cliente_id.setCellValueFactory(new PropertyValueFactory<>("clienteId"));
        saldo.setCellValueFactory(new PropertyValueFactory<>("saldo"));
        tipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        cbxClienteId.setItems(listaClientesObs);
        cbxClienteId.setVisibleRowCount(4);
    }

    private void atualizaTabela() throws SQLException {
        listaContas = contaDAO.retornaListaContas();
        listaContasObs = FXCollections.observableArrayList(listaContas);
        filtroContas = new FilteredList<>(listaContasObs, c -> true);

        SortedList<Conta> sorted = new SortedList<>(filtroContas);
        sorted.comparatorProperty().bind(tblContas.comparatorProperty());

        tblContas.setItems(sorted);

        listaClientes = clienteDAO.retornaListaClientes();
        listaClientesId.clear();

        for (Cliente c : listaClientes) {
            listaClientesId.add(c.getId());
        }
        listaClientesObs = FXCollections.observableArrayList(listaClientesId);
    }

    @FXML
    protected void criarClick(ActionEvent actionEvent) {

        if (!checarCampos()) {
            ViewPopups.mostrarPopupErro("Preencha todos os campos corretamente.");
            return;
        }

        try {
            if (!modoEdicao) {
                contaDAO.addConta(retornaConta());
            }
            else {
                contaDAO.editarConta(editarId, retornaConta());

                modoEdicao = false;
                editarId = -1;

                btnEditar.setText("EDITAR");
                btnLimpar.setDisable(false);
                btnExcluir.setDisable(false);
                imgEditar.setImage(new Image(getClass().getResourceAsStream("/images/editIcon.png")));
            }

            atualizaTabela();
            limparErros();
            limparCampos();

        } catch (SQLException e) {
            System.out.println("Erro de SQL -> " + e);
        }
    }

    @FXML
    protected void limparClick(javafx.event.ActionEvent actionEvent) {
        limparCampos();
    }

    @FXML
    protected void editarClick(javafx.event.ActionEvent actionEvent) {
        if (!modoEdicao) {

            int selected = tblContas.getSelectionModel().getSelectedIndex();

            if (selected != -1 && selected < listaContas.size()) {
                modoEdicao = true;
                preencherCampos(listaContas.get(selected));
                btnEditar.setText("CANCEL");
                btnLimpar.setDisable(true);
                btnExcluir.setDisable(true);
                editarId = listaContas.get(selected).getId();
                imgEditar.setImage(new Image(getClass().getResourceAsStream("/images/cancelIcon.png")));

            } else {
                ViewPopups.mostrarPopupErro("Escolha um item para editar.");
            }

        } else {
            modoEdicao = false;
            btnEditar.setText("EDITAR");
            btnLimpar.setDisable(false);
            btnExcluir.setDisable(false);
            editarId = -1;
            imgEditar.setImage(new Image(getClass().getResourceAsStream("/images/editIcon.png")));
            limparCampos();
        }
    }

    @FXML
    protected void excluirClick(javafx.event.ActionEvent actionEvent) {

        int selected = tblContas.getSelectionModel().getSelectedIndex();

        if (selected != -1 && selected < listaContas.size()) {
            Conta c = listaContas.get(selected);

            try {
                contaDAO.deletarConta(c.getId());
                atualizaTabela();
            } catch (SQLException e) {
                System.out.println("Erro de SQL -> " + e);
            }

        } else {
             ViewPopups.mostrarPopupErro("Escolha um item para excluir.");
        }
    }

    private boolean checarCampos() {
        boolean valido = true;
        limparErros();

        if (txtNumero.getText().isEmpty()) {valido = false; setErro(txtNumero);}
        if (cbxClienteId.getEditor().getText().isEmpty()) {valido = false; setErro(cbxClienteId);}
        if (txtSaldo.getText().isEmpty()) {valido = false; setErro(txtSaldo);}
        if (txtSenha.getText().isEmpty()) {valido = false; setErro(txtSenha);}
        if (cbxTipo.getSelectionModel().isEmpty()) {valido = false; setErro(cbxTipo);}

        return valido;
    }

    private void preencherCampos(Conta c) {
        cbxClienteId.getSelectionModel().select(c.getClienteId());
        cbxClienteId.requestFocus();
        txtNumero.setText(c.getNumero());
        txtSaldo.setText(String.valueOf(c.getSaldo()));
        txtSenha.setText(c.getSenha());

        if (c.getTipo().equals("corrente")) {
            cbxTipo.getSelectionModel().select(1);
        } else {
            cbxTipo.getSelectionModel().select(0);
        }
    }

    private void setErro(Node node) {
        node.setStyle("-fx-border-color: #ff4d4d; -fx-border-width: 2;");
    }

    private void limparCampos() {
        txtNumero.setText("");
        cbxClienteId.getSelectionModel().clearSelection();
        txtSaldo.setText("");
        txtSenha.setText("");
        cbxClienteId.requestFocus();
        cbxTipo.getSelectionModel().selectFirst();
    }

    private void limparErros() {
        txtNumero.setStyle("");
        cbxClienteId.setStyle("");
        txtSaldo.setStyle("");
        txtSenha.setStyle("");
        cbxTipo.setStyle("");
    }

    private Conta retornaConta() {
        Conta c = new Conta();

        c.setNumero(txtNumero.getText());
        c.setClienteId(Integer.parseInt(cbxClienteId.getSelectionModel().getSelectedItem().toString()));
        c.setSaldo(Double.parseDouble(txtSaldo.getText()));
        c.setSenha(txtSenha.getText());

        if (cbxTipo.getSelectionModel().getSelectedItem().equals("Corrente")) {
            c.setTipo("corrente");
        } else {
            c.setTipo("poupanca");
        }

        return c;
    }

    @FXML
    protected void btnImprimirClick(MouseEvent mouseEvent) {
        RelatorioUtil.gerarRelatorio("contas");
    }

}
