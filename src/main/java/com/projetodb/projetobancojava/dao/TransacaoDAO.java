package com.projetodb.projetobancojava.dao;

import com.projetodb.projetobancojava.database.ConexaoDB;
import com.projetodb.projetobancojava.model.Transacao;
import com.projetodb.projetobancojava.view.ViewPopups;

import java.sql.*;
import java.util.ArrayList;

public class TransacaoDAO {

    private Connection con;

    public ArrayList<Transacao> retornaListaTransacao() throws SQLException {

        con = ConexaoDB.getConexao();
        String sql = "SELECT * FROM tb_transacao";

        ArrayList<Transacao> listaTransacao = new ArrayList<>();

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Transacao t = new Transacao();

                t.setId(rs.getInt("id"));
                t.setTipo(rs.getString("tipo"));
                t.setValor(rs.getDouble("valor"));
                t.setDataEnvio(rs.getDate("data_envio").toLocalDate());

                t.setContaDestinoId((Integer) rs.getObject("fk_conta_destino_id"));
                t.setContaOrigemId((Integer) rs.getObject("fk_conta_origem_id"));

                listaTransacao.add(t);
            }
        } catch (SQLException e) {
            System.out.println("ERRO ao retornar as Transacoes -> " + e);
        } finally {
            con.close();
            System.out.println("Conexão fechada. (Retornar Transacoes)");
        }

        return listaTransacao;
    }

    public void addTransacao(Transacao t) throws SQLException{
        con = ConexaoDB.getConexao();

        switch (t.getTipo()) {
            case "saque":

                String sql = "CALL sp_saque(?, ?)";

                try {
                    CallableStatement cs = con.prepareCall(sql);

                    cs.setInt(1, t.getContaOrigemId());
                    cs.setDouble(2, t.getValor());

                    cs.execute();
                } catch (SQLException e) {
                    System.out.println("Erro ao adicionar saque -> " + e);
                } finally {
                    con.close();
                }

                break;

            case "deposito":

                String sq = "CALL sp_depositar(?, ?)";

                try {
                    CallableStatement cs = con.prepareCall(sq);

                    cs.setInt(1, t.getContaDestinoId());
                    cs.setDouble(2, t.getValor());

                    cs.execute();

                } catch (SQLException e) {
                    System.out.println("Erro ao adicionar depósito -> " + e);
                } finally {
                    con.close();
                }

                break;

            case "transferencia":

                String s = "CALL sp_transferir(?, ?, ?)";

                try {
                    CallableStatement cs = con.prepareCall(s);

                    cs.setInt(1, t.getContaOrigemId());
                    cs.setInt(2, t.getContaDestinoId());
                    cs.setDouble(3, t.getValor());

                    cs.execute();

                } catch (SQLException e) {
                    System.out.println("Erro ao adicionar transferência -> " + e);
                } finally {
                    con.close();
                }

                break;
        }

    }

    public void deletarTransacao(int id) {
        con = ConexaoDB.getConexao();
        String sql = "DELETE FROM tb_transacao WHERE id = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            if ("42501".equals(e.getSQLState())) { // Codigo 42501 = sql permission denied
                ViewPopups.mostrarPopupErro("Você não possui permissão para realizar esta operação.");
            }

            e.printStackTrace();
        }
    }

    public void editarTransacao(int id, Transacao t) throws SQLException{
        con = ConexaoDB.getConexao();

        String sql = "UPDATE tb_transacao SET tipo=?, valor=?, data_envio=?, fk_conta_destino_id=?, fk_conta_origem_id=? WHERE id=?";

        try{
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, t.getTipo());
            ps.setDouble(2, t.getValor());
            ps.setDate(3, Date.valueOf(t.getDataEnvio()));
            if (t.getContaDestinoId() == null) { ps.setNull(4, java.sql.Types.INTEGER); }
            else { ps.setInt(4, t.getContaDestinoId()); }

            if (t.getContaOrigemId() == null){ ps.setNull(5, java.sql.Types.INTEGER); }
            else { ps.setInt(5, t.getContaOrigemId()); }
            ps.setInt(6, id);

            ps.executeUpdate();
        } catch (SQLException e){
            System.out.println("ERRO, não foi possível salvar no banco -> " + e);
        } finally{
            con.close();
            System.out.println("Conexão fechada (Editar Transacao)");
        }
    }

}
