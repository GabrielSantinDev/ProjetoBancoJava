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
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class TransacoesController {

    @FXML
    private AnchorPane btnBuscarOrigem, btnBuscarDestino;

    @FXML
    private Button btnCriar, btnExcluir, btnLimpar;

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
    private TextField txtPesquisar, txtContaOrigem, txtContaDestino;

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
        txtContaOrigem.setDisable(true);
        txtContaDestino.setDisable(true);

        btnBuscarOrigem.setDisable(true);
        btnBuscarDestino.setDisable(true);

        cbxTipo.setItems(FXCollections.observableArrayList("Saque", "Depósito", "Transferência"));
        cbxTipo.setVisibleRowCount(3);

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        tipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        origemId.setCellValueFactory(new PropertyValueFactory<>("contaOrigemFormatada"));
        destinoId.setCellValueFactory(new PropertyValueFactory<>("contaDestinoFormatada"));
        data.setCellValueFactory(new PropertyValueFactory<>("dataEnvioFormatada"));
        valor.setCellValueFactory(new PropertyValueFactory<>("valor"));
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

        String origemTxt = txtContaOrigem.getText();
        String destinoTxt = txtContaDestino.getText();

        switch (tipo) {
            case "Saque":
                if (origemTxt.isEmpty()) { valido = false; setErro(txtContaOrigem); }
                break;

            case "Transferência":
                if (origemTxt.isEmpty()) { valido = false; setErro(txtContaOrigem); }
                if (destinoTxt.isEmpty()) { valido = false; setErro(txtContaDestino); }
                break;

            case "Depósito":
                if (destinoTxt.isEmpty()) { valido = false; setErro(txtContaDestino); }
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
                t.setContaOrigemId(Integer.parseInt(txtContaOrigem.getText()));
                break;

            case "Depósito":
                t.setTipo("deposito");
                t.setContaDestinoId(Integer.parseInt(txtContaDestino.getText()));
                break;

            case "Transferência":
                t.setTipo("transferencia");
                t.setContaDestinoId(Integer.parseInt(txtContaDestino.getText()));
                t.setContaOrigemId(Integer.parseInt(txtContaOrigem.getText()));
        }

        t.setValor(Double.parseDouble(txtValor.getText()));
        t.setDataEnvio(LocalDate.now());

        return t;
    }

    private void limparCampos() {
        txtValor.clear();
        cbxTipo.getSelectionModel().selectFirst();
        txtContaOrigem.clear();
        txtContaDestino.clear();
        txtValor.requestFocus();
    }

    private void limparErros() {
        txtValor.setStyle("");
        cbxTipo.setStyle("");
        txtContaOrigem.setStyle("");
        txtContaDestino.setStyle("");
    }

    private void tipoChange(String v) {
        switch (v) {
            case "Saque":
                txtContaDestino.setDisable(true);
                txtContaOrigem.setDisable(false);

                btnBuscarDestino.setDisable(true);
                btnBuscarOrigem.setDisable(false);
                break;

            case "Depósito":
                txtContaOrigem.setDisable(true);
                txtContaDestino.setDisable(false);

                btnBuscarDestino.setDisable(false);
                btnBuscarOrigem.setDisable(true);
                break;

            case "Transferência":
                txtContaOrigem.setDisable(false);
                txtContaDestino.setDisable(false);

                btnBuscarOrigem.setDisable(false);
                btnBuscarDestino.setDisable(false);
        }
    }

    @FXML
    protected void btnBuscarOrigemClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("buscarConta.fxml"));
        Parent root = loader.load();

        BuscarContaController controller = loader.getController();

        Stage stage = new Stage();
        stage.setTitle("Selecionar conta de origem");

        Image icone = new Image(getClass().getResourceAsStream("/images/helpIcon.png"));
        stage.getIcons().add(icone);

        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(txtContaOrigem.getScene().getWindow());
        stage.showAndWait();

        int cId = controller.getSelectedId();

        if (cId != -1) {
            txtContaOrigem.setText(String.valueOf(cId));
        }
    }

    @FXML
    protected void btnBuscarDestinoClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("buscarConta.fxml"));
        Parent root = loader.load();

        BuscarContaController controller = loader.getController();

        Stage stage = new Stage();

        stage.setTitle("Selecionar conta de destino");
        Image icone = new Image(getClass().getResourceAsStream("/images/helpIcon.png"));
        stage.getIcons().add(icone);

        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(txtContaDestino.getScene().getWindow());
        stage.showAndWait();

        int cId = controller.getSelectedId();

        if (cId != -1) {
            txtContaDestino.setText(String.valueOf(cId));
        }
    }

}
