package com.academia.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;

import com.academia.dao.ConexaoDB;
import com.academia.model.Aluno;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.fxml.Initializable;

public class AlteraAlunoController implements Initializable {

    @FXML
    private TextField altBairro;

    @FXML
    private Button altCadastro;

    @FXML
    private TextField altContato;

    @FXML
    private TextField altEmail;

    @FXML
    private TextField altEnd;

    @FXML
    private TextField altMensal;

    @FXML
    private TextField altNome;

    @FXML
    private TextField buscaMatricula;

    @FXML
    private DatePicker alteraVencimento;

    private Connection conexao = ConexaoDB.getConnection();

    @FXML
    void buscaAluno(ActionEvent event) {

        if(buscaMatricula.getText().isEmpty()){
            Alert erro = new Alert(Alert.AlertType.ERROR);
            erro.setTitle("Erro ao consultar aluno");
            erro.setHeaderText("Campo 'matrícula' vazio");
            erro.setContentText("Por favor, insira a matrícula do aluno");
            erro.showAndWait();

        } else {

            Aluno novoAluno = new Aluno();

            novoAluno.setMatricula(Integer.parseInt(buscaMatricula.getText()));

            String sql = "SELECT * FROM alunos WHERE matricula = (?)";

            try {
                PreparedStatement std = conexao.prepareStatement(sql);

                std.setInt(1, novoAluno.getMatricula());

                ResultSet rs = std.executeQuery();

                while(rs.next()){
                    altNome.setText(rs.getString("nome"));
                    altEmail.setText(rs.getString("email"));
                    altEnd.setText(rs.getString("endereco"));
                    altBairro.setText(rs.getString("bairro"));
                    altContato.setText(rs.getString("contato"));
                    altMensal.setText(rs.getString("mensalidade"));
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Falha ao acessar Banco de Dados");
            }
        }
    }

    @FXML
    void atualizaCadastro(ActionEvent event) {

        Aluno atualizaAluno = new Aluno();
        String sql = "SELECT * FROM alunos WHERE matricula = (?)";
        int bus = 0;

        if(buscaMatricula.getText().isEmpty()){
            Alert atualizado = new Alert(Alert.AlertType.ERROR);
            atualizado.setTitle("Erro ao atualizar");
            atualizado.setHeaderText("Informe a matrícula do aluno");
            atualizado.showAndWait();

        } else {

            atualizaAluno.setMatricula(Integer.parseInt(buscaMatricula.getText()));

            try {
                PreparedStatement pre = conexao.prepareStatement(sql);
                pre.setInt(1, atualizaAluno.getMatricula());
                ResultSet rs = pre.executeQuery();

                while(rs.next()){
                    bus = (rs.getInt("matricula"));
                }

            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            if(atualizaAluno.getMatricula() != bus){
                Alert atualizado = new Alert(Alert.AlertType.ERROR);
                atualizado.setTitle("Erro ao atualizar");
                atualizado.setHeaderText("Matrícula não encontrada");
                atualizado.showAndWait();

            }   else {
                atualizaAluno.setMatricula(Integer.parseInt(buscaMatricula.getText()));
                String atualiza = "UPDATE alunos SET nome = (?), email = (?), endereco = (?), bairro = (?), contato = (?), mensalidade = (?), vencimento = (?) WHERE matricula = (?)";

                LocalDate alteraData = alteraVencimento.getValue();
                String formataData = alteraData.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                try {

                    PreparedStatement ref = conexao.prepareStatement(atualiza);

                    ref.setString(1, altNome.getText());
                    ref.setString(2, altEmail.getText());
                    ref.setString(3, altEnd.getText());
                    ref.setString(4, altBairro.getText());
                    ref.setString(5, altContato.getText());
                    ref.setFloat(6, Float.parseFloat(altMensal.getText()));
                    ref.setString(7, formataData);
                    ref.setInt(8, atualizaAluno.getMatricula());
                    ref.execute();
                    ref.close();

                    Alert atualizado = new Alert(Alert.AlertType.CONFIRMATION);
                    atualizado.setTitle("Cadastro atualizado");
                    atualizado.setHeaderText("As informações foram atualizadas");
                    atualizado.showAndWait();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @FXML
    void excluirAluno(ActionEvent event){

        String b = "SELECT * FROM alunos WHERE matricula = (?)";
        int receba = 0;

        try {

            Aluno excluirAluno = new Aluno();

            excluirAluno.setMatricula(Integer.parseInt(buscaMatricula.getText()));

            PreparedStatement se = conexao.prepareStatement(b);
            se.setInt(1, excluirAluno.getMatricula());
            ResultSet rs = se.executeQuery();

            while(rs.next()){
                receba = (rs.getInt("matricula"));
            }

            if(excluirAluno.getMatricula() != receba){
                Alert deleta = new Alert(Alert.AlertType.ERROR);
                deleta.setTitle("Erro ao excluir");
                deleta.setHeaderText("Falha ao excluir aluno");
                deleta.setContentText("Certifique-se que inseriu corretamente a matrícula do aluno");
                deleta.showAndWait();

            } else {
                String delete = "DELETE FROM alunos WHERE matricula = (?)";

                PreparedStatement del = conexao.prepareStatement(delete);

                del.setInt(1, excluirAluno.getMatricula());
                del.execute();
                del.close();

                Alert deleta = new Alert(Alert.AlertType.INFORMATION);
                deleta.setTitle("Deletar Aluno");
                deleta.setHeaderText("Aluno excluído");
                deleta.setContentText("Futuramente você poderá cadastrá-lo novamente");
                deleta.showAndWait();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }

}
