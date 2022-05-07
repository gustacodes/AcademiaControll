package com.academia.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.academia.dao.ConexaoDB;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class FinanceiroController implements Initializable {

    @FXML
    private Label totalAlunos;

    @FXML
    private Label totalAtraso;

    @FXML
    private Label totalFaturamento;

    @FXML
    private Label totalPendente;

    private Connection conexao = ConexaoDB.getConnection();

    void financeiro(){
        String sql = "SELECT * FROM alunos";
        String vencidos = null;
        int qtdAlunos = 0, qtdAtrasados = 0;
        double soma = 0, pendentes = 0;

        try {
            PreparedStatement std = conexao.prepareStatement(sql);
            ResultSet rs = std.executeQuery();

            while(rs.next()){

                int matricula = rs.getInt("matricula");
                double totalPago = rs.getDouble("mensalidade");
                vencidos = rs.getString("vencimento");
                String atraso = rs.getString("atrasados");

                if(atraso.equals("Em atraso")){
                    qtdAtrasados++;
                    pendentes += totalPago;
                } else {
                    soma += totalPago;
                }

                qtdAlunos++;
            }

            totalAtraso.setText(Integer.toString(qtdAtrasados));
            totalAlunos.setText(Integer.toString(qtdAlunos));
            totalPendente.setText(Double.toString(pendentes));
            totalFaturamento.setText(Double.toString(soma));


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        financeiro();
    }

}
