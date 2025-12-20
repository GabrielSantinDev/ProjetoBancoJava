package com.projetodb.projetobancojava;

import com.projetodb.projetobancojava.dao.ContaDAO;
import com.projetodb.projetobancojava.model.Conta;
import com.projetodb.projetobancojava.view.ViewPopups;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;

public class BuscarContaController {

    @FXML
    private Button btnSelecionar;

    @FXML
    private Button btnVoltar;

    @FXML
    private TableColumn<Conta, Integer> id;

    @FXML
    private TableColumn<Conta, String> numero;

    @FXML
    private TableView<Conta> tblContas;

    @FXML
    private TextField txtPesquisar;

    private final ContaDAO contaDAO = new ContaDAO();
    private ArrayList<Conta> listaContas = new ArrayList<Conta>();
    private ObservableList<Conta> listaContasObs;
    private FilteredList<Conta> filtroContas;

    private int selectedId = -1;

    @FXML
    void initialize() throws SQLException {
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

                return false;
            });
        });
    }

    private void atualizaTabela() throws SQLException {
        listaContas = contaDAO.retornaListaContas();
        listaContasObs = FXCollections.observableArrayList(listaContas);
        filtroContas = new FilteredList<>(listaContasObs, c -> true);

        SortedList<Conta> sorted = new SortedList<>(filtroContas);
        sorted.comparatorProperty().bind(tblContas.comparatorProperty());

        tblContas.setItems(sorted);
    }

    private void configValues() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        numero.setCellValueFactory(new PropertyValueFactory<>("numero"));
    }

    @FXML
    void selecionarClick(ActionEvent event) {

        Conta selecionado = tblContas.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            ViewPopups.mostrarPopupErro("Selecione uma conta.");
            return;
        } else {
            selectedId = selecionado.getId();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }

    }

    @FXML
    void voltarClick(ActionEvent event) {
        selectedId = -1;
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

    }

    public int getSelectedId() {
        return selectedId;
    }

}
