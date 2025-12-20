package com.projetodb.projetobancojava;

import com.projetodb.projetobancojava.dao.ClienteDAO;
import com.projetodb.projetobancojava.model.Cliente;
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

public class BuscarClienteController {

    @FXML
    private Button btnSelecionar;

    @FXML
    private Button btnVoltar;

    @FXML
    private TableColumn<Cliente, String> cpf;

    @FXML
    private TableColumn<Cliente, Integer> id;

    @FXML
    private ImageView imgEditar1;

    @FXML
    private TableColumn<Cliente, String> nome;

    @FXML
    private TableView<Cliente> tblClientes;

    @FXML
    private TextField txtPesquisar;

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private ArrayList<Cliente> listaClientes = new ArrayList<>();
    private ObservableList<Cliente> listaClientesObs;
    private FilteredList<Cliente> filtroClientes;

    private int selectedId = -1;

    @FXML
    void initialize() throws SQLException {
        atualizaTabela();
        configValues();

        txtPesquisar.textProperty().addListener((obs, oldValue, newValue) -> {
            filtroClientes.setPredicate(cliente -> {
                if (newValue == null || newValue.isBlank()) {
                    return true;
                }

                String filtroLower = newValue.toLowerCase();

                if (String.valueOf(cliente.getId()).contains(filtroLower)) return true;
                if (cliente.getNome().toLowerCase().contains(filtroLower)) return true;
                if (cliente.getCpf().toLowerCase().contains(filtroLower)) return true;

                return false;
            });
        });
    }

    private void atualizaTabela() throws SQLException {
        listaClientes = clienteDAO.retornaListaClientes();
        listaClientesObs = FXCollections.observableArrayList(listaClientes);
        filtroClientes = new FilteredList<>(listaClientesObs, c -> true);

        SortedList<Cliente> sorted = new SortedList<>(filtroClientes);
        sorted.comparatorProperty().bind(tblClientes.comparatorProperty());

        tblClientes.setItems(sorted);
    }

    private void configValues() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        cpf.setCellValueFactory(new PropertyValueFactory<>("cpfFormatado"));
    }

    @FXML
    void selecionarClick(ActionEvent event) {

        Cliente selecionado = tblClientes.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            ViewPopups.mostrarPopupErro("Selecione um cliente.");
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
