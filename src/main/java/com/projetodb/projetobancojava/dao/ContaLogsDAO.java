package com.projetodb.projetobancojava.dao;

import com.projetodb.projetobancojava.database.ConexaoDB;
import com.projetodb.projetobancojava.model.ContaLogs;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class ContaLogsDAO {

    private Connection con;

    public ArrayList<ContaLogs> retornaListaContaLogs() throws SQLException {

        con = ConexaoDB.getConexao();
        String sql = "SELECT * FROM tb_conta_logs";

        ArrayList<ContaLogs> listaContaLogs = new ArrayList<>();

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                ContaLogs c = new ContaLogs();

                c.setId(rs.getInt("id"));
                c.setSaldoAntigo(rs.getDouble("saldo_antigo"));
                c.setSaldoNovo(rs.getDouble("saldo_novo"));
                c.setDataMudanca(rs.getTimestamp("data_mudanca").toLocalDateTime());
                c.setContaId(rs.getInt("fk_conta_id"));

                listaContaLogs.add(c);
            }
        } catch (SQLException e) {
            System.out.println("ERRO ao retornar as Logs de Contas -> " + e);
        } finally {
            con.close();
            System.out.println("Conexão fechada. (Retornar Logs Contas)");
        }

        return listaContaLogs;
    }

    public ArrayList<ContaLogs> retornaListaExtrato(Integer conta_id, LocalDate data_inicio, LocalDate data_final) throws SQLException {

        con = ConexaoDB.getConexao();
        String sql = "SELECT * FROM fn_extrato_transacoes(?, ?, ?);";

        ArrayList<ContaLogs> listaContaLogs = new ArrayList<>();

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, conta_id);
            ps.setDate(2, Date.valueOf(data_inicio));
            ps.setDate(3, Date.valueOf(data_final));

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                ContaLogs c = new ContaLogs();

                c.setSaldoAntigo(rs.getDouble("saldo_antigo"));
                c.setSaldoNovo(rs.getDouble("saldo_novo"));
                c.setDataMudanca(rs.getTimestamp("data_mudanca").toLocalDateTime());
                c.setContaId(rs.getInt("fk_conta_id"));

                listaContaLogs.add(c);
            }
        } catch (SQLException e) {
            System.out.println("ERRO ao retornar as Logs de Contas -> " + e);
        } finally {
            con.close();
            System.out.println("Conexão fechada. (Retornar Logs Contas)");
        }

        return listaContaLogs;
    }

}
