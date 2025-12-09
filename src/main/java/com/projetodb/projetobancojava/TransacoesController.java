package com.projetodb.projetobancojava;

import com.projetodb.projetobancojava.dao.ContaDAO;
import com.projetodb.projetobancojava.dao.TransacaoDAO;
import com.projetodb.projetobancojava.model.Conta;
import com.projetodb.projetobancojava.model.Transacao;
import com.projetodb.projetobancojava.view.ViewPopups;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class TransacoesController {

    @FXML
    private Button btnCriar, btnExcluir, btnLimpar;

    @FXML
    private ComboBox<Integer> cbxDestinoID;

    @FXML
    private ComboBox<Integer> cbxOrigemID;

    @FXML
    private ComboBox<String> cbxTipo;

    @FXML
    private TableColumn<Transacao, LocalTime> data;

    @FXML
    private TableColumn<Transacao, Integer> destinoId;

    @FXML
    private TableColumn<Transacao, Integer> id;

    @FXML
    private TableColumn<Transacao, Integer> origemId;

    @FXML
    private TableView<Transacao> tblTransacoes;

    @FXML
    private TableColumn<Transacao, String> tipo;

    @FXML
    private TextField txtPesquisar;

    @FXML
    private TextField txtValor;

    @FXML
    private TableColumn<Transacao, Double> valor;

    private final TransacaoDAO transacaoDAO = new TransacaoDAO();
    private ArrayList<Transacao> listaTransacoes = new ArrayList<>();
    private ObservableList<Transacao> listaTransacoesObs;
    private FilteredList<Transacao> filtroTransacoes;

    private final ContaDAO contaDAO = new ContaDAO();
    private ArrayList<Conta> listaContas = new ArrayList<>();
    private ArrayList<Integer> listaContasId = new ArrayList<>();
    private ObservableList<Integer> listaContasObs;

    @FXML
    public void initialize() throws SQLException {
        atualizaTabela();
        configValues();

        cbxTipo.setOnAction(event -> {
            String valor = cbxTipo.getValue();
            tipoChange(valor);
        });

        txtPesquisar.textProperty().addListener((obs, oldValue, newValue) -> {
            filtroTransacoes.setPredicate(transacao -> {
                if (newValue == null || newValue.isBlank()) {
                    return true;
                }

                String filtroLower = newValue.toLowerCase();

                if (String.valueOf(transacao.getId()).contains(filtroLower)) return true;
                if (String.valueOf(transacao.getContaOrigemId()).contains(filtroLower)) return true;
                if (String.valueOf(transacao.getContaDestinoId()).contains(filtroLower)) return true;
                if (transacao.getTipo().toLowerCase().contains(filtroLower)) return true;

                return false;
            });
        });
    }

    private void atualizaTabela() throws SQLException {
        listaTransacoes = transacaoDAO.retornaListaTransacao();
        listaTransacoesObs = FXCollections.observableArrayList(listaTransacoes);
        filtroTransacoes = new FilteredList<>(listaTransacoesObs, c -> true);

        SortedList<Transacao> sorted = new SortedList<>(filtroTransacoes);
        sorted.comparatorProperty().bind(tblTransacoes.comparatorProperty());

        tblTransacoes.setItems(sorted);

        listaContas = contaDAO.retornaListaContas();
        listaContasId.clear();
        for (Conta c : listaContas) {
            listaContasId.add(c.getId());
        }
        listaContasObs = FXCollections.observableArrayList(listaContasId);
    }

    private void configValues() {
        cbxDestinoID.setDisable(true);
        cbxOrigemID.setDisable(true);

        cbxTipo.setItems(FXCollections.observableArrayList("Saque", "Depósito", "Transferência"));
        cbxTipo.setVisibleRowCount(3);

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        tipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        origemId.setCellValueFactory(new PropertyValueFactory<>("contaOrigemFormatada"));
        destinoId.setCellValueFactory(new PropertyValueFactory<>("contaDestinoFormatada"));
        data.setCellValueFactory(new PropertyValueFactory<>("dataEnvioFormatada"));
        valor.setCellValueFactory(new PropertyValueFactory<>("valor"));


        cbxOrigemID.setItems(listaContasObs);
        cbxOrigemID.setVisibleRowCount(4);

        cbxDestinoID.setItems(listaContasObs);
        cbxDestinoID.setVisibleRowCount(4);
    }

    @FXML
    void criarClick(ActionEvent event) {

        if (checarCampos()) {
            try {
                transacaoDAO.addTransacao(retornaTransacao());
                atualizaTabela();

                limparErros();
                limparCampos();
            } catch (SQLException e) {
                System.out.println("Erro de SQL -> " + e);
            }
        } else {
            ViewPopups.mostrarPopupErro("Preencha todos os campos corretamente.");
        }
    }

    private boolean checarCampos() {
        boolean valido = true;
        limparErros();

        String tipo = cbxTipo.getSelectionModel().getSelectedItem();

        if (tipo == null) { valido = false; setErro(cbxTipo); }
        if (txtValor.getText().isEmpty()) { valido = false; setErro(txtValor);}

        String origemTxt = cbxOrigemID.getEditor().getText();
        String destinoTxt = cbxDestinoID.getEditor().getText();

        switch (tipo) {
            case "Saque":
                if (origemTxt.isEmpty()) { valido = false; setErro(cbxOrigemID); }
                break;

            case "Transferência":
                if (origemTxt.isEmpty()) { valido = false; setErro(cbxOrigemID); }
                if (destinoTxt.isEmpty()) { valido = false; setErro(cbxDestinoID); }
                break;

            case "Depósito":
                if (destinoTxt.isEmpty()) { valido = false; setErro(cbxDestinoID); }
                break;
        }

        return valido;
    }

    @FXML
    void excluirClick(ActionEvent event) {
        int selected = tblTransacoes.getSelectionModel().getSelectedIndex();

        if (selected != -1 && selected < listaTransacoes.size()) {
            Transacao t = listaTransacoes.get(selected);

            try {
                transacaoDAO.deletarTransacao(t.getId());
                atualizaTabela();
            } catch (SQLException e) {
                System.out.println("Erro de SQL -> " + e);
            }

        } else {
            ViewPopups.mostrarPopupErro("Escolha um item para excluir.");
        }
    }

    @FXML
    void limparClick(ActionEvent event) {
        limparCampos();
    }

    private void setErro(Node node) {
        node.setStyle("-fx-border-color: #ff4d4d; -fx-border-width: 2;");
    }

    private Transacao retornaTransacao() {
        Transacao t = new Transacao();

        String tipo = cbxTipo.getSelectionModel().getSelectedItem();

        switch (tipo) {
            case "Saque":
                t.setTipo("saque");
                t.setContaOrigemId(Integer.parseInt(cbxOrigemID.getEditor().getText()));
                break;

            case "Depósito":
                t.setTipo("deposito");
                t.setContaDestinoId(Integer.parseInt(cbxDestinoID.getEditor().getText()));
                break;

            case "Transferência":
                t.setTipo("transferencia");
                t.setContaDestinoId(Integer.parseInt(cbxDestinoID.getEditor().getText()));
                t.setContaOrigemId(Integer.parseInt(cbxOrigemID.getEditor().getText()));
        }

        t.setValor(Double.parseDouble(txtValor.getText()));
        t.setDataEnvio(LocalDate.now());

        return t;
    }

    private void limparCampos() {
        txtValor.clear();
        cbxTipo.getSelectionModel().selectFirst();
        cbxOrigemID.getEditor().clear();
        cbxDestinoID.getEditor().clear();
        txtValor.requestFocus();
    }

    private void limparErros() {
        txtValor.setStyle("");
        cbxTipo.setStyle("");
        cbxOrigemID.setStyle("");
        cbxDestinoID.setStyle("");
    }

    private void tipoChange(String v) {
        switch (v) {
            case "Saque":
                cbxDestinoID.setDisable(true);
                cbxOrigemID.setDisable(false);
                break;

            case "Depósito":
                cbxOrigemID.setDisable(true);
                cbxDestinoID.setDisable(false);
                break;

            case "Transferência":
                cbxOrigemID.setDisable(false);
                cbxDestinoID.setDisable(false);
        }
    }

}
