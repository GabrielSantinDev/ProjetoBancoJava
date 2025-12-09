package com.projetodb.projetobancojava.dao;

import com.projetodb.projetobancojava.database.ConexaoDB;
import com.projetodb.projetobancojava.model.Cliente;
import com.projetodb.projetobancojava.view.ViewPopups;

import java.sql.*;
import java.util.ArrayList;

public class ClienteDAO {

    private Connection con;

    public ArrayList<Cliente> retornaListaClientes() throws SQLException {

        con = ConexaoDB.getConexao();
        String sql = "SELECT * FROM tb_cliente";

        ArrayList<Cliente> listaClientes = new ArrayList<>();

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Cliente c = new Cliente();

                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                c.setCpf(rs.getString("cpf"));
                c.setEmail(rs.getString("email"));
                c.setTelefone(rs.getString("telefone"));
                c.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());

                listaClientes.add(c);
            }
        } catch (SQLException e) {
            System.out.println("ERRO ao retornar as Clientes -> " + e);
        } finally {
            con.close();
            System.out.println("Conexão fechada. (Retornar Clientes)");
        }

        return listaClientes;
    }

    public void addCliente(Cliente c) throws SQLException{
        con = ConexaoDB.getConexao();

        String sql = "INSERT INTO tb_cliente(nome, cpf, email, telefone, data_nascimento) VALUES (?, ?, ?, ?, ?)";

        try{
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, c.getNome());
            ps.setString(2, c.getCpf());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getTelefone());
            ps.setDate(5, Date.valueOf(c.getDataNascimento()));

            ps.executeUpdate();
        } catch (SQLException e){
            System.out.println("ERRO, não foi possível salvar no banco -> " + e);
        } finally{
            con.close();
            System.out.println("Conexão fechada (Add Cliente)");
        }
    }

    public void deletarCliente(int id) {
        con = ConexaoDB.getConexao();
        String sql = "DELETE FROM tb_cliente WHERE id = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            if ("42501".equals(e.getSQLState())) { // Codigo 42501 = sql permission denied
                ViewPopups.mostrarPopupErro("Você não possui permissão para realizar esta operação.");
            }
            if ("23503".equals(e.getSQLState())) { // Codigo 23503 = violates foreign key constraints
                ViewPopups.mostrarPopupErro("Operação bloqueada, existem chaves estrangeiras vinculadas a esse cliente.");
            }

            e.printStackTrace();
        }
    }

    public void editarCliente(int id, Cliente c) throws SQLException{
        con = ConexaoDB.getConexao();

        String sql = "UPDATE tb_cliente SET nome=?, cpf=?, email=?, telefone=?, data_nascimento=? WHERE id=?";

        try{
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, c.getNome());
            ps.setString(2, c.getCpf());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getTelefone());
            ps.setDate(5, Date.valueOf(c.getDataNascimento()));
            ps.setInt(6, id);

            ps.executeUpdate();
        } catch (SQLException e){
            if ("42501".equals(e.getSQLState())) { // Codigo 42501 = sql permission denied
                ViewPopups.mostrarPopupErro("Você não possui permissão para realizar esta operação.");
            }

            System.out.println("ERRO, não foi possível salvar no banco -> " + e);
        } finally{
            con.close();
            System.out.println("Conexão fechada (Editar Cliente)");
        }
    }

}
