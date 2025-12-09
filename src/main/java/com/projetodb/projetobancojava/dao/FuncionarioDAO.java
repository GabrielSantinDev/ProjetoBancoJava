package com.projetodb.projetobancojava.dao;

import com.projetodb.projetobancojava.database.ConexaoDB;
import com.projetodb.projetobancojava.model.Funcionario;
import com.projetodb.projetobancojava.view.ViewPopups;

import java.sql.*;
import java.util.ArrayList;

public class FuncionarioDAO {

    private Connection con;

    public int checarLogin(String email, String senha) throws SQLException {
        int id = -1;
        con = ConexaoDB.getConexao();
        String sql = "SELECT id FROM tb_funcionario WHERE email=? AND senha=?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, senha);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                id = rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println("ERRO, nao encontrei a tabela -> " + e);
        } finally{
            con.close();
            System.out.println("Encerrando conexão");
        }

        return id;
    }

    public Funcionario retornarFuncionario(int id) throws SQLException {
        Funcionario f = new Funcionario();
        f.setId(id);

        con = ConexaoDB.getConexao();
        String sql = "SELECT nome, cpf, email, senha, telefone, data_nascimento, salario, cargo FROM tb_funcionario WHERE id=?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                f.setNome(rs.getString("nome"));
                f.setCpf(rs.getString("cpf"));
                f.setEmail(rs.getString("email"));
                f.setSenha(rs.getString("senha"));
                f.setTelefone(rs.getString("telefone"));
                f.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
                f.setSalario(rs.getDouble("salario"));
                f.setCargo(rs.getString("cargo"));
            }
        } catch (SQLException e) {
            System.out.println("ERRO, nao encontrei a tabela -> " + e);
        } finally {
            con.close();
            System.out.println("Encerrando conexão");
        }

        return f;
    }

    public ArrayList<Funcionario> retornaListaFuncionarios() throws SQLException {

        con = ConexaoDB.getConexao();
        String sql = "SELECT * FROM tb_funcionario";

        ArrayList<Funcionario> listaFuncionarios = new ArrayList<>();

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Funcionario f = new Funcionario();

                f.setId(rs.getInt("id"));
                f.setNome(rs.getString("nome"));
                f.setCpf(rs.getString("cpf"));
                f.setEmail(rs.getString("email"));
                f.setSenha(rs.getString("senha"));
                f.setTelefone(rs.getString("telefone"));
                f.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
                f.setSalario(rs.getDouble("salario"));
                f.setCargo(rs.getString("cargo"));

                listaFuncionarios.add(f);
            }
        } catch (SQLException e) {
            System.out.println("ERRO ao retornar as Funcionarios -> " + e);
        } finally {
            con.close();
            System.out.println("Conexão fechada. (Retornar Funcionarios)");
        }

        return listaFuncionarios;

    }

    public void addFuncionario(Funcionario f) throws SQLException{
        con = ConexaoDB.getConexao();

        String sql = "INSERT INTO tb_funcionario(nome, cpf, email, senha, telefone, data_nascimento, salario, cargo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, f.getNome());
            ps.setString(2, f.getCpf());
            ps.setString(3, f.getEmail());
            ps.setString(4, f.getSenha());
            ps.setString(5, f.getTelefone());
            ps.setDate(6, Date.valueOf(f.getDataNascimento()));
            ps.setDouble(7, f.getSalario());
            ps.setString(8, f.getCargo());

            ps.executeUpdate();
        } catch (SQLException e){

            if ("42501".equals(e.getSQLState())) { // Codigo 42501 = sql permission denied
                ViewPopups.mostrarPopupErro("Você não possui permissão para realizar esta operação.");
            }

            System.out.println("ERRO, não foi possível salvar no banco -> " + e);
        } finally{
            con.close();
            System.out.println("Conexão fechada (Add Funcionario)");
        }
    }

    public void deletarFuncionario(int id) {
        con = ConexaoDB.getConexao();
        String sql = "DELETE FROM tb_funcionario WHERE id = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            if ("42501".equals(e.getSQLState())) { // Codigo 42501 = sql permission denied
                ViewPopups.mostrarPopupErro("Você não possui permissão para realizar esta operação.");
            }

            if ("23503".equals(e.getSQLState())) { // Codigo 23503 = violates foreign key constraints
                ViewPopups.mostrarPopupErro("Operação bloqueada, existem chaves estrangeiras vinculadas a esse funcionário.");
            }

            e.printStackTrace();
        }
    }

    public void editarFuncionario(int id, Funcionario f) throws SQLException{
        con = ConexaoDB.getConexao();

        String sql = "UPDATE tb_funcionario SET nome=?, cpf=?, email=?, senha=?, telefone=?, data_nascimento=?, salario=?, cargo=? WHERE id=?";

        try{
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, f.getNome());
            ps.setString(2, f.getCpf());
            ps.setString(3, f.getEmail());
            ps.setString(4, f.getSenha());
            ps.setString(5, f.getTelefone());
            ps.setDate(6, Date.valueOf(f.getDataNascimento()));
            ps.setDouble(7, f.getSalario());
            ps.setString(8, f.getCargo());
            ps.setInt(9, id);

            ps.executeUpdate();
        } catch (SQLException e){
            if ("42501".equals(e.getSQLState())) { // Codigo 42501 = sql permission denied
                ViewPopups.mostrarPopupErro("Você não possui permissão para realizar esta operação.");
            }

            System.out.println("ERRO, não foi possível salvar no banco -> " + e);
        } finally{
            con.close();
            System.out.println("Conexão fechada (Editar Funcionario)");
        }
    }

}
