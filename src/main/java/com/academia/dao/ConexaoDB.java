package com.academia.dao;

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 *   @author Gustavo Henrique
 *
 *           Conexão com o banco de dados
 *
 */

public class ConexaoDB {

    private static String url = "jdbc:postgresql://localhost:5432/LGC";
    private static String driver = "org.postgresql.Driver";
    private static String usuario = "postgres";
    private static String senha = "15421542";

    public static Connection getConnection() {

        Connection conexao = null;

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao conectar ao Banco de Dados");
        }

        try {
            conexao = DriverManager.getConnection(url, usuario, senha);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conexao;

    }

}