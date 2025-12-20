package com.projetodb.projetobancojava;

import com.projetodb.projetobancojava.dao.ContaDAO;
import com.projetodb.projetobancojava.dao.ContaLogsDAO;
import com.projetodb.projetobancojava.model.Conta;
import com.projetodb.projetobancojava.model.ContaLogs;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ExtratosController {

    @FXML
    private Button btnBuscar, btnLimpar, btnVoltar;

    @FXML
    private TableColumn<ContaLogs, Integer> contaId;

    @FXML
    private TableColumn<ContaLogs, String> dataMudanca;

    @FXML
    private DatePicker dtpDataFinal;

    @FXML
    private DatePicker dtpDataInicio;

    @FXML
    private ImageView imgEditar1;

    @FXML
    private TableColumn<ContaLogs, Double> saldoAntigo;

    @FXML
    private TableColumn<ContaLogs, Double> saldoNovo;

    @FXML
    private TableView<ContaLogs> tblExtrato;

    @FXML
    private TextField txtPesquisar, txtContaId;

    private final ContaLogsDAO contaLogsDAO = new ContaLogsDAO();
    private ArrayList<ContaLogs> listaContasLogs = new ArrayList<>();
    private ObservableList<ContaLogs> listaContasLogsObs;
    private FilteredList<ContaLogs> filtroContasLogs;

    private final ContaDAO contaDAO = new ContaDAO();
    private ArrayList<Conta> listaContas = new ArrayList<>();
    private ArrayList<Integer> listaContasId = new ArrayList<>();
    private ObservableList<Integer> listaContasObs;

    @FXML
    protected void initialize() throws SQLException {
        atualizaTabela();
        configValues();

        txtPesquisar.textProperty().addListener((obs, oldValue, newValue) -> {
            filtroContasLogs.setPredicate(contaLogs -> {
                if (newValue == null || newValue.isBlank()) {
                    return true;
                }

                String filtroLower = newValue.toLowerCase();

                if (String.valueOf(contaLogs.getId()).contains(filtroLower)) return true;
                if (String.valueOf(contaLogs.getTempoFormatado()).contains(filtroLower)) return true;

                return false;
            });
        });

    }

    private void configValues() {
        btnVoltar.setDisable(true);

        contaId.setCellValueFactory(new PropertyValueFactory<>("contaId"));
        saldoAntigo.setCellValueFactory(new PropertyValueFactory<>("saldoAntigo"));
        saldoNovo.setCellValueFactory(new PropertyValueFactory<>("saldoNovo"));
        dataMudanca.setCellValueFactory(new PropertyValueFactory<>("tempoFormatado"));

    }

    private void atualizaTabela() throws SQLException {
        listaContasLogs = contaLogsDAO.retornaListaContaLogs();
        listaContasLogsObs = FXCollections.observableArrayList(listaContasLogs);
        filtroContasLogs = new FilteredList<>(listaContasLogsObs, c -> true);

        SortedList<ContaLogs> sorted = new SortedList<>(filtroContasLogs);
        sorted.comparatorProperty().bind(tblExtrato.comparatorProperty());

        tblExtrato.setItems(sorted);

        listaContas = contaDAO.retornaListaContas();
        listaContasId.clear();
        for (Conta c : listaContas) {
            listaContasId.add(c.getId());
        }
        listaContasObs = FXCollections.observableArrayList(listaContasId);
    }

    private void atualizarTabelaExtratos() throws SQLException {

        Integer clienteIdExtrato = Integer.parseInt(txtContaId.getText());
        LocalDate dataInicioExtrato = dtpDataInicio.getValue();
        LocalDate dataFinalExtrato = dtpDataFinal.getValue();

        listaContasLogs = contaLogsDAO.retornaListaExtrato(clienteIdExtrato, dataInicioExtrato, dataFinalExtrato);
        listaContasLogsObs = FXCollections.observableArrayList(listaContasLogs);
        filtroContasLogs = new FilteredList<>(listaContasLogsObs, c -> true);

        SortedList<ContaLogs> sorted = new SortedList<>(filtroContasLogs);
        sorted.comparatorProperty().bind(tblExtrato.comparatorProperty());

        tblExtrato.setItems(sorted);
    }

    @FXML
    void buscarClick(ActionEvent event) {
        if (!checarCampos()) {
            ViewPopups.mostrarPopupErro("Preencha todos os campos corretamente.");
            return;
        }

        try {
            atualizarTabelaExtratos();
            btnVoltar.setDisable(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void voltarClick(ActionEvent event) throws SQLException {
        atualizaTabela();
        btnVoltar.setDisable(true);
    }

    @FXML
    void limparClick(ActionEvent event) {
        limparErros();
        limparCampos();
    }

    private boolean checarCampos() {
        boolean v = true;

        limparErros();

        if (txtContaId.getText().isEmpty()) { v = false; setErro(txtContaId);}
        if (dtpDataInicio.getValue() == null) { v= false; setErro(dtpDataInicio);}
        if (dtpDataFinal.getValue() == null) { v= false; setErro(dtpDataFinal);}

        return v;
    }

    private void setErro(Node node) {
        node.setStyle("-fx-border-color: #ff4d4d; -fx-border-width: 2;");
    }

    private void limparCampos() {
        txtContaId.clear();
        dtpDataInicio.setValue(null);
        dtpDataFinal.setValue(null);
    }

    private void limparErros() {
        txtContaId.setStyle("");
        dtpDataFinal.setStyle("");
        dtpDataInicio.setStyle("");
    }

    @FXML
    protected void btnBuscarClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("buscarConta.fxml"));
        Parent root = loader.load();

        BuscarContaController controller = loader.getController();

        Stage stage = new Stage();
        stage.setTitle("Selecionar conta");

        Image icone = new Image(getClass().getResourceAsStream("/images/helpIcon.png"));
        stage.getIcons().add(icone);

        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(txtContaId.getScene().getWindow());
        stage.showAndWait();

        int cId = controller.getSelectedId();

        if (cId != -1) {
            txtContaId.setText(String.valueOf(cId));
        }
    }

}
