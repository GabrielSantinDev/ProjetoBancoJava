package com.projetodb.projetobancojava.dao;

import com.projetodb.projetobancojava.database.ConexaoDB;
import com.projetodb.projetobancojava.model.Conta;
import com.projetodb.projetobancojava.view.ViewPopups;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ContaDAO {

    private Connection con;

    public ArrayList<Conta> retornaListaContas() throws SQLException {

        con = ConexaoDB.getConexao();
        String sql = "SELECT * FROM tb_conta";

        ArrayList<Conta> listaContas = new ArrayList<>();

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Conta c = new Conta();

                c.setId(rs.getInt("id"));
                c.setNumero(rs.getString("numero"));
                c.setSaldo(rs.getDouble("saldo"));
                c.setSenha(rs.getString("senha"));
                c.setTipo(rs.getString("tipo"));
                c.setClienteId(rs.getInt("fk_cliente_id"));

                listaContas.add(c);
            }
        } catch (SQLException e) {
            System.out.println("ERRO ao retornar as contas -> " + e);
        } finally {
            con.close();
            System.out.println("Conexão fechada. (Retornar Contas)");
        }

        return listaContas;
    }

    public void addConta(Conta c) throws SQLException{
        con = ConexaoDB.getConexao();

        String sql = "INSERT INTO tb_conta(numero, tipo, saldo, senha, fk_cliente_id) VALUES (?, ?, ?, ?, ?)";

        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, c.getNumero());
            ps.setString(2, c.getTipo());
            ps.setDouble(3, c.getSaldo());
            ps.setString(4, c.getSenha());
            ps.setInt(5, c.getClienteId());

            ps.executeUpdate();
        } catch (SQLException e){
            System.out.println("ERRO, não foi possível salvar no banco -> " + e);
        } finally{
            con.close();
            System.out.println("Conexão fechada (Add conta)");
        }
    }

    public void deletarConta(int id) {
        con = ConexaoDB.getConexao();
        String sql = "DELETE FROM tb_conta WHERE id = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            if ("42501".equals(e.getSQLState())) { // Codigo 42501 = sql permission denied
                ViewPopups.mostrarPopupErro("Você não possui permissão para realizar esta operação.");
            }
            if ("23503".equals(e.getSQLState())) { // Codigo 23503 = violates foreign key constraints
                ViewPopups.mostrarPopupErro("Operação bloqueada, existem chaves estrangeiras vinculadas a essa conta.");
            }

            e.printStackTrace();
        }
    }

    public void editarConta(int id, Conta c) throws SQLException{
        con = ConexaoDB.getConexao();

        String sql = "UPDATE tb_conta SET numero=?, tipo=?, saldo=?, senha=?, fk_cliente_id=? WHERE id=?";

        try{
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, c.getNumero());
            ps.setString(2, c.getTipo());
            ps.setDouble(3, c.getSaldo());
            ps.setString(4, c.getSenha());
            ps.setInt(5, c.getClienteId());
            ps.setInt(6, id);

            ps.executeUpdate();
        } catch (SQLException e){
            if ("42501".equals(e.getSQLState())) { // Codigo 42501 = sql permission denied
                ViewPopups.mostrarPopupErro("Você não possui permissão para realizar esta operação.");
            }

            System.out.println("ERRO, não foi possível salvar no banco -> " + e);
        } finally{
            con.close();
            System.out.println("Conexão fechada (Editar conta)");
        }
    }

}
