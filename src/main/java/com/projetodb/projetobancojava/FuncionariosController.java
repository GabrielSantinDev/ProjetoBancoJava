package com.projetodb.projetobancojava;

import com.projetodb.projetobancojava.dao.FuncionarioDAO;
import com.projetodb.projetobancojava.model.Cliente;
import com.projetodb.projetobancojava.model.Funcionario;
import com.projetodb.projetobancojava.util.FormatarCampos;
import com.projetodb.projetobancojava.view.ViewPopups;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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

public class FuncionariosController {

    @FXML
    private Button btnCriar, btnEditar, btnExcluir, btnLimpar;

    @FXML
    private TableColumn<Funcionario, String> cargo;

    @FXML
    private ComboBox<String> cbxCargo;

    @FXML
    private TableColumn<Funcionario, String> cpf;

    @FXML
    private TableColumn<Funcionario, LocalDate> dataNascimento;

    @FXML
    private DatePicker dtpDataNascimento;

    @FXML
    private TableColumn<Funcionario, String> email;

    @FXML
    private TableColumn<Funcionario, Integer> id;

    @FXML
    private ImageView imgEditar;

    @FXML
    private ImageView imgEditar1;

    @FXML
    private TableColumn<Funcionario, String> nome;

    @FXML
    private TableColumn<Funcionario, Double> salario;

    @FXML
    private TableView<Funcionario> tblFuncionarios;

    @FXML
    private TableColumn<Funcionario, String> telefone;

    @FXML
    private TextField txtCpf;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtPesquisar;

    @FXML
    private TextField txtSalario;

    @FXML
    private TextField txtSenha;

    @FXML
    private TextField txtTelefone;

    private final FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
    private ArrayList<Funcionario> listaFuncionarios = new ArrayList<>();
    private ObservableList<Funcionario> listaFuncionariosObs;
    private FilteredList<Funcionario> filtroFuncionarios;

    private boolean modoEdicao = false;
    private int editarId = -1;

    @FXML
    protected void initialize() throws SQLException {
        atualizaTabela();
        configValues();

        txtPesquisar.textProperty().addListener((obs, oldValue, newValue) -> {
            filtroFuncionarios.setPredicate(funcionario -> {
                if (newValue == null || newValue.isBlank()) {
                    return true;
                }

                String filtroLower = newValue.toLowerCase();

                if (String.valueOf(funcionario.getId()).contains(filtroLower)) return true;
                if (funcionario.getNome().toLowerCase().contains(filtroLower)) return true;
                if (funcionario.getCpf().toLowerCase().contains(filtroLower)) return true;

                return false;
            });
        });
    }

    private void atualizaTabela() throws SQLException {
        listaFuncionarios = funcionarioDAO.retornaListaFuncionarios();
        listaFuncionariosObs = FXCollections.observableArrayList(listaFuncionarios);
        filtroFuncionarios = new FilteredList<>(listaFuncionariosObs, c -> true);

        SortedList<Funcionario> sorted = new SortedList<>(filtroFuncionarios);
        sorted.comparatorProperty().bind(tblFuncionarios.comparatorProperty());

        tblFuncionarios.setItems(sorted);
    }

    private void configValues() {
        cbxCargo.setItems(FXCollections.observableArrayList("Assistente", "Gerente"));
        tblFuncionarios.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        cpf.setCellValueFactory(new PropertyValueFactory<>("cpfFormatado"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        telefone.setCellValueFactory(new PropertyValueFactory<>("telefoneFormatado"));
        dataNascimento.setCellValueFactory(new PropertyValueFactory<>("dataNascimentoFormatada"));
        cargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        salario.setCellValueFactory(new PropertyValueFactory<>("salario"));
    }

    @FXML
    protected void criarClick(javafx.event.ActionEvent actionEvent) {

        if (!validarCampos()) {
            ViewPopups.mostrarPopupErro("Preencha todos os campos corretamente.");
            return;
        }

        try {
            Funcionario f = retornaFuncionario();

            if (!modoEdicao) {
                funcionarioDAO.addFuncionario(f);

            } else {
                funcionarioDAO.editarFuncionario(editarId, f);

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

        Funcionario selecionado = tblFuncionarios.getSelectionModel().getSelectedItem();

        if (!modoEdicao) {

            if (selecionado == null) {
                ViewPopups.mostrarPopupErro("Selecione um funcionário para editar.");
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

        Funcionario selecionado = tblFuncionarios.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            ViewPopups.mostrarPopupErro("Selecione um funcionário para excluir.");
            return;
        }

        try {
            funcionarioDAO.deletarFuncionario(selecionado.getId());
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
        if (txtSalario.getText().isEmpty()) { setErro(txtSalario); v = false; }
        if (cbxCargo.getValue() == null) { setErro(cbxCargo); v = false; }
        if (txtSenha.getText().isEmpty()) { setErro(txtSenha); v = false; }
        if (dtpDataNascimento.getValue() == null) { setErro(dtpDataNascimento); v = false; }

        if (txtCpf.getLength() < 11) { setErro(txtCpf); v = false; }
        if (txtTelefone.getLength() < 11) { setErro(txtTelefone); v = false; }

        return v;
    }

    private void preencherCampos(Funcionario f) {
        txtNome.setText(f.getNome());
        txtCpf.setText(FormatarCampos.formatarParaNumeros(f.getCpf()));
        txtEmail.setText(f.getEmail());
        txtTelefone.setText(FormatarCampos.formatarParaNumeros(f.getTelefone()));
        txtSalario.setText(String.valueOf(f.getSalario()));
        txtSenha.setText(f.getSenha());
        cbxCargo.setValue(f.getCargo());
        dtpDataNascimento.setValue(f.getDataNascimento());
    }

    private void limparCampos() {
        txtNome.clear();
        txtCpf.clear();
        txtEmail.clear();
        txtTelefone.clear();
        txtSalario.clear();
        txtSenha.clear();
        cbxCargo.getSelectionModel().selectFirst();
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
        txtSalario.setStyle("");
        txtSenha.setStyle("");
        cbxCargo.setStyle("");
        dtpDataNascimento.setStyle("");
    }

    private Funcionario retornaFuncionario() {
        Funcionario f = new Funcionario();

        f.setNome(txtNome.getText());
        f.setCpf(FormatarCampos.formatarParaNumeros(txtCpf.getText()));
        f.setEmail(txtEmail.getText());
        f.setTelefone(FormatarCampos.formatarParaNumeros(txtTelefone.getText()));
        f.setCargo(cbxCargo.getValue().toLowerCase());
        f.setSenha(txtSenha.getText());
        f.setSalario(Double.parseDouble(txtSalario.getText()));
        f.setDataNascimento(dtpDataNascimento.getValue());

        return f;
    }

}
