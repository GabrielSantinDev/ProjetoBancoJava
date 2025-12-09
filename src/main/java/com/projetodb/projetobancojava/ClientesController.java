package com.projetodb.projetobancojava;

import com.projetodb.projetobancojava.dao.ClienteDAO;
import com.projetodb.projetobancojava.model.Cliente;
import com.projetodb.projetobancojava.model.Conta;
import com.projetodb.projetobancojava.util.FormatarCampos;
import com.projetodb.projetobancojava.view.ViewPopups;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ClientesController {

    @FXML
    private Button btnCriar, btnEditar, btnExcluir, btnLimpar;

    @FXML
    private TableColumn<Cliente, String> cpf;

    @FXML
    private TableColumn<Cliente, LocalDate> dataNascimento;

    @FXML
    private DatePicker dtpDataNascimento;

    @FXML
    private TableColumn<Cliente, String> email;

    @FXML
    private TableColumn<Cliente, Integer> id;

    @FXML
    private ImageView imgEditar;

    @FXML
    private ImageView imgEditar1;

    @FXML
    private TableColumn<Cliente, String> nome;

    @FXML
    private TableView<Cliente> tblClientes;

    @FXML
    private TableColumn<Cliente, String> telefone;

    @FXML
    private TextField txtCpf;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtPesquisar;

    @FXML
    private TextField txtTelefone;

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private ArrayList<Cliente> listaClientes = new ArrayList<>();
    private ObservableList<Cliente> listaClientesObs;
    private FilteredList<Cliente> filtroClientes;

    private boolean modoEdicao = false;
    private int editarId = -1;

    @FXML
    private void initialize() throws SQLException {
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
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        telefone.setCellValueFactory(new PropertyValueFactory<>("telefoneFormatado"));
        dataNascimento.setCellValueFactory(new PropertyValueFactory<>("dataNascimentoFormatada"));
    }

    @FXML
    protected void criarClick(javafx.event.ActionEvent actionEvent) {
        if (!validarCampos()) {
            ViewPopups.mostrarPopupErro("Preencha todos os campos corretamente.");
            return;
        }

        try {
            Cliente cliente = retornaCliente();

            if (!modoEdicao) {
                clienteDAO.addCliente(cliente);

            } else {
                clienteDAO.editarCliente(editarId, cliente);

                modoEdicao = false;
                editarId = -1;
                btnEditar.setText("EDITAR");
                btnLimpar.setDisable(false);
                btnExcluir.setDisable(false);

                imgEditar.setImage(new Image(getClass().getResourceAsStream("/images/editIcon.png")));
            }

            atualizaTabela();
            limparCampos();
            limparErros();

        } catch (SQLException e) {
            System.out.println("Erro SQL -> " + e);
        }
    }

    @FXML
    protected void editarClick(javafx.event.ActionEvent actionEvent) {

        Cliente selecionado = tblClientes.getSelectionModel().getSelectedItem();

        if (!modoEdicao) {

            if (selecionado == null) {
                ViewPopups.mostrarPopupErro("Selecione um cliente para editar.");
                return;
            }

            modoEdicao = true;

            preencherCampos(selecionado);
            editarId = selecionado.getId();

            btnEditar.setText("CANCELAR");
            btnLimpar.setDisable(true);
            btnExcluir.setDisable(true);

            imgEditar.setImage(new Image(getClass().getResourceAsStream("/images/cancelIcon.png")));

        } else {

            modoEdicao = false;
            editarId = -1;

            limparCampos();

            btnEditar.setText("EDITAR");
            btnLimpar.setDisable(false);
            btnExcluir.setDisable(false);

            imgEditar.setImage(new Image(getClass().getResourceAsStream("/images/editIcon.png")));
        }
    }

    @FXML
    protected void excluirClick(javafx.event.ActionEvent actionEvent) {

        Cliente selecionado = tblClientes.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            ViewPopups.mostrarPopupErro("Selecione um cliente para excluir.");
            return;
        }

        try {
            clienteDAO.deletarCliente(selecionado.getId());
            atualizaTabela();

        } catch (SQLException e) {
            System.out.println("Erro SQL -> " + e);
        }
    }

    @FXML
    void limparClick(javafx.event.ActionEvent actionEvent) {
        limparCampos();
        limparErros();
    }

    private boolean validarCampos() {
        boolean v = true;

        if (txtNome.getText().isEmpty()) { setErro(txtNome); v = false; }
        if (txtCpf.getText().isEmpty()) { setErro(txtCpf); v = false; }
        if (txtEmail.getText().isEmpty()) { setErro(txtEmail); v = false; }
        if (txtTelefone.getText().isEmpty()) { setErro(txtTelefone); v = false; }
        if (dtpDataNascimento.getValue() == null) { setErro(dtpDataNascimento); v = false; }

        if (txtCpf.getLength() < 11) { setErro(txtCpf); v = false; }
        if (txtTelefone.getLength() < 11) { setErro(txtTelefone); v = false; }

        return v;
    }

    private void preencherCampos(Cliente c) {
        txtNome.setText(c.getNome());
        txtCpf.setText(c.getCpf());
        txtEmail.setText(c.getEmail());
        txtTelefone.setText(c.getTelefone());
        dtpDataNascimento.setValue(c.getDataNascimento());
    }

    private void limparCampos() {
        txtNome.clear();
        txtCpf.clear();
        txtEmail.clear();
        txtTelefone.clear();
        dtpDataNascimento.setValue(null);
        txtNome.requestFocus();
    }

    private void setErro(Node node) {
        node.setStyle("-fx-border-color: #ff4d4d; -fx-border-width: 2;");
    }

    private void limparErros() {
        txtNome.setStyle("");
        txtCpf.setStyle("");
        txtEmail.setStyle("");
        txtTelefone.setStyle("");
        dtpDataNascimento.setStyle("");
    }

    private Cliente retornaCliente() {
        Cliente c = new Cliente();

        c.setNome(txtNome.getText());
        c.setCpf(FormatarCampos.formatarParaNumeros(txtCpf.getText()));
        c.setEmail(txtEmail.getText());
        c.setTelefone(FormatarCampos.formatarParaNumeros(txtTelefone.getText()));
        c.setDataNascimento(dtpDataNascimento.getValue());

        return c;
    }
}
