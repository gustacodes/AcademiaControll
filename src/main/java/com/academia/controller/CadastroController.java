package com.academia.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.ResourceBundle;

import com.academia.dao.ConexaoDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

/**
 *
 *   @author Gustavo Henrique
 *
 *      e-mail: gustalencarvalho@gmail.com
 *
 */

public class CadastroController implements Initializable {

    @FXML
    private Button cadastrarBotao;

    @FXML
    private TextField contatoCampo;

    @FXML
    private TextField emailCampo;

    @FXML
    private TextField nomeCampo;

    @FXML
    private TextField endCampo;

    @FXML
    private TextField baiCampo;

    @FXML
    private TextField txtMensal;

    @FXML
    private DatePicker vencimento;

    //Chamando a conexão com o Banco de Dados
    private Connection conexao = ConexaoDB.getConnection();

    //Função para cadastrar um aluno
    @FXML
    void cadBotao(ActionEvent event) {

        LocalDate dataVencimento = vencimento.getValue();
        String formataData = dataVencimento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        //Função para gerar a matrícula de cada aluno aleatoriamente
        Random rand = new Random();
        int matricula = rand.nextInt(10000) + 1;

        if(txtMensal.getText().isEmpty()){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Falha no cadastro");
            a.setHeaderText("Dados não inseridos");
            a.setContentText("Por favor, preencha todos os dados do cadastro.");
            a.showAndWait();
        } else {

            //Verificando se um dos campos estão vazios. Se houver algum, o programa retornará um erro
            if(nomeCampo.getText().isEmpty() || endCampo.getText().isEmpty() || baiCampo.getText().isEmpty() || contatoCampo.getText().isEmpty() || emailCampo.getText().isEmpty()){

                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Falha no cadastro");
                a.setHeaderText("Dados não inseridos");
                a.setContentText("Por favor, preencha todos os dados do cadastro.");
                a.showAndWait();
            } else {

                //Caso esteja tudo preenchido corretamente, os dados serão inseridos no Banco de Dados e o aluno estará matriculado
                String sql = "INSERT INTO alunos (matricula, nome, endereco, bairro, contato, email, mensalidade, vencimento) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                try {
                    //Conectando ao Banco de Dados e inserindo os dados no BD
                    PreparedStatement cadAluno = conexao.prepareStatement(sql);

                    cadAluno.setInt(1, matricula);
                    cadAluno.setString(2, nomeCampo.getText());
                    cadAluno.setString(3, endCampo.getText());
                    cadAluno.setString(4, baiCampo.getText());
                    cadAluno.setString(5, contatoCampo.getText());
                    cadAluno.setString(6, emailCampo.getText());
                    cadAluno.setFloat(7, Float.parseFloat(txtMensal.getText()));
                    cadAluno.setString(8, formataData);

                    cadAluno.execute();
                    cadAluno.close();

                    //Comando apenas para informar que o aluno foi cadastrado com sucesso
                    Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                    a.setTitle("Cadastro");
                    a.setContentText("O aluno " + nomeCampo.getText() + " foi cadastrado com sucesso!");
                    a.setHeaderText("Aluno cadastrado!");
                    a.showAndWait();

                } catch (Exception e) {

                    //Caso haja algum erro acima, ele entrará aqui informando o erro
                    Alert erro = new Alert(Alert.AlertType.ERROR);
                    erro.setTitle("Eror");
                    erro.setContentText("Algo deu errado");
                    erro.setHeaderText("Erro ao cadastrar aluno. Verifique os dados e tente novamente");
                }
            }
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}