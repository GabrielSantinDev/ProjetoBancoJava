package com.projetodb.projetobancojava.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDB {

    private static final String URL = "jdbc:postgresql://localhost:5432/db_projeto_banco";
    private static final String DRIVER = "org.postgresql.Driver";
    private static String user = "login_user";
    private static String password = "login_user";

    private static Connection con = null;

    public static Connection getConexao(){

        try {
            Class.forName(DRIVER);

            con = DriverManager.getConnection(URL, user, password);

            System.out.println("Conexao com o banco bem sucedido!");
            return con;
        } catch (ClassNotFoundException e) {
            System.out.println("ERRO driver -> " + e);
        } catch (SQLException e){
            System.out.println("ERRO SQL -> " + e);
        }

        return con;
    }

    public static void logarUsuario(String userNovo, String passwordNovo) {
        user = userNovo;
        password = passwordNovo;
    }

}
